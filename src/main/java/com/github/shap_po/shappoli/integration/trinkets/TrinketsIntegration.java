package com.github.shap_po.shappoli.integration.trinkets;

import com.github.shap_po.shappoli.integration.trinkets.command.TrinketsCommand;
import com.github.shap_po.shappoli.integration.trinkets.power.factory.PowerTypes;
import com.github.shap_po.shappoli.integration.trinkets.action.factory.EntityActionTypes;
import com.github.shap_po.shappoli.integration.trinkets.condition.factory.EntityConditionTypes;
import com.github.shap_po.shappoli.integration.trinkets.condition.factory.ItemConditionTypes;

public class TrinketsIntegration {
    public static void register() {
        PowerTypes.register();
        EntityActionTypes.register();
        EntityConditionTypes.register();
        ItemConditionTypes.register();

        TrinketsCommand.register();
    }
}
