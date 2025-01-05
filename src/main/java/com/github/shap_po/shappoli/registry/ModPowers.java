package com.github.shap_po.shappoli.registry;

import com.github.shap_po.shappoli.action.type.ShappoliBiEntityActionTypes;
import com.github.shap_po.shappoli.action.type.ShappoliEntityActionTypes;
import com.github.shap_po.shappoli.action.type.ShappoliItemActionTypes;
import com.github.shap_po.shappoli.condition.type.ShappoliBiEntityConditionTypes;
import com.github.shap_po.shappoli.condition.type.ShappoliEntityConditionTypes;
import com.github.shap_po.shappoli.condition.type.ShappoliItemConditionTypes;
import com.github.shap_po.shappoli.power.type.ShappoliPowerTypes;

public class ModPowers {
    public static void register() {
        ShappoliPowerTypes.register();

        ShappoliBiEntityConditionTypes.register();
        ShappoliEntityConditionTypes.register();
        ShappoliItemConditionTypes.register();

        ShappoliBiEntityActionTypes.register();
        ShappoliEntityActionTypes.register();
        ShappoliItemActionTypes.register();
    }
}
