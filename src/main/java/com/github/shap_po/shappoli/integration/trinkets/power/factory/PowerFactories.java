package com.github.shap_po.shappoli.integration.trinkets.power.factory;

import com.github.shap_po.shappoli.integration.trinkets.power.*;
import io.github.apace100.apoli.power.factory.PowerTypeFactory;
import io.github.apace100.apoli.power.type.PowerType;
import io.github.apace100.apoli.registry.ApoliRegistries;
import net.minecraft.registry.Registry;

import java.util.function.Supplier;

public class PowerFactories {
    public static void register() {
        register(ActionOnTrinketChangePower::createFactory);
        register(ConditionedModifyTrinketSlotPower::createFactory);
        register(ModifyTrinketSlotPower::createFactory);
        register(PreventTrinketEquipPower::createFactory);
        register(PreventTrinketUnequipPower::createFactory);
    }

    @SuppressWarnings("unchecked")
    public static <T extends PowerType> PowerTypeFactory<T> register(PowerTypeFactory<?> powerTypeFactory) {
        return (PowerTypeFactory<T>) Registry.register(ApoliRegistries.POWER_FACTORY, powerTypeFactory.getSerializerId(), powerTypeFactory);
    }

    public static <T extends PowerType> PowerTypeFactory<T> register(Supplier<PowerTypeFactory<?>> powerTypeFactory) {
        return register(powerTypeFactory.get());
    }
}
