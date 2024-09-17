package com.github.shap_po.shappoli.power;

import com.github.shap_po.shappoli.Shappoli;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.CooldownPower;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.PreventEntityCollisionPower;
import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.apoli.util.HudRender;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Pair;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class ActionOnEntityCollisionPower extends CooldownPower {
    private final Consumer<Pair<Entity, Entity>> bientityAction;
    private final Predicate<Pair<Entity, Entity>> bientityCondition;

    public ActionOnEntityCollisionPower(
        PowerType<?> type, LivingEntity entity,
        int cooldownDuration, HudRender hudRender,
        Consumer<Pair<Entity, Entity>> bientityAction,
        Predicate<Pair<Entity, Entity>> bientityCondition
    ) {
        super(type, entity, cooldownDuration, hudRender);
        this.bientityAction = bientityAction;
        this.bientityCondition = bientityCondition;
    }

    public void apply() {
        List<Entity> collidingEntities = getCollidingEntities();
        for (Entity other : collidingEntities) {
            if (this.canUse() &&
                !PreventEntityCollisionPower.doesApply(entity, other) &&
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

    public static PowerFactory createFactory() {
        return new PowerFactory<>(Shappoli.identifier("action_on_entity_collision"),
            new SerializableData()
                .add("bientity_action", ApoliDataTypes.BIENTITY_ACTION)
                .add("bientity_condition", ApoliDataTypes.BIENTITY_CONDITION, null)
                .add("cooldown", SerializableDataTypes.INT, 1)
                .add("hud_render", ApoliDataTypes.HUD_RENDER, HudRender.DONT_RENDER)
            ,
            data -> (type, entity) -> new ActionOnEntityCollisionPower(
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
