package com.github.shap_po.shappoli.integration.trinkets;

import com.github.shap_po.shappoli.integration.trinkets.action.factory.EntityActionTypes;
import com.github.shap_po.shappoli.integration.trinkets.command.TrinketsCommand;
import com.github.shap_po.shappoli.integration.trinkets.component.item.ShappoliTrinketsDataComponentTypes;
import com.github.shap_po.shappoli.integration.trinkets.condition.factory.EntityConditionTypes;
import com.github.shap_po.shappoli.integration.trinkets.condition.factory.ItemConditionTypes;
import com.github.shap_po.shappoli.integration.trinkets.event.TrinketsEventListener;
import com.github.shap_po.shappoli.integration.trinkets.networking.ShappoliTrinketsPackets;
import com.github.shap_po.shappoli.integration.trinkets.power.factory.PowerTypes;
import com.github.shap_po.shappoli.integration.trinkets.slk.SlotLinkedKeyManager;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resource.ResourceType;

public class TrinketsIntegration {
    public static void register() {
        PowerTypes.register();
        EntityActionTypes.register();
        EntityConditionTypes.register();
        ItemConditionTypes.register();

        TrinketsCommand.register();

        ShappoliTrinketsDataComponentTypes.register();
        TrinketsEventListener.register();

        ShappoliTrinketsPackets.register();

        SlotLinkedKeyManager slotLinkedKeyManager = new SlotLinkedKeyManager();
        ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(slotLinkedKeyManager);
    }
}
