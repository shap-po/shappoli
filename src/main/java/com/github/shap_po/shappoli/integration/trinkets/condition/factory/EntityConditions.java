package com.github.shap_po.shappoli.integration.trinkets.condition.factory;

import com.github.shap_po.shappoli.integration.trinkets.condition.type.entity.EquippedTrinketCountConditionType;
import com.github.shap_po.shappoli.integration.trinkets.condition.type.entity.TrinketSlotCountConditionType;
import io.github.apace100.apoli.condition.factory.ConditionTypeFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import net.minecraft.entity.Entity;
import net.minecraft.registry.Registry;

public class EntityConditions {
    public static void register() {
        register(EquippedTrinketCountConditionType.getFactory());
        register(TrinketSlotCountConditionType.getFactory());
    }

    private static void register(ConditionTypeFactory<Entity> ConditionTypeFactory) {
        Registry.register(ApoliRegistries.ENTITY_CONDITION, ConditionTypeFactory.getSerializerId(), ConditionTypeFactory);
    }
}
