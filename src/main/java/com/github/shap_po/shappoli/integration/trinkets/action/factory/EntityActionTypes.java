package com.github.shap_po.shappoli.integration.trinkets.action.factory;

import com.github.shap_po.shappoli.integration.trinkets.action.type.entity.ModifyTrinketActionType;
import io.github.apace100.apoli.action.factory.ActionTypeFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import net.minecraft.entity.Entity;
import net.minecraft.registry.Registry;

public class EntityActionTypes {
    public static void register() {
        register(ModifyTrinketActionType.getFactory());
    }

    private static void register(ActionTypeFactory<Entity> ActionTypeFactory) {
        Registry.register(ApoliRegistries.ENTITY_ACTION, ActionTypeFactory.getSerializerId(), ActionTypeFactory);
    }
}
