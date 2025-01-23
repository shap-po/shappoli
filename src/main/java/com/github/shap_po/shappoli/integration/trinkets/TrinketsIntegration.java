package com.github.shap_po.shappoli.integration.trinkets;

import com.github.shap_po.shappoli.integration.trinkets.action.type.ShappoliTrinketsEntityActionTypes;
import com.github.shap_po.shappoli.integration.trinkets.command.TrinketsCommand;
import com.github.shap_po.shappoli.integration.trinkets.component.item.ShappoliTrinketsDataComponentTypes;
import com.github.shap_po.shappoli.integration.trinkets.condition.type.ShappoliTrinketsEntityConditionTypes;
import com.github.shap_po.shappoli.integration.trinkets.condition.type.ShappoliTrinketsItemConditionTypes;
import com.github.shap_po.shappoli.integration.trinkets.event.TrinketsEventListener;
import com.github.shap_po.shappoli.integration.trinkets.networking.ShappoliTrinketsPackets;
import com.github.shap_po.shappoli.integration.trinkets.networking.ShappoliTrinketsPacketsS2C;
import com.github.shap_po.shappoli.integration.trinkets.power.type.ShappoliTrinketsPowerTypes;
import com.github.shap_po.shappoli.integration.trinkets.keybinding.TrinketKeyBindingManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resource.ResourceType;

public class TrinketsIntegration {
    public static void register() {
        ShappoliTrinketsPowerTypes.register();
        ShappoliTrinketsEntityActionTypes.register();
        ShappoliTrinketsEntityConditionTypes.register();
        ShappoliTrinketsItemConditionTypes.register();

        TrinketsCommand.register();

        ShappoliTrinketsDataComponentTypes.register();
        TrinketsEventListener.register();

        ShappoliTrinketsPackets.register();

        ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new TrinketKeyBindingManager());
    }

    @Environment(EnvType.CLIENT)
    public static void registerClient() {
        ShappoliTrinketsPacketsS2C.register();
    }
}
