package com.github.shap_po.shappoli.integration;

import com.github.shap_po.shappoli.integration.backport.apoli.ApoliBackport;
import com.github.shap_po.shappoli.integration.origins.OriginsIntegration;
import com.github.shap_po.shappoli.integration.trinkets.TrinketsIntegration;
import com.github.shap_po.shappoli.integration.walkers.WalkersIntegration;
import net.fabricmc.loader.api.FabricLoader;

public class ModIntegrations {
    public static void register() {
        ApoliBackport.register(); // no need to check if Apoli is loaded, as it's required

        register(OriginsIntegration::register, "origins");
        register(TrinketsIntegration::register, "trinkets");
        register(WalkersIntegration::register, "walkers"); // woodwalkers
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
