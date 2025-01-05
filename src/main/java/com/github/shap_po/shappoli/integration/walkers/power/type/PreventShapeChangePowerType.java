package com.github.shap_po.shappoli.integration.walkers.power.type;

import io.github.apace100.apoli.condition.BiEntityCondition;
import io.github.apace100.apoli.condition.EntityCondition;
import io.github.apace100.apoli.data.TypedDataObjectFactory;
import io.github.apace100.apoli.power.PowerConfiguration;
import io.github.apace100.apoli.power.type.PowerType;
import io.github.apace100.calio.data.SerializableData;
import net.minecraft.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class PreventShapeChangePowerType extends PowerType {
    public static final TypedDataObjectFactory<PreventShapeChangePowerType> DATA_FACTORY = PowerType.createConditionedDataFactory(
        new SerializableData()
            .add("bientity_condition", BiEntityCondition.DATA_TYPE.optional(), Optional.empty()),
        (data, condition) -> new PreventShapeChangePowerType(
            data.get("bientity_condition"),
            condition
        ),
        (powerType, serializableData) -> serializableData.instance()
            .set("bientity_condition", powerType.biEntityCondition)
    );

    private final Optional<BiEntityCondition> biEntityCondition;

    public PreventShapeChangePowerType(Optional<BiEntityCondition> biEntityCondition, Optional<EntityCondition> condition) {
        super(condition);
        this.biEntityCondition = biEntityCondition;
    }

    public boolean doesApply(LivingEntity shape) {
        return biEntityCondition.map(biEntityCondition -> biEntityCondition.test(getHolder(), shape)).orElse(true);
    }

    @Override
    public @NotNull PowerConfiguration<?> getConfig() {
        return ShappoliWalkersPowerTypes.PREVENT_SHAPE_CHANGE;
    }
}
