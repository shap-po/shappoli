package com.github.shap_po.shappoli.registry;

import com.github.shap_po.shappoli.power.factory.PowerFactories;
import com.github.shap_po.shappoli.power.factory.action.EntityActions;
import com.github.shap_po.shappoli.power.factory.condition.EntityConditions;
import com.github.shap_po.shappoli.power.factory.condition.ItemConditions;

public class ModPowers {
    public static void register() {
        PowerFactories.register();
        EntityConditions.register();
        ItemConditions.register();
        EntityActions.register();
    }
}
