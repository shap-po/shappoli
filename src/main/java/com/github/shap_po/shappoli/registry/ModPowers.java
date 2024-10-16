package com.github.shap_po.shappoli.registry;

import com.github.shap_po.shappoli.power.factory.PowerTypes;
import com.github.shap_po.shappoli.action.factory.BiEntityActionTypes;
import com.github.shap_po.shappoli.action.factory.BlockActionTypes;
import com.github.shap_po.shappoli.action.factory.EntityActionTypes;
import com.github.shap_po.shappoli.action.factory.ItemActionTypes;
import com.github.shap_po.shappoli.condition.factory.BiEntityConditionTypes;
import com.github.shap_po.shappoli.condition.factory.EntityConditionTypes;
import com.github.shap_po.shappoli.condition.factory.ItemConditionTypes;

public class ModPowers {
    public static void register() {
        PowerTypes.register();

        BiEntityConditionTypes.register();
        EntityConditionTypes.register();
        ItemConditionTypes.register();

        BiEntityActionTypes.register();
        BlockActionTypes.register();
        EntityActionTypes.register();
        ItemActionTypes.register();
    }
}
