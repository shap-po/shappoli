package com.github.shap_po.shappoli.power.type;

import com.github.shap_po.shappoli.Shappoli;
import com.github.shap_po.shappoli.util.MiscUtil;
import com.google.common.collect.Streams;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.factory.PowerTypeFactory;
import io.github.apace100.apoli.power.type.CooldownPowerType;
import io.github.apace100.apoli.util.HudRender;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class ActiveCooldownPowerType extends CooldownPowerType implements ActiveAny {
    protected final @Nullable Consumer<Entity> activeFunction;
    private final List<Key> keys;
    private final List<String> categories;
    private final boolean boundOnly;
    private final boolean continuous;

    public ActiveCooldownPowerType(
        Power power, LivingEntity entity,
        int cooldownDuration,
        HudRender hudRender,
        @Nullable Consumer<Entity> activeFunction,
        List<Key> keys,
        List<String> categories,
        boolean boundOnly,
        boolean continuous
    ) {
        super(power, entity, cooldownDuration, hudRender);
        this.activeFunction = activeFunction;
        this.keys = keys;
        this.categories = categories;
        this.boundOnly = boundOnly;
        this.continuous = continuous;
    }

    @Override
    public List<Key> getKeys() {
        return keys;
    }

    @Override
    public Stream<Key> getPressedKeys(List<KeyBinding> keyBindings, Map<String, Boolean> keybindingStates) {
        // if both keys and categories are empty, find all pressed keys
        if (keys.isEmpty() && categories.isEmpty()) {
            return keyBindings.stream()
                .filter(KeyBinding::isPressed)
                .filter(keyBinding -> !boundOnly || !keyBinding.isUnbound())
                .map(this::keyFromKeyBinding);
        }

        // otherwise, find all pressed keys that match key ids or categories
        return Streams.concat(
            keys.stream().filter(key -> keybindingStates.getOrDefault(key.key, false)),
            keyBindings.stream()
                .filter(KeyBinding::isPressed)
                .flatMap(keyBinding -> categories.stream()
                    .filter(group -> keyBinding.getCategory().equals(group))
                    .map(group -> keyFromKeyBinding(keyBinding))
                )
        );
    }

    private Key keyFromKeyBinding(KeyBinding keyBinding) {
        Key key = new Key();
        key.key = keyBinding.getTranslationKey();
        key.continuous = continuous;
        return key;
    }


    @Override
    public void onUse() {
        if (canUse()) {
            if (activeFunction != null) {
                this.activeFunction.accept(this.entity);
            }
            use();
        }
    }

    public static PowerTypeFactory getActiveSelfFactory() {
        return new PowerTypeFactory<>(
            Shappoli.identifier("active_self"),
            new SerializableData()
                .add("entity_action", ApoliDataTypes.ENTITY_ACTION)
                .add("cooldown", SerializableDataTypes.INT, 1)
                .add("hud_render", ApoliDataTypes.HUD_RENDER, HudRender.DONT_RENDER)
                .add("key", ApoliDataTypes.BACKWARDS_COMPATIBLE_KEY, null)
                .add("keys", ApoliDataTypes.BACKWARDS_COMPATIBLE_KEY.list(), null)
                .add("category", SerializableDataTypes.STRING, null)
                .add("categories", SerializableDataTypes.STRINGS, null)
                .add("bound_only", SerializableDataTypes.BOOLEAN, true)
                .add("continuous", SerializableDataTypes.BOOLEAN, false)
            ,
            data -> (power, player) -> new ActiveCooldownPowerType(
                power, player,
                data.getInt("cooldown"),
                data.get("hud_render"),
                data.get("entity_action"),
                MiscUtil.<Key>listFromData(data, "key", "keys")
                    // set the continuous flag for each key from the data
                    .stream().peek(key -> key.continuous = data.getBoolean("continuous")).toList(),
                MiscUtil.listFromData(data, "category", "categories"),
                data.getBoolean("bound_only"),
                data.getBoolean("continuous")
            ))
            .allowCondition();
    }
}
