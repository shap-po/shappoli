package com.github.shap_po.shappoli.registry;

import com.github.shap_po.shappoli.power.ActionOnTrinketUpdate;
import com.github.shap_po.shappoli.power.EquippedTrinketCondition;
import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.apoli.power.factory.PowerFactorySupplier;
import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.Entity;
import net.minecraft.registry.Registry;

public class ModPowers {
    public static void register() {
        if (FabricLoader.getInstance().isModLoaded("trinkets")) {
            registerCondition(EquippedTrinketCondition.getFactory());
            registerPower(ActionOnTrinketUpdate::createFactory);
        }
    }

    private static void registerCondition(ConditionFactory<Entity> conditionFactory) {
        Registry.register(ApoliRegistries.ENTITY_CONDITION, conditionFactory.getSerializerId(), conditionFactory);
    }

    private static void registerPower(PowerFactory<?> powerFactory) {
        Registry.register(ApoliRegistries.POWER_FACTORY, powerFactory.getSerializerId(), powerFactory);
    }

    private static void registerPower(PowerFactorySupplier<?> factorySupplier) {
        registerPower(factorySupplier.createFactory());
    }
}
