package com.github.shap_po.shappoli.integration.walkers.power.factory.action;

import com.github.shap_po.shappoli.integration.walkers.power.factory.action.entity.ChangeShapeAbilityCooldownAction;
import com.github.shap_po.shappoli.integration.walkers.power.factory.action.entity.SwitchShapeAction;
import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import net.minecraft.entity.Entity;
import net.minecraft.registry.Registry;

public class EntityActions {
    public static void register() {
        register(ChangeShapeAbilityCooldownAction.getFactory());
        register(SwitchShapeAction.getFactory());
    }

    private static void register(ActionFactory<Entity> actionFactory) {
        Registry.register(ApoliRegistries.ENTITY_ACTION, actionFactory.getSerializerId(), actionFactory);
    }
}
