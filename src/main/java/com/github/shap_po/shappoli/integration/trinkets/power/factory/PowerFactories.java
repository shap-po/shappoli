package com.github.shap_po.shappoli.integration.trinkets.power.factory;

import com.github.shap_po.shappoli.integration.trinkets.power.ActionOnTrinketChangePower;
import com.github.shap_po.shappoli.integration.trinkets.power.PreventTrinketEquipPower;
import com.github.shap_po.shappoli.integration.trinkets.power.PreventTrinketUnequipPower;
import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.apoli.power.factory.PowerFactorySupplier;
import io.github.apace100.apoli.registry.ApoliRegistries;
import net.minecraft.registry.Registry;

public class PowerFactories {
    public static void register() {
        register(ActionOnTrinketChangePower::createFactory);
        register(PreventTrinketEquipPower::createFactory);
        register(PreventTrinketUnequipPower::createFactory);
    }

    private static void register(PowerFactory<?> powerFactory) {
        Registry.register(ApoliRegistries.POWER_FACTORY, powerFactory.getSerializerId(), powerFactory);
    }

    private static void register(PowerFactorySupplier<?> factorySupplier) {
        register(factorySupplier.createFactory());
    }
}
