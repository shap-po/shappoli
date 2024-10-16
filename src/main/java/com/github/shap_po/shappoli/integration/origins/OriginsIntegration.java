package com.github.shap_po.shappoli.integration.origins;

import com.github.shap_po.shappoli.integration.origins.action.factory.BiEntityActionTypes;
import com.github.shap_po.shappoli.integration.origins.action.factory.EntityActionTypes;

public class OriginsIntegration {
    public static void register() {
        BiEntityActionTypes.register();
        EntityActionTypes.register();
    }
}
