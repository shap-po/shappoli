package com.github.shap_po.shappoli.integration.trinkets;

import com.github.shap_po.shappoli.integration.trinkets.networking.ShappoliTrinketsPacketsS2C;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class TrinketsIntegrationClient {
    public static void register() {
        ShappoliTrinketsPacketsS2C.register();
    }
}
