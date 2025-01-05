package com.github.shap_po.shappoli.power.type;

import com.github.shap_po.shappoli.util.MiscUtil;
import io.github.apace100.apoli.condition.EntityCondition;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.data.TypedDataObjectFactory;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerConfiguration;
import io.github.apace100.apoli.power.PowerReference;
import io.github.apace100.apoli.power.type.PowerType;
import io.github.apace100.calio.data.SerializableData;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class SuppressPowerPowerType extends PowerType {
    public static final TypedDataObjectFactory<SuppressPowerPowerType> DATA_FACTORY = PowerType.createConditionedDataFactory(
        new SerializableData()
            .add("power", ApoliDataTypes.POWER_REFERENCE, null)
            .add("powers", ApoliDataTypes.POWER_REFERENCE.list(), null)
            .validate(data -> MiscUtil.checkAtLeastOneFieldExists(data, "power", "powers")),
        (data, condition) -> new SuppressPowerPowerType(
            MiscUtil.listFromData(data, "power", "powers"),
            condition
        ),
        (powerType, serializableData) -> serializableData.instance()
            .set("powers", powerType.powerRefs)
    );

    List<PowerReference> powerRefs;

    public SuppressPowerPowerType(List<PowerReference> powerRefs, Optional<EntityCondition> condition) {
        super(condition);
        this.powerRefs = powerRefs;
    }

    public boolean doesApply(PowerType power) {
        return doesApply(power.getPower());
    }

    public boolean doesApply(Power power) {
        return powerRefs.stream().anyMatch(ref -> ref.equals(power.asReference()));
    }

    @Override
    public @NotNull PowerConfiguration<?> getConfig() {
        return ShappoliPowerTypes.SUPPRESS_POWER;
    }
}
