package com.github.shap_po.shappoli.power.type;

import com.github.shap_po.shappoli.ShappoliClient;
import io.github.apace100.apoli.ApoliClient;
import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.mixin.KeyBindingAccessor;
import io.github.apace100.apoli.power.type.Active;
import io.github.apace100.apoli.power.type.PowerType;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public interface ActiveAny extends Active {
    List<Key> getKeys();

    Stream<Key> getPressedKeys(List<KeyBinding> keyBindings, Map<String, Boolean> keybindingStates);

    @Override
    default void setKey(Key key) {
        // NO-OP
    }

    @Override
    default Key getKey() {
        return NullKey.INSTANCE;
    }

    @Environment(EnvType.CLIENT)
    static void integrateCallback(MinecraftClient client) {
        if (client.player == null) {
            return;
        }

        List<PowerType> powers = PowerHolderComponent.KEY.get(client.player).getPowerTypes().stream()
            .filter(ActiveAny.class::isInstance)
            .toList();

        if (powers.isEmpty()) {
            return;
        }

        List<PowerType> triggeredPowers = new LinkedList<>();

        List<KeyBinding> allKeyBindings = KeyBindingAccessor.getKeysById().values().stream().toList();
        Map<String, Boolean> currentKeybindingStates = allKeyBindings.stream()
            .collect(HashMap::new, (map, keyBinding) -> map.put(keyBinding.getTranslationKey(), keyBinding.isPressed()), HashMap::putAll);

        for (PowerType power : powers) {
            if (((ActiveAny) power).getPressedKeys(allKeyBindings, currentKeybindingStates)
                .anyMatch(key -> key.continuous || !ShappoliClient.lastKeyBindingStates.getOrDefault(key.key, false))) {
                triggeredPowers.add(power);
            }
        }

        ShappoliClient.lastKeyBindingStates.putAll(currentKeybindingStates);

        if (!triggeredPowers.isEmpty()) {
            ApoliClient.performActivePowers(triggeredPowers);
        }
    }

    class NullKey extends Key {
        static final NullKey INSTANCE = new NullKey();
        /**
         * Override default key value so that {@link ApoliClient#getKeyBinding} will return null and {@link Active#integrateCallback(MinecraftClient)} will not crash
         */
        public String key = null;
    }
}
