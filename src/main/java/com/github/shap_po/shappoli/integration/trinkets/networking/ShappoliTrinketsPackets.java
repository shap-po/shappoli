package com.github.shap_po.shappoli.integration.trinkets.networking;

import com.github.shap_po.shappoli.integration.trinkets.networking.s2c.SyncSlotLinkedKeysS2CPacket;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;

public class ShappoliTrinketsPackets {
    public static void register() {
        PayloadTypeRegistry.playS2C().register(SyncSlotLinkedKeysS2CPacket.PACKET_ID, SyncSlotLinkedKeysS2CPacket.PACKET_CODEC);
    }
}
