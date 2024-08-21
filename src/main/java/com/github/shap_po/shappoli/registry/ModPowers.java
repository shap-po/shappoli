package com.github.shap_po.shappoli.registry;

import com.github.shap_po.shappoli.power.factory.PowerFactories;
import com.github.shap_po.shappoli.power.factory.condition.EntityConditions;

public class ModPowers {
    public static void register() {
        PowerFactories.register();
        EntityConditions.register();
    }
}
