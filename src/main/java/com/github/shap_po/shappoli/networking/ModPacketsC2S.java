package com.github.shap_po.shappoli.networking;

import com.github.shap_po.shappoli.Shappoli;
import com.github.shap_po.shappoli.networking.packet.c2s.UseActiveAnyPowersC2SPacket;
import com.github.shap_po.shappoli.power.type.ActiveAny;
import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.power.PowerManager;
import io.github.apace100.apoli.power.type.PowerType;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;

public class ModPacketsC2S {
    public static void register() {
        ServerPlayConnectionEvents.INIT.register((handler, server) ->
            ServerPlayNetworking.registerReceiver(handler, UseActiveAnyPowersC2SPacket.PACKET_ID, ModPacketsC2S::onUseActiveAnyPowers)
        );
    }

    private static void onUseActiveAnyPowers(UseActiveAnyPowersC2SPacket payload, ServerPlayNetworking.Context context) {
        ServerPlayerEntity player = context.player();
        PowerHolderComponent component = PowerHolderComponent.getNullable(player);
        if (component == null) {
            return;
        }

        for (Pair<Identifier, ActiveAny.Key> powerAndKey : payload.powersAndKeys()) {
            Identifier powerTypeId = powerAndKey.getLeft();
            ActiveAny.Key key = powerAndKey.getRight();

            PowerType powerType = PowerManager.getOptional(powerTypeId)
                .map(component::getPowerType)
                .orElse(null);

            if (powerType == null) {
                Shappoli.LOGGER.warn("Found unknown power \"{}\" while receiving packet for triggering active any powers of player {}!", powerTypeId, player.getName().getString());
            } else if (powerType instanceof ActiveAny activePower) {
                activePower.onUse(key);
            }
        }
    }
}
