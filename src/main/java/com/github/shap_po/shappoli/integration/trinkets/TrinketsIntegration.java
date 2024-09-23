package com.github.shap_po.shappoli.integration.trinkets;

import com.github.shap_po.shappoli.integration.trinkets.command.TrinketsCommand;
import com.github.shap_po.shappoli.integration.trinkets.power.factory.PowerTypes;
import com.github.shap_po.shappoli.integration.trinkets.action.factory.EntityActions;
import com.github.shap_po.shappoli.integration.trinkets.condition.factory.EntityConditions;
import com.github.shap_po.shappoli.integration.trinkets.condition.factory.ItemConditions;

public class TrinketsIntegration {
    public static void register() {
        PowerTypes.register();
        EntityActions.register();
        EntityConditions.register();
        ItemConditions.register();

        TrinketsCommand.register();
    }
}
