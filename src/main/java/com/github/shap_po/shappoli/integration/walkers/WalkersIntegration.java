package com.github.shap_po.shappoli.integration.walkers;

import com.github.shap_po.shappoli.integration.walkers.power.factory.PowerTypes;
import com.github.shap_po.shappoli.integration.walkers.action.factory.BiEntityActionTypes;
import com.github.shap_po.shappoli.integration.walkers.action.factory.EntityActionTypes;
import com.github.shap_po.shappoli.integration.walkers.condition.factory.EntityConditionTypes;

public class WalkersIntegration {
    public static void register() {
        PowerTypes.register();
        BiEntityActionTypes.register();
        EntityActionTypes.register();
        EntityConditionTypes.register();
    }
}
