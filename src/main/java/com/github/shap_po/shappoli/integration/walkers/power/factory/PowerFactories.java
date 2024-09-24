package com.github.shap_po.shappoli.integration.walkers.power.factory;

import com.github.shap_po.shappoli.integration.walkers.power.ActionOnShapeChangePower;
import com.github.shap_po.shappoli.integration.walkers.power.PreventShapeChangePower;
import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.apoli.power.factory.PowerFactorySupplier;
import io.github.apace100.apoli.registry.ApoliRegistries;
import net.minecraft.registry.Registry;

public class PowerFactories {
    public static void register() {
        register(ActionOnShapeChangePower::createFactory);
        register(PreventShapeChangePower::createFactory);
    }

    private static void register(PowerFactory<?> powerFactory) {
        Registry.register(ApoliRegistries.POWER_FACTORY, powerFactory.getSerializerId(), powerFactory);
    }

    private static void register(PowerFactorySupplier<?> factorySupplier) {
        register(factorySupplier.createFactory());
    }
}
