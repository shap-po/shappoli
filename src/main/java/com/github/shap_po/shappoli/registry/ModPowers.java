package com.github.shap_po.shappoli.registry;

import com.github.shap_po.shappoli.power.factory.PowerFactories;
import com.github.shap_po.shappoli.power.factory.action.BiEntityActions;
import com.github.shap_po.shappoli.power.factory.action.BlockActions;
import com.github.shap_po.shappoli.power.factory.action.EntityActions;
import com.github.shap_po.shappoli.power.factory.action.ItemActions;
import com.github.shap_po.shappoli.power.factory.condition.ItemConditions;

public class ModPowers {
    public static void register() {
        PowerFactories.register();

        ItemConditions.register();

        BiEntityActions.register();
        BlockActions.register();
        EntityActions.register();
        ItemActions.register();
    }
}
