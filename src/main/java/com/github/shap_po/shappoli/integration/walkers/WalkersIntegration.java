package com.github.shap_po.shappoli.integration.walkers;

import com.github.shap_po.shappoli.integration.walkers.events.ShapeEventHandler;
import com.github.shap_po.shappoli.integration.walkers.power.factory.PowerFactories;
import com.github.shap_po.shappoli.integration.walkers.power.factory.ability.ShapeAbilities;
import com.github.shap_po.shappoli.integration.walkers.power.factory.action.BiEntityActions;
import com.github.shap_po.shappoli.integration.walkers.power.factory.action.EntityActions;
import com.github.shap_po.shappoli.integration.walkers.power.factory.condition.EntityConditions;
import com.github.shap_po.shappoli.integration.walkers.registry.ShappoliWalkersShapeAbilityTypeRegistry;

public class WalkersIntegration {
    public static void register() {
        PowerFactories.register();
        BiEntityActions.register();
        EntityActions.register();
        EntityConditions.register();

        ShapeAbilities.register();
        ShapeEventHandler.register();
        ShappoliWalkersShapeAbilityTypeRegistry.register();
    }
}
