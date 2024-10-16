package com.github.shap_po.shappoli.integration.walkers.action.factory;

import com.github.shap_po.shappoli.integration.walkers.action.type.entity.ChangeShapeAbilityCooldownActionType;
import com.github.shap_po.shappoli.integration.walkers.action.type.entity.ShapeActionActionType;
import com.github.shap_po.shappoli.integration.walkers.action.type.entity.SwitchShapeActionType;
import com.github.shap_po.shappoli.integration.walkers.action.type.entity.UseShapeAbilityActionType;
import io.github.apace100.apoli.action.factory.ActionTypeFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import net.minecraft.entity.Entity;
import net.minecraft.registry.Registry;

public class EntityActionTypes {
    public static void register() {
        register(ChangeShapeAbilityCooldownActionType.getFactory());
        register(ShapeActionActionType.getFactory());
        register(SwitchShapeActionType.getFactory());
        register(UseShapeAbilityActionType.getFactory());
    }

    private static void register(ActionTypeFactory<Entity> ActionTypeFactory) {
        Registry.register(ApoliRegistries.ENTITY_ACTION, ActionTypeFactory.getSerializerId(), ActionTypeFactory);
    }
}
