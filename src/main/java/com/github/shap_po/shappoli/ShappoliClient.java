package com.github.shap_po.shappoli;

import com.github.shap_po.shappoli.event.ClientEventListener;
import com.github.shap_po.shappoli.integration.ModIntegrations;
import com.github.shap_po.shappoli.networking.packet.c2s.UseActiveAnyPowersC2SPacket;
import com.github.shap_po.shappoli.power.type.ActiveAny;
import io.github.apace100.apoli.power.type.PowerType;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

@Environment(EnvType.CLIENT)
public class ShappoliClient implements ClientModInitializer {
    public static final HashMap<String, Boolean> lastKeyBindingStates = new HashMap<>();

    public static void performActivePowers(List<Pair<PowerType, ActiveAny.Key>> triggeredPowers) {
        List<Pair<Identifier, ActiveAny.Key>> powersAndKeys = new LinkedList<>();
        for (Pair<PowerType, ActiveAny.Key> pair : triggeredPowers) {
            PowerType powerType = pair.getLeft();
            ActiveAny.Key key = pair.getRight();

            if (powerType instanceof ActiveAny activePower) {
                activePower.onUse(key);
            }

            powersAndKeys.add(new Pair<>(powerType.getPower().getId(), key));
        }

        ClientPlayNetworking.send(new UseActiveAnyPowersC2SPacket(powersAndKeys));
    }

    @Override
    public void onInitializeClient() {
        ClientEventListener.register();
        ModIntegrations.registerClient();
    }
}
