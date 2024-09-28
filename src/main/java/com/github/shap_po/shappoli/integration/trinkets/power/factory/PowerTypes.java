package com.github.shap_po.shappoli.integration.trinkets.power.factory;

import com.github.shap_po.shappoli.integration.trinkets.power.type.*;
import io.github.apace100.apoli.power.factory.PowerTypeFactory;
import io.github.apace100.apoli.power.type.PowerType;
import io.github.apace100.apoli.registry.ApoliRegistries;
import net.minecraft.registry.Registry;

import java.util.function.Supplier;

public class PowerTypes {
    public static void register() {
        register(ActionOnTrinketChangePowerType::getFactory);
        register(ConditionedModifyTrinketSlotPowerType::getFactory);
        register(ModifyTrinketSlotPowerType::getFactory);
        register(PreventTrinketEquipPowerType::getFactory);
        register(PreventTrinketUnequipPowerType::getFactory);
    }

    @SuppressWarnings("unchecked")
    public static <T extends PowerType> PowerTypeFactory<T> register(PowerTypeFactory<?> powerTypeFactory) {
        return (PowerTypeFactory<T>) Registry.register(ApoliRegistries.POWER_FACTORY, powerTypeFactory.getSerializerId(), powerTypeFactory);
    }

    public static <T extends PowerType> PowerTypeFactory<T> register(Supplier<PowerTypeFactory<?>> powerTypeFactory) {
        return register(powerTypeFactory.get());
    }
}
