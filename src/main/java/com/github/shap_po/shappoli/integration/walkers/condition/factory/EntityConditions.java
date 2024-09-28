package com.github.shap_po.shappoli.integration.walkers.condition.factory;

import com.github.shap_po.shappoli.integration.walkers.condition.type.entity.CanUseShapeAbilityConditionType;
import com.github.shap_po.shappoli.integration.walkers.condition.type.entity.HasShapeAbilityConditionType;
import com.github.shap_po.shappoli.integration.walkers.condition.type.entity.ShapeAbilityCooldownConditionType;
import com.github.shap_po.shappoli.integration.walkers.condition.type.entity.ShapeConditionType;
import io.github.apace100.apoli.condition.factory.ConditionTypeFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import net.minecraft.entity.Entity;
import net.minecraft.registry.Registry;

public class EntityConditions {
    public static void register() {
        register(CanUseShapeAbilityConditionType.getFactory());
        register(HasShapeAbilityConditionType.getFactory());
        register(ShapeAbilityCooldownConditionType.getFactory());
        register(ShapeConditionType.getFactory());
    }

    private static void register(ConditionTypeFactory<Entity> ConditionTypeFactory) {
        Registry.register(ApoliRegistries.ENTITY_CONDITION, ConditionTypeFactory.getSerializerId(), ConditionTypeFactory);
    }
}
