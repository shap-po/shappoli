package com.github.shap_po.shappoli.integration.walkers.power;

import com.github.shap_po.shappoli.Shappoli;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.calio.data.SerializableData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Pair;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class ActionOnShapeChangePower extends Power {
    private final Consumer<Pair<Entity, Entity>> bientityAction;
    private final Predicate<Pair<Entity, Entity>> bientityCondition;

    public ActionOnShapeChangePower(
        PowerType<?> type,
        LivingEntity entity,
        Consumer<Pair<Entity, Entity>> bientityAction,
        Predicate<Pair<Entity, Entity>> bientityCondition
    ) {
        super(type, entity);
        this.bientityAction = bientityAction;
        this.bientityCondition = bientityCondition;
    }

    public boolean doesApply(LivingEntity shape) {
        return bientityCondition == null || bientityCondition.test(new Pair<>(entity, shape));
    }

    public void apply(LivingEntity shape) {
        bientityAction.accept(new Pair<>(entity, shape));
    }

    public static PowerFactory createFactory() {
        return new PowerFactory<>(
            Shappoli.identifier("action_on_shape_change"),
            new SerializableData()
                .add("bientity_action", ApoliDataTypes.BIENTITY_ACTION)
                .add("bientity_condition", ApoliDataTypes.BIENTITY_CONDITION, null)
            ,
            data -> (type1, player) -> new ActionOnShapeChangePower(
                type1,
                player,
                data.get("bientity_action"),
                data.get("bientity_condition")
            )
        ).allowCondition();
    }
}
