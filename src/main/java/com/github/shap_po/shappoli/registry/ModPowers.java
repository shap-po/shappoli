package com.github.shap_po.shappoli.registry;

import com.github.shap_po.shappoli.power.factory.PowerTypes;
import com.github.shap_po.shappoli.action.factory.BiEntityActions;
import com.github.shap_po.shappoli.action.factory.BlockActions;
import com.github.shap_po.shappoli.action.factory.EntityActions;
import com.github.shap_po.shappoli.action.factory.ItemActions;
import com.github.shap_po.shappoli.condition.factory.BiEntityConditions;
import com.github.shap_po.shappoli.condition.factory.EntityConditions;
import com.github.shap_po.shappoli.condition.factory.ItemConditions;

public class ModPowers {
    public static void register() {
        PowerTypes.register();

        BiEntityConditions.register();
        EntityConditions.register();
        ItemConditions.register();

        BiEntityActions.register();
        BlockActions.register();
        EntityActions.register();
        ItemActions.register();
    }
}
