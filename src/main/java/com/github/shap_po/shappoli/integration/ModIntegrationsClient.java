package com.github.shap_po.shappoli.integration;

import com.github.shap_po.shappoli.integration.trinkets.TrinketsIntegrationClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;

@Environment(EnvType.CLIENT)
public class ModIntegrationsClient {
    public static void register() {
        register(TrinketsIntegrationClient::register, "trinkets");
    }

    @SuppressWarnings({"SameParameterValue"})
    private static void register(Runnable runnable, String modid) {
        if (isModLoaded(modid)) {
            runnable.run();
        }
    }

    private static boolean isModLoaded(String modid) {
        return FabricLoader.getInstance().isModLoaded(modid);
    }
}
