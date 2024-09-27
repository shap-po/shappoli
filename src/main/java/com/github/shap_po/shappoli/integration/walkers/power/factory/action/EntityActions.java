package com.github.shap_po.shappoli.integration.walkers.power.factory.action;

import com.github.shap_po.shappoli.integration.walkers.power.factory.action.entity.ChangeShapeAbilityCooldownAction;
import com.github.shap_po.shappoli.integration.walkers.power.factory.action.entity.ShapeActionAction;
import com.github.shap_po.shappoli.integration.walkers.power.factory.action.entity.SwitchShapeAction;
import io.github.apace100.apoli.action.factory.ActionTypeFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import net.minecraft.entity.Entity;
import net.minecraft.registry.Registry;

public class EntityActions {
    public static void register() {
        register(ChangeShapeAbilityCooldownAction.getFactory());
        register(ShapeActionAction.getFactory());
        register(SwitchShapeAction.getFactory());
    }

    private static void register(ActionTypeFactory<Entity> ActionTypeFactory) {
        Registry.register(ApoliRegistries.ENTITY_ACTION, ActionTypeFactory.getSerializerId(), ActionTypeFactory);
    }
}
