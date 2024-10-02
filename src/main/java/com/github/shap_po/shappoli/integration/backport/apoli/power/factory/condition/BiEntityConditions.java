package com.github.shap_po.shappoli.integration.backport.apoli.power.factory.condition;

import com.github.shap_po.shappoli.integration.backport.apoli.power.factory.condition.bientity.EqualCondition;
import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import net.minecraft.entity.Entity;
import net.minecraft.registry.Registry;
import net.minecraft.util.Pair;

public class BiEntityConditions {
    public static void register() {
        register(EqualCondition.getFactory());
    }

    private static void register(ConditionFactory<Pair<Entity, Entity>> conditionFactory) {
        // Register only if original condition is not already registered
        if (!ApoliRegistries.BIENTITY_CONDITION.containsId(conditionFactory.getSerializerId())) {
            Registry.register(ApoliRegistries.BIENTITY_CONDITION, conditionFactory.getSerializerId(), conditionFactory);
        }
    }
}
