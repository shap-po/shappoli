package com.github.shap_po.shappoli.integration.trinkets;

import com.github.shap_po.shappoli.integration.trinkets.power.factory.PowerFactories;
import com.github.shap_po.shappoli.integration.trinkets.power.factory.action.EntityActions;
import com.github.shap_po.shappoli.integration.trinkets.power.factory.condition.EntityConditions;

public class TrinketsIntegration {
    public static void register() {
        PowerFactories.register();
        EntityActions.register();
        EntityConditions.register();
    }
}
