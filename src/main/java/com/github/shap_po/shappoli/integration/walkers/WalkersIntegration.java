package com.github.shap_po.shappoli.integration.walkers;

import com.github.shap_po.shappoli.integration.walkers.power.factory.PowerFactories;
import com.github.shap_po.shappoli.integration.walkers.power.factory.action.BiEntityActions;
import com.github.shap_po.shappoli.integration.walkers.power.factory.action.EntityActions;

public class WalkersIntegration {
    public static void register() {
        PowerFactories.register();
        BiEntityActions.register();
        EntityActions.register();
    }
}
