package com.github.shap_po.shappoli.integration.trinkets.networking;

import com.github.shap_po.shappoli.integration.trinkets.networking.s2c.SyncSlotLinkedKeysS2CPacket;
import com.github.shap_po.shappoli.integration.trinkets.slk.SlotLinkedKeyManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

@Environment(EnvType.CLIENT)
public class ShappoliTrinketsPacketsS2C {
    public static void register() {
        ClientPlayConnectionEvents.INIT.register(((clientPlayNetworkHandler, minecraftClient) -> {
            ClientPlayNetworking.registerReceiver(SyncSlotLinkedKeysS2CPacket.PACKET_ID, (packet, context) -> SlotLinkedKeyManager.receive(packet));
        }));
    }
}
