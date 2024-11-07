package com.github.shap_po.shappoli.networking.packet.c2s;

import com.github.shap_po.shappoli.Shappoli;
import com.github.shap_po.shappoli.data.ShappoliDataTypes;
import com.github.shap_po.shappoli.power.type.ActiveAny;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;

import java.util.ArrayList;
import java.util.List;

public record UseActiveAnyPowersC2SPacket(
    List<Pair<Identifier, ActiveAny.Key>> powersAndKeys) implements CustomPayload {
    public static final Id<UseActiveAnyPowersC2SPacket> PACKET_ID = new Id<>(Shappoli.identifier("c2s/use_active_powers"));
    public static final PacketCodec<RegistryByteBuf, UseActiveAnyPowersC2SPacket> PACKET_CODEC = PacketCodec.tuple(
        PacketCodecs.collection(ArrayList::new, PacketCodec.ofStatic(
                (buf, pair) -> {
                    buf.writeIdentifier(pair.getLeft());
                    ShappoliDataTypes.ACTIVE_ANY_KEY.packetCodec().encode(buf, pair.getRight());
                },
                buf -> new Pair<>(buf.readIdentifier(), ShappoliDataTypes.ACTIVE_ANY_KEY.packetCodec().decode(buf))
            )
        ), UseActiveAnyPowersC2SPacket::powersAndKeys,
        UseActiveAnyPowersC2SPacket::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }
}
