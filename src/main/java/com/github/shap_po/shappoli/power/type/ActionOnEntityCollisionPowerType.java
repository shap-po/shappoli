package com.github.shap_po.shappoli.power.type;

import io.github.apace100.apoli.action.BiEntityAction;
import io.github.apace100.apoli.condition.BiEntityCondition;
import io.github.apace100.apoli.condition.EntityCondition;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.data.TypedDataObjectFactory;
import io.github.apace100.apoli.power.PowerConfiguration;
import io.github.apace100.apoli.power.type.CooldownPowerType;
import io.github.apace100.apoli.power.type.PowerType;
import io.github.apace100.apoli.power.type.PreventEntityCollisionPowerType;
import io.github.apace100.apoli.util.HudRender;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class ActionOnEntityCollisionPowerType extends CooldownPowerType {
    public static final TypedDataObjectFactory<ActionOnEntityCollisionPowerType> DATA_FACTORY = PowerType.createConditionedDataFactory(
        new SerializableData()
            .add("cooldown", SerializableDataTypes.INT, 1)
            .add("hud_render", ApoliDataTypes.HUD_RENDER, HudRender.DONT_RENDER)
            .add("bientity_action", BiEntityAction.DATA_TYPE)
            .add("bientity_condition", BiEntityCondition.DATA_TYPE.optional(), Optional.empty())
            .add("tick_rate", SerializableDataTypes.POSITIVE_INT, 20),
        (data, condition) -> new ActionOnEntityCollisionPowerType(
            data.get("cooldown"),
            data.get("hud_render"),
            data.get("bientity_action"),
            data.get("bientity_condition"),
            data.get("tick_rate"),
            condition
        ),
        (powerType, serializableData) -> serializableData.instance()
            .set("cooldown", powerType.getCooldown())
            .set("hud_render", powerType.getRenderSettings())
            .set("bientity_action", powerType.biEntityAction)
            .set("bientity_condition", powerType.biEntityCondition)
            .set("tick_rate", powerType.tickRate)
    );

    private final BiEntityAction biEntityAction;
    private final Optional<BiEntityCondition> biEntityCondition;
    private final int tickRate;

    public ActionOnEntityCollisionPowerType(
        int cooldownDuration, HudRender hudRender,
        BiEntityAction biEntityAction,
        Optional<BiEntityCondition> biEntityCondition,
        int tickRate,
        Optional<EntityCondition> condition
    ) {
        super(cooldownDuration, hudRender, condition);
        this.biEntityAction = biEntityAction;
        this.biEntityCondition = biEntityCondition;
        this.tickRate = tickRate;
        this.setTicking();
    }

    @Override
    public void commonTick() {
        if (!canUse() || getHolder().age % tickRate != 0) {
            return;
        }

        for (Entity other : getCollidingEntities()) {
            if (canUse() &&
                !PreventEntityCollisionPowerType.doesApply(getHolder(), other) &&
                biEntityCondition.map(biEntityCondition -> biEntityCondition.test(getHolder(), other)).orElse(true)
            ) {
                biEntityAction.execute(getHolder(), other);
                use();
            }
        }
    }

    private List<Entity> getCollidingEntities() {
        Entity entity = getHolder();
        return entity.getWorld().getOtherEntities(entity, entity.getBoundingBox());
    }

    @Override
    public @NotNull PowerConfiguration<?> getConfig() {
        return ShappoliPowerTypes.ACTION_ON_ENTITY_COLLISION;
    }
}
