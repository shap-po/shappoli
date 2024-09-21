package com.github.shap_po.shappoli.power;

import com.github.shap_po.shappoli.Shappoli;
import com.github.shap_po.shappoli.data.ShappoliDataTypes;
import com.github.shap_po.shappoli.util.MiscUtil;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.PowerTypeReference;
import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.calio.data.SerializableData;
import net.minecraft.entity.LivingEntity;

import java.util.List;

public class SuppressPowerPower extends Power {
    List<PowerTypeReference<?>> powerRefs;

    public SuppressPowerPower(
        PowerType<?> type, LivingEntity entity,
        List<PowerTypeReference<?>> powerRefs
    ) {
        super(type, entity);
        this.powerRefs = powerRefs;
    }

    public boolean doesApply(Power power) {
        return doesApply(power.getType());
    }

    public boolean doesApply(PowerType power) {
        return powerRefs.stream().anyMatch(ref -> ref.equals(power));
    }

    public static PowerFactory createFactory() {
        return new PowerFactory<>(Shappoli.identifier("suppress_power"),
            new SerializableData()
                .add("power", ApoliDataTypes.POWER_TYPE, null)
                .add("powers", ShappoliDataTypes.POWER_TYPES, null)
            ,
            data -> (type, entity) -> new SuppressPowerPower(
                type, entity,
                MiscUtil.listFromData(data, "power", "powers")
            )
        ).allowCondition();
    }
}
