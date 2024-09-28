package com.github.shap_po.shappoli.integration.origins;

import com.github.shap_po.shappoli.integration.origins.action.factory.BiEntityActions;
import com.github.shap_po.shappoli.integration.origins.action.factory.EntityActions;

public class OriginsIntegration {
    public static void register() {
        BiEntityActions.register();
        EntityActions.register();
    }
}
