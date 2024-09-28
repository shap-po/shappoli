package com.github.shap_po.shappoli.integration.walkers.power.type;

import com.github.shap_po.shappoli.Shappoli;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.factory.PowerTypeFactory;
import io.github.apace100.apoli.power.factory.PowerTypes;
import io.github.apace100.apoli.power.type.PowerType;
import io.github.apace100.calio.data.SerializableData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Pair;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class ActionOnShapeChangePowerType extends PowerType {
    private final Consumer<Pair<Entity, Entity>> bientityAction;
    private final @Nullable Predicate<Pair<Entity, Entity>> bientityCondition;

    public ActionOnShapeChangePowerType(
        Power type,
        LivingEntity entity,
        Consumer<Pair<Entity, Entity>> bientityAction,
        @Nullable Predicate<Pair<Entity, Entity>> bientityCondition
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

    public static PowerTypeFactory getFactory() {
        PowerTypeFactory<?> factory = new PowerTypeFactory<>(
            Shappoli.identifier("action_on_shape_change"),
            new SerializableData()
                .add("bientity_action", ApoliDataTypes.BIENTITY_ACTION)
                .add("bientity_condition", ApoliDataTypes.BIENTITY_CONDITION, null)
            ,
            data -> (type, player) -> new ActionOnShapeChangePowerType(
                type,
                player,
                data.get("bientity_action"),
                data.get("bientity_condition")
            )
        ).allowCondition();

        PowerTypes.ALIASES.addPathAlias("action_on_morph", factory.getSerializerId().getPath());
        return factory;
    }
}
