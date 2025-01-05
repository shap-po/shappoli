package com.github.shap_po.shappoli.integration.walkers.power.type;

import io.github.apace100.apoli.action.BiEntityAction;
import io.github.apace100.apoli.condition.BiEntityCondition;
import io.github.apace100.apoli.condition.EntityCondition;
import io.github.apace100.apoli.data.TypedDataObjectFactory;
import io.github.apace100.apoli.power.PowerConfiguration;
import io.github.apace100.apoli.power.type.PowerType;
import io.github.apace100.calio.data.SerializableData;
import net.minecraft.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class ActionOnShapeChangePowerType extends PowerType {
    public static final TypedDataObjectFactory<ActionOnShapeChangePowerType> DATA_FACTORY = PowerType.createConditionedDataFactory(
        new SerializableData()
            .add("bientity_action", BiEntityAction.DATA_TYPE)
            .add("bientity_condition", BiEntityCondition.DATA_TYPE.optional(), Optional.empty()),
        (data, condition) -> new ActionOnShapeChangePowerType(
            data.get("bientity_action"),
            data.get("bientity_condition"),
            condition
        ),
        (powerType, serializableData) -> serializableData.instance()
            .set("bientity_action", powerType.biEntityAction)
            .set("bientity_condition", powerType.biEntityCondition)
    );

    private final BiEntityAction biEntityAction;
    private final Optional<BiEntityCondition> biEntityCondition;

    public ActionOnShapeChangePowerType(
        BiEntityAction biEntityAction,
        Optional<BiEntityCondition> biEntityCondition,
        Optional<EntityCondition> condition
    ) {
        super(condition);
        this.biEntityAction = biEntityAction;
        this.biEntityCondition = biEntityCondition;
    }

    public boolean doesApply(LivingEntity shape) {
        return biEntityCondition.map(biEntityCondition -> biEntityCondition.test(getHolder(), shape)).orElse(true);
    }

    public void apply(LivingEntity shape) {
        biEntityAction.execute(getHolder(), shape);
    }

    @Override
    public @NotNull PowerConfiguration<?> getConfig() {
        return ShappoliWalkersPowerTypes.ACTION_ON_SHAPE_CHANGE;
    }
}
