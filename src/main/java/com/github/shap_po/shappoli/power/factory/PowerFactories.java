package com.github.shap_po.shappoli.power.factory;

import com.github.shap_po.shappoli.power.*;
import io.github.apace100.apoli.power.factory.PowerTypeFactory;
import io.github.apace100.apoli.power.type.PowerType;
import io.github.apace100.apoli.registry.ApoliRegistries;
import net.minecraft.registry.Registry;

import java.util.function.Supplier;

public class PowerFactories {
    public static void register() {
        register(ActionOnEntityCollisionPower::createFactory);
        register(ModifyVillagerReputationPower::createFactory);
        register(ReceiveActionPower::createFactory);
        register(ReceiveConditionPower::createFactory);
        register(SuppressPowerPower::createFactory);
    }

    @SuppressWarnings("unchecked")
    public static <T extends PowerType> PowerTypeFactory<T> register(PowerTypeFactory<?> powerTypeFactory) {
        return (PowerTypeFactory<T>) Registry.register(ApoliRegistries.POWER_FACTORY, powerTypeFactory.getSerializerId(), powerTypeFactory);
    }

    public static <T extends PowerType> PowerTypeFactory<T> register(Supplier<PowerTypeFactory<?>> powerTypeFactory) {
        return register(powerTypeFactory.get());
    }
}
