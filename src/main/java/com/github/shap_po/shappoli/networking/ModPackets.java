package com.github.shap_po.shappoli.networking;

import com.github.shap_po.shappoli.networking.packet.c2s.UseActiveAnyPowersC2SPacket;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;

public class ModPackets {
    public static void register() {
        PayloadTypeRegistry.playC2S().register(UseActiveAnyPowersC2SPacket.PACKET_ID, UseActiveAnyPowersC2SPacket.PACKET_CODEC);
    }
}
