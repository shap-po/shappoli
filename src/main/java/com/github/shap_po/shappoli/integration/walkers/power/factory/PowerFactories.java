package com.github.shap_po.shappoli.integration.walkers.power.factory;

import com.github.shap_po.shappoli.integration.walkers.power.ActionOnShapeAbilityUsePower;
import com.github.shap_po.shappoli.integration.walkers.power.ActionOnShapeChangePower;
import com.github.shap_po.shappoli.integration.walkers.power.PreventShapeAbilityUsePower;
import com.github.shap_po.shappoli.integration.walkers.power.PreventShapeChangePower;
import io.github.apace100.apoli.power.factory.PowerTypeFactory;
import io.github.apace100.apoli.power.type.PowerType;
import io.github.apace100.apoli.registry.ApoliRegistries;
import net.minecraft.registry.Registry;

import java.util.function.Supplier;

public class PowerFactories {
    public static void register() {
        register(ActionOnShapeAbilityUsePower::createFactory);
        register(ActionOnShapeChangePower::createFactory);
        register(PreventShapeAbilityUsePower::createFactory);
        register(PreventShapeChangePower::createFactory);
    }

    @SuppressWarnings("unchecked")
    public static <T extends PowerType> PowerTypeFactory<T> register(PowerTypeFactory<?> powerTypeFactory) {
        return (PowerTypeFactory<T>) Registry.register(ApoliRegistries.POWER_FACTORY, powerTypeFactory.getSerializerId(), powerTypeFactory);
    }

    public static <T extends PowerType> PowerTypeFactory<T> register(Supplier<PowerTypeFactory<?>> powerTypeFactory) {
        return register(powerTypeFactory.get());
    }
}
