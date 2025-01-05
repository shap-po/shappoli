package com.github.shap_po.shappoli.power.type;

import com.github.shap_po.shappoli.data.ShappoliDataTypes;
import com.github.shap_po.shappoli.util.MiscUtil;
import io.github.apace100.apoli.action.EntityAction;
import io.github.apace100.apoli.condition.EntityCondition;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.data.TypedDataObjectFactory;
import io.github.apace100.apoli.power.PowerConfiguration;
import io.github.apace100.apoli.power.type.PowerType;
import io.github.apace100.apoli.util.HudRender;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class ActionOnKeyPressPowerType extends ActiveCooldownPowerType {
    public static final TypedDataObjectFactory<ActionOnKeyPressPowerType> DATA_FACTORY = PowerType.createConditionedDataFactory(
        new SerializableData()
            .add("entity_action", EntityAction.DATA_TYPE)
            .add("hud_render", ApoliDataTypes.HUD_RENDER, HudRender.DONT_RENDER)
            .add("cooldown", SerializableDataTypes.INT, 1)
            .add("key", ShappoliDataTypes.ACTIVE_ANY_KEY, null)
            .add("keys", ShappoliDataTypes.ACTIVE_ANY_KEY.list(), null)
            .add("bound_only", SerializableDataTypes.BOOLEAN, true)
            .add("continuous", SerializableDataTypes.BOOLEAN, false),
        (data, condition) -> new ActionOnKeyPressPowerType(
            data.<EntityAction>get("entity_action"),
            data.get("hud_render"),
            data.getInt("cooldown"),
            MiscUtil.listFromData(data, "key", "keys"),
            data.getBoolean("bound_only"),
            data.getBoolean("continuous"),
            condition
        ),
        (powerType, serializableData) -> serializableData.instance()
            .set("entity_action", powerType.entityAction)
            .set("hud_render", powerType.getRenderSettings())
            .set("cooldown", powerType.getCooldown())
            .set("keys", powerType.getKeys())
            .set("bound_only", powerType.isBoundOnly())
            .set("continuous", powerType.isContinuous())
    );

    protected final Optional<EntityAction> entityAction;

    public ActionOnKeyPressPowerType(
        Optional<EntityAction> entityAction,
        HudRender hudRender,
        int cooldownDuration,
        List<Key> keys,
        boolean boundOnly,
        boolean continuous,
        Optional<EntityCondition> condition
    ) {
        super(hudRender, cooldownDuration, keys.stream().peek(key -> key.continuous = continuous).toList(), boundOnly, continuous, condition);
        this.entityAction = entityAction;
    }

    public ActionOnKeyPressPowerType(
        EntityAction entityAction,
        HudRender hudRender,
        int cooldownDuration,
        List<Key> keys,
        boolean boundOnly,
        boolean continuous,
        Optional<EntityCondition> condition
    ) {
        this(Optional.of(entityAction), hudRender, cooldownDuration, keys, boundOnly, continuous, condition);
    }

    @Override
    public @NotNull PowerConfiguration<?> getConfig() {
        return ShappoliPowerTypes.ACTION_ON_KEY_PRESS;
    }

    @Override
    public void onUse(Key key) {
        super.onUse(key);
        entityAction.ifPresent(entityAction -> entityAction.execute(getHolder()));
    }
}
