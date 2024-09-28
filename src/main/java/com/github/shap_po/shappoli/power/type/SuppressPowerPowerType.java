package com.github.shap_po.shappoli.power.type;

import com.github.shap_po.shappoli.Shappoli;
import com.github.shap_po.shappoli.data.ShappoliDataTypes;
import com.github.shap_po.shappoli.util.MiscUtil;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerReference;
import io.github.apace100.apoli.power.factory.PowerTypeFactory;
import io.github.apace100.apoli.power.type.PowerType;
import io.github.apace100.calio.data.SerializableData;
import net.minecraft.entity.LivingEntity;

import java.util.List;

public class SuppressPowerPowerType extends PowerType {
    List<PowerReference> powerRefs;

    public SuppressPowerPowerType(
        Power type, LivingEntity entity,
        List<PowerReference> powerRefs
    ) {
        super(type, entity);
        this.powerRefs = powerRefs;
    }

    public boolean doesApply(PowerType power) {
        return doesApply(power.getPower());
    }

    public boolean doesApply(Power power) {
        return powerRefs.stream().anyMatch(ref -> ref.equals(power));
    }

    public static PowerTypeFactory getFactory() {
        return new PowerTypeFactory<>(
            Shappoli.identifier("suppress_power"),
            new SerializableData()
                .add("power", ApoliDataTypes.POWER_REFERENCE, null)
                .add("powers", ShappoliDataTypes.POWER_REFERENCES, null)
                .postProcessor(data -> MiscUtil.checkHasAtLeastOneField(data, "power", "powers"))
            ,
            data -> (type, entity) -> new SuppressPowerPowerType(
                type, entity,
                MiscUtil.listFromData(data, "power", "powers")
            )
        ).allowCondition();
    }
}
