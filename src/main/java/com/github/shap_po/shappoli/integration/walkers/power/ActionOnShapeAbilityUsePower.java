package com.github.shap_po.shappoli.integration.walkers.power;

import com.github.shap_po.shappoli.Shappoli;
import com.github.shap_po.shappoli.integration.walkers.util.WalkersUtil;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.factory.PowerFactories;
import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.calio.data.SerializableData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Pair;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class ActionOnShapeAbilityUsePower extends Power {
    private final Consumer<Pair<Entity, Entity>> bientityAction;
    private final Predicate<Pair<Entity, Entity>> bientityCondition;

    public ActionOnShapeAbilityUsePower(
        PowerType<?> type,
        LivingEntity entity,
        Consumer<Pair<Entity, Entity>> bientityAction,
        Predicate<Pair<Entity, Entity>> bientityCondition
    ) {
        super(type, entity);
        this.bientityAction = bientityAction;
        this.bientityCondition = bientityCondition;
    }

    public boolean doesApply() {
        if (!(entity instanceof PlayerEntity player)) {
            return false;
        }
        return bientityCondition == null || bientityCondition.test(new Pair<>(entity, WalkersUtil.getShape(player)));
    }

    public void apply() {
        if (!(entity instanceof PlayerEntity player)) {
            return;
        }
        bientityAction.accept(new Pair<>(entity, WalkersUtil.getShape(player)));
    }

    public static PowerFactory createFactory() {
        return new PowerFactory<>(
            Shappoli.identifier("action_on_shape_ability_use"),
            new SerializableData()
                .add("bientity_action", ApoliDataTypes.BIENTITY_ACTION)
                .add("bientity_condition", ApoliDataTypes.BIENTITY_CONDITION, null)
            ,
            data -> (type1, player) -> new ActionOnShapeAbilityUsePower(
                type1,
                player,
                data.get("bientity_action"),
                data.get("bientity_condition")
            )
        ).allowCondition();
    }
}
