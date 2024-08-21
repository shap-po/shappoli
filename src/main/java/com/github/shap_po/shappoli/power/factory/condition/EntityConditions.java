package com.github.shap_po.shappoli.power.factory.condition;

import com.github.shap_po.shappoli.power.factory.condition.entity.EquippedTrinketCondition;
import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.Entity;
import net.minecraft.registry.Registry;

public class EntityConditions {
    public static void register() {
        if (FabricLoader.getInstance().isModLoaded("trinkets")) {
            register(EquippedTrinketCondition.getFactory());
        }
    }

    private static void register(ConditionFactory<Entity> conditionFactory) {
        Registry.register(ApoliRegistries.ENTITY_CONDITION, conditionFactory.getSerializerId(), conditionFactory);
    }
}
