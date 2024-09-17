package com.github.shap_po.shappoli.integration.trinkets.power.factory.condition;

import com.github.shap_po.shappoli.integration.trinkets.power.factory.condition.entity.EquippedTrinketCountCondition;
import com.github.shap_po.shappoli.integration.trinkets.power.factory.condition.entity.TrinketSlotCountCondition;
import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import net.minecraft.entity.Entity;
import net.minecraft.registry.Registry;

public class EntityConditions {
    public static void register() {
        register(EquippedTrinketCountCondition.getFactory());
        register(TrinketSlotCountCondition.getFactory());
    }

    private static void register(ConditionFactory<Entity> conditionFactory) {
        Registry.register(ApoliRegistries.ENTITY_CONDITION, conditionFactory.getSerializerId(), conditionFactory);
    }
}
