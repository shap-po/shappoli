package com.github.shap_po.shappoli.integration.origins;

import com.github.shap_po.shappoli.integration.origins.action.type.ShappoliOriginsBiEntityActionTypes;
import com.github.shap_po.shappoli.integration.origins.action.type.ShappoliOriginsEntityActionTypes;

public class OriginsIntegration {
    public static void register() {
        ShappoliOriginsBiEntityActionTypes.register();
        ShappoliOriginsEntityActionTypes.register();
    }
}
