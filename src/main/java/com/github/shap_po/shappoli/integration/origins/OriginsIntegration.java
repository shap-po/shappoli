package com.github.shap_po.shappoli.integration.origins;

import com.github.shap_po.shappoli.integration.origins.power.factory.action.BiEntityActions;
import com.github.shap_po.shappoli.integration.origins.power.factory.action.EntityActions;

public class OriginsIntegration {
    public static void register() {
        BiEntityActions.register();
        EntityActions.register();
    }
}
