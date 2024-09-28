package com.github.shap_po.shappoli.power.type;

import com.github.shap_po.shappoli.Shappoli;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.factory.PowerTypeFactory;
import io.github.apace100.apoli.power.type.CooldownPowerType;
import io.github.apace100.apoli.power.type.PreventEntityCollisionPowerType;
import io.github.apace100.apoli.util.HudRender;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Pair;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class ActionOnEntityCollisionPowerType extends CooldownPowerType {
    private final Consumer<Pair<Entity, Entity>> bientityAction;
    private final @Nullable Predicate<Pair<Entity, Entity>> bientityCondition;

    public ActionOnEntityCollisionPowerType(
        Power power, LivingEntity entity,
        int cooldownDuration, HudRender hudRender,
        Consumer<Pair<Entity, Entity>> bientityAction,
        @Nullable Predicate<Pair<Entity, Entity>> bientityCondition
    ) {
        super(power, entity, cooldownDuration, hudRender);
        this.bientityAction = bientityAction;
        this.bientityCondition = bientityCondition;
    }

    public void apply() {
        List<Entity> collidingEntities = getCollidingEntities();
        for (Entity other : collidingEntities) {
            if (this.canUse() &&
                !PreventEntityCollisionPowerType.doesApply(entity, other) &&
                (bientityCondition == null || bientityCondition.test(new Pair<>(entity, other)))
            ) {
                bientityAction.accept(new Pair<>(entity, other));
                use();
            }
        }
    }

    private List<Entity> getCollidingEntities() {
        return entity.getWorld().getOtherEntities(entity, entity.getBoundingBox());
    }

    public static PowerTypeFactory getFactory() {
        return new PowerTypeFactory<>(
            Shappoli.identifier("action_on_entity_collision"),
            new SerializableData()
                .add("bientity_action", ApoliDataTypes.BIENTITY_ACTION)
                .add("bientity_condition", ApoliDataTypes.BIENTITY_CONDITION, null)
                .add("cooldown", SerializableDataTypes.INT, 1)
                .add("hud_render", ApoliDataTypes.HUD_RENDER, HudRender.DONT_RENDER)
            ,
            data -> (type, entity) -> new ActionOnEntityCollisionPowerType(
                type,
                entity,
                data.getInt("cooldown"),
                data.get("hud_render"),
                data.get("bientity_action"),
                data.get("bientity_condition")
            )
        ).allowCondition();
    }
}
