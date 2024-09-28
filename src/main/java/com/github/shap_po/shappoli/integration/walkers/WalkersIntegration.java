package com.github.shap_po.shappoli.integration.walkers;

import com.github.shap_po.shappoli.integration.walkers.power.factory.PowerTypes;
import com.github.shap_po.shappoli.integration.walkers.action.factory.BiEntityActions;
import com.github.shap_po.shappoli.integration.walkers.action.factory.EntityActions;
import com.github.shap_po.shappoli.integration.walkers.condition.factory.EntityConditions;

public class WalkersIntegration {
    public static void register() {
        PowerTypes.register();
        BiEntityActions.register();
        EntityActions.register();
        EntityConditions.register();
    }
}
