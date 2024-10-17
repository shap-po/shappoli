package com.github.shap_po.shappoli.event;

import com.github.shap_po.shappoli.power.type.ActiveAny;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

@Environment(EnvType.CLIENT)
public class ClientEventListener {
    public static void register() {
        ClientTickEvents.START_CLIENT_TICK.register(ActiveAny::integrateCallback);
    }
}
