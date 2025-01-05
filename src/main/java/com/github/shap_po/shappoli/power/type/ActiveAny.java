package com.github.shap_po.shappoli.power.type;

import com.github.shap_po.shappoli.ShappoliClient;
import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.mixin.KeyBindingAccessor;
import io.github.apace100.apoli.power.type.PowerType;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.util.Pair;

import java.util.*;
import java.util.stream.Stream;

/**
 * A modified version of {@link io.github.apace100.apoli.power.type.Active} that allows listening to multiple keys
 */
public interface ActiveAny {
    void onUse(Key key);

    default boolean canTrigger() {
        return true;
    }

    List<Key> getKeys();

    Stream<Key> getPressedKeys(List<KeyBinding> keyBindings, Map<String, Boolean> keybindingStates);

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

        List<Pair<PowerType, Key>> triggeredPowers = new LinkedList<>();

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

    class Key {
        public static final SerializableData DATA = new SerializableData()
            .add("key", SerializableDataTypes.STRING, null)
            .add("category", SerializableDataTypes.STRING, null)
            .add("continuous", SerializableDataTypes.BOOLEAN, false);

        String key;
        String category;
        public boolean continuous = false;

        public static Key fromData(SerializableData.Instance dataInstance) {
            Key key = new Key();
            key.key = dataInstance.getString("key");
            key.category = dataInstance.getString("category");
            key.continuous = dataInstance.getBoolean("continuous");
            return key;
        }

        public SerializableData.Instance toData(SerializableData data) {
            SerializableData.Instance dataInstance = data.new Instance();
            dataInstance.set("key", key);
            dataInstance.set("category", category);
            dataInstance.set("continuous", continuous);
            return dataInstance;
        }

        @Override
        public String toString() {
            return "Key{" +
                "key='" + key + '\'' +
                ", category='" + category + '\'' +
                ", continuous=" + continuous +
                '}';
        }

        @Override
        public boolean equals(final Object obj) {
            if (obj == this) {
                return true;
            }

            if (!(obj instanceof Key otherKey)) {
                return false;
            }

            return Objects.equals(this.key, otherKey.key)
                && Objects.equals(this.category, otherKey.category)
                && this.continuous == otherKey.continuous;
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.key, this.category, this.continuous);
        }
    }
}
