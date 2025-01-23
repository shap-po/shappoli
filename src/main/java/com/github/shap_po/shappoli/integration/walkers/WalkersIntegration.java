package com.github.shap_po.shappoli.integration.walkers;

import com.github.shap_po.shappoli.integration.walkers.action.type.ShappoliWalkersBiEntityActionTypes;
import com.github.shap_po.shappoli.integration.walkers.action.type.ShappoliWalkersEntityActionTypes;
import com.github.shap_po.shappoli.integration.walkers.condition.type.ShappoliWalkersEntityConditionTypes;
import com.github.shap_po.shappoli.integration.walkers.events.ShapeEventHandler;
import com.github.shap_po.shappoli.integration.walkers.power.type.ShappoliWalkersPowerTypes;
import com.github.shap_po.shappoli.integration.walkers.registry.ShappoliWalkersShapeAbilityClassRegistry;

public class WalkersIntegration {
    public static void register() {
        ShappoliWalkersPowerTypes.register();
        ShappoliWalkersBiEntityActionTypes.register();
        ShappoliWalkersEntityActionTypes.register();
        ShappoliWalkersEntityConditionTypes.register();

        ShapeEventHandler.register();
        ShappoliWalkersShapeAbilityClassRegistry.register();
    }
}
