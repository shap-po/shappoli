package com.github.shap_po.shappoli;

import com.github.shap_po.shappoli.event.ClientEventListener;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.util.HashMap;

@Environment(EnvType.CLIENT)
public class ShappoliClient implements ClientModInitializer {
    public static final HashMap<String, Boolean> lastKeyBindingStates = new HashMap<>();

    @Override
    public void onInitializeClient() {
        ClientEventListener.register();
    }
}
