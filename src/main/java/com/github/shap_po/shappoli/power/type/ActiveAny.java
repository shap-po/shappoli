package com.github.shap_po.shappoli.power.type;

import com.github.shap_po.shappoli.ShappoliClient;
import com.github.shap_po.shappoli.util.ShappoliKeyBindingReference;
import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.mixin.KeyBindingAccessor;
import io.github.apace100.apoli.power.type.PowerType;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.util.Pair;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * A modified version of {@link io.github.apace100.apoli.power.type.Active} that allows listening to multiple keys
 */
public interface ActiveAny {
    void onUse(ShappoliKeyBindingReference key);

    default boolean canTrigger() {
        return true;
    }

    List<ShappoliKeyBindingReference> getKeys();

    Stream<ShappoliKeyBindingReference> getPressedKeys(List<KeyBinding> keyBindings, Map<String, Boolean> keybindingStates);

    @Environment(EnvType.CLIENT)
    static void integrateCallback(MinecraftClient client) {
        if (client.player == null) {
            return;
        }

        List<PowerType> powers = PowerHolderComponent.getOptional(client.player)
            .map(c -> c
                .getPowerTypes().stream()
                .filter(ActiveAny.class::isInstance)
                .toList())
            .orElse(List.of());

        if (powers.isEmpty()) {
            return;
        }

        List<Pair<PowerType, ShappoliKeyBindingReference>> triggeredPowers = new LinkedList<>();

        List<KeyBinding> allKeyBindings = KeyBindingAccessor.getKeysById().values().stream().toList();
        Map<String, Boolean> currentKeybindingStates = allKeyBindings.stream()
            .collect(HashMap::new, (map, keyBinding) -> map.put(keyBinding.getTranslationKey(), keyBinding.isPressed()), HashMap::putAll);

        for (PowerType power : powers) {
            if (!(power instanceof ActiveAny activePower) || !activePower.canTrigger()) {
                continue;
            }
            activePower.getPressedKeys(allKeyBindings, currentKeybindingStates)
                .filter(key -> key.continuous || !ShappoliClient.lastKeyBindingStates.getOrDefault(key.key, false))
                .findFirst()
                .ifPresent(pressedKey -> triggeredPowers.add(new Pair<>(power, pressedKey)));
        }

        ShappoliClient.lastKeyBindingStates.putAll(currentKeybindingStates);

        if (!triggeredPowers.isEmpty()) {
            ShappoliClient.performActivePowers(triggeredPowers);
        }
    }
}
