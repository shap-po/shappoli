package com.github.shap_po.shappoli.integration.trinkets.networking;

import com.github.shap_po.shappoli.integration.trinkets.networking.s2c.SyncTrinketKeyBindingsS2CPacket;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;

public class ShappoliTrinketsPackets {
    public static void register() {
        PayloadTypeRegistry.playS2C().register(SyncTrinketKeyBindingsS2CPacket.PACKET_ID, SyncTrinketKeyBindingsS2CPacket.PACKET_CODEC);
    }
}
