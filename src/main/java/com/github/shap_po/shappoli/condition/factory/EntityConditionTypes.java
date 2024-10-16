package com.github.shap_po.shappoli.condition.factory;

import com.github.shap_po.shappoli.condition.type.meta.SendConditionConditionType;
import com.github.shap_po.shappoli.power.type.ReceiveConditionPowerType;
import io.github.apace100.apoli.condition.factory.ConditionTypeFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import net.minecraft.entity.Entity;
import net.minecraft.registry.Registry;

public class EntityConditionTypes {
    public static void register() {
        register(SendConditionConditionType.getFactory(
            entity -> entity,
            ReceiveConditionPowerType::receiveEntity,
            io.github.apace100.apoli.condition.type.EntityConditionTypes.ALIASES
        ));
    }

    private static void register(ConditionTypeFactory<Entity> ConditionTypeFactory) {
        Registry.register(ApoliRegistries.ENTITY_CONDITION, ConditionTypeFactory.getSerializerId(), ConditionTypeFactory);
    }

}
