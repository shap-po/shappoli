package com.github.shap_po.shappoli.condition.factory;

import com.github.shap_po.shappoli.power.type.ReceiveConditionPowerType;
import com.github.shap_po.shappoli.condition.type.meta.SendConditionConditionType;
import io.github.apace100.apoli.condition.factory.ConditionTypeFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import net.minecraft.entity.Entity;
import net.minecraft.registry.Registry;

public class EntityConditions {
    public static void register() {
        register(SendConditionConditionType.getFactory(
            entity -> entity,
            ReceiveConditionPowerType::receiveEntity,
            io.github.apace100.apoli.condition.factory.EntityConditions.ALIASES
        ));
    }

    private static void register(ConditionTypeFactory<Entity> ConditionTypeFactory) {
        Registry.register(ApoliRegistries.ENTITY_CONDITION, ConditionTypeFactory.getSerializerId(), ConditionTypeFactory);
    }

}
