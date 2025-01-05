package com.github.shap_po.shappoli.integration;

import com.github.shap_po.shappoli.integration.origins.OriginsIntegration;
import com.github.shap_po.shappoli.integration.trinkets.TrinketsIntegration;
import com.github.shap_po.shappoli.integration.walkers.WalkersIntegration;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;

public class ModIntegrations {
    public static void register() {
        register(OriginsIntegration::register, "origins");
        register(TrinketsIntegration::register, "trinkets");
        register(WalkersIntegration::register, "walkers"); // woodwalkers
    }

    @Environment(EnvType.CLIENT)
    public static void registerClient(){
        register(TrinketsIntegration::registerClient, "trinkets");
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
