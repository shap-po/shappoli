package com.github.shap_po.shappoli;

import com.github.shap_po.shappoli.command.ShappoliCommand;
import com.github.shap_po.shappoli.integration.ModIntegrations;
import com.github.shap_po.shappoli.registry.ModPowers;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Shappoli implements ModInitializer {
    public static final String MOD_ID = "shappoli";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        ModPowers.register();
        ModIntegrations.register();

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            ShappoliCommand.register(dispatcher);
        });
    }

    public static Identifier identifier(String path) {
        return Identifier.of(MOD_ID, path);
    }
}
