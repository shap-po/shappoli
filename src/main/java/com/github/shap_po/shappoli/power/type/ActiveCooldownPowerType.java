package com.github.shap_po.shappoli.power.type;

import com.google.common.collect.Streams;
import io.github.apace100.apoli.condition.EntityCondition;
import io.github.apace100.apoli.power.type.CooldownPowerType;
import io.github.apace100.apoli.util.HudRender;
import net.minecraft.client.option.KeyBinding;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public abstract class ActiveCooldownPowerType extends CooldownPowerType implements ActiveAny {
    private final List<Key> keys;
    private final boolean boundOnly;
    private final boolean continuous;

    public ActiveCooldownPowerType(
        HudRender hudRender,
        int cooldownDuration,
        List<Key> keys,
        boolean boundOnly,
        boolean continuous,
        Optional<EntityCondition> condition
    ) {
        super(cooldownDuration, hudRender, condition);
        this.keys = keys;
        this.boundOnly = boundOnly;
        this.continuous = continuous;
    }

    public ActiveCooldownPowerType(
        HudRender hudRender,
        int cooldownDuration,
        List<Key> keys,
        boolean boundOnly,
        boolean continuous
    ) {
        this(hudRender, cooldownDuration, keys, boundOnly, continuous, Optional.empty());
    }

    @Override
    public List<Key> getKeys() {
        return keys;
    }

    public boolean isBoundOnly() {
        return boundOnly;
    }

    public boolean isContinuous() {
        return continuous;
    }

    @Override
    public Stream<Key> getPressedKeys(List<KeyBinding> keyBindings, Map<String, Boolean> keybindingStates) {
        // if both keys and categories are empty, find all pressed keys
        if (keys.isEmpty()) {
            return keyBindings.stream()
                .filter(KeyBinding::isPressed)
                .filter(keyBinding -> !boundOnly || !keyBinding.isUnbound())
                .map(this::keyFromKeyBinding);
        }

        // otherwise, find all pressed keys that match key ids or categories
        return Streams.concat(
            keys.stream()
                .filter(key -> key.key != null)
                .filter(key -> keybindingStates.getOrDefault(key.key, false)),
            keyBindings.stream()
                .filter(KeyBinding::isPressed)
                .flatMap(keyBinding -> keys.stream()
                    .filter(key -> key.category != null)
                    .filter(key -> keyBinding.getCategory().equals(key.category))
                    .map(group -> keyFromKeyBinding(keyBinding))
                )
        );
    }

    private Key keyFromKeyBinding(KeyBinding keyBinding) {
        Key key = new Key();
        key.key = keyBinding.getTranslationKey();
        key.category = keyBinding.getCategory();
        key.continuous = continuous;
        return key;
    }

    @Override
    public void onUse(Key key) {
        use();
    }

    @Override
    public boolean canTrigger() {
        return super.isActive();
    }
}
