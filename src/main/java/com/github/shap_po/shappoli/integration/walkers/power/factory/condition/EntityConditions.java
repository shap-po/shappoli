package com.github.shap_po.shappoli.integration.walkers.power.factory.condition;

import com.github.shap_po.shappoli.integration.walkers.power.factory.condition.entity.CanUseShapeAbilityCondition;
import com.github.shap_po.shappoli.integration.walkers.power.factory.condition.entity.HasShapeAbilityCondition;
import com.github.shap_po.shappoli.integration.walkers.power.factory.condition.entity.ShapeAbilityCooldownCondition;
import com.github.shap_po.shappoli.integration.walkers.power.factory.condition.entity.ShapeCondition;
import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import net.minecraft.entity.Entity;
import net.minecraft.registry.Registry;

public class EntityConditions {
    public static void register() {
        register(CanUseShapeAbilityCondition.getFactory());
        register(HasShapeAbilityCondition.getFactory());
        register(ShapeAbilityCooldownCondition.getFactory());
        register(ShapeCondition.getFactory());
    }

    private static void register(ConditionFactory<Entity> conditionFactory) {
        Registry.register(ApoliRegistries.ENTITY_CONDITION, conditionFactory.getSerializerId(), conditionFactory);
    }
}
