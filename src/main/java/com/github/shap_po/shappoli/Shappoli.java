package com.github.shap_po.shappoli;

import com.github.shap_po.shappoli.registry.ModPowers;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Shappoli implements ModInitializer {
    public static final String MOD_ID = "shappoli";
    public static final Logger LOGGER = LoggerFactory.getLogger("shappoli");

    @Override
    public void onInitialize() {
        ModPowers.register();
    }

    public static Identifier identifier(String path) {
        return Identifier.of(MOD_ID, path);
    }
}