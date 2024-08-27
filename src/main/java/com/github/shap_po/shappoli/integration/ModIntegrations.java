package com.github.shap_po.shappoli.integration;

import com.github.shap_po.shappoli.integration.trinkets.TrinketsIntegration;
import net.fabricmc.loader.api.FabricLoader;

public class ModIntegrations {
    public static void register() {
        register(TrinketsIntegration::register, "trinkets");
    }

    private static void register(Runnable runnable, String modid) {
        if (isModLoaded(modid)) {
            runnable.run();
        }
    }

    private static boolean isModLoaded(String modid) {
        return FabricLoader.getInstance().isModLoaded(modid);
    }
}
