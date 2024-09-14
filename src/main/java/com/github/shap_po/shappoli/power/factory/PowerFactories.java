package com.github.shap_po.shappoli.power.factory;

import com.github.shap_po.shappoli.power.ModifyVillagerReputationPower;
import com.github.shap_po.shappoli.power.ActionOnEventReceivePower;
import com.github.shap_po.shappoli.power.ReceiveConditionPower;
import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.apoli.power.factory.PowerFactorySupplier;
import io.github.apace100.apoli.registry.ApoliRegistries;
import net.minecraft.registry.Registry;

public class PowerFactories {
    public static void register() {
        register(ModifyVillagerReputationPower::createFactory);
        register(ActionOnEventReceivePower::createFactory);
        register(ReceiveConditionPower::createFactory);
    }

    private static void register(PowerFactory<?> powerFactory) {
        Registry.register(ApoliRegistries.POWER_FACTORY, powerFactory.getSerializerId(), powerFactory);
    }

    private static void register(PowerFactorySupplier<?> factorySupplier) {
        register(factorySupplier.createFactory());
    }
}
