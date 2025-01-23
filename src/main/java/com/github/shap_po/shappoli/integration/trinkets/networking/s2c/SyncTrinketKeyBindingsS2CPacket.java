package com.github.shap_po.shappoli.integration.trinkets.networking.s2c;

import com.github.shap_po.shappoli.Shappoli;
import com.github.shap_po.shappoli.integration.trinkets.keybinding.TrinketKeyBinding;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public record SyncTrinketKeyBindingsS2CPacket(Map<Identifier, TrinketKeyBinding> trinketKeyBindingMap) implements CustomPayload {
    public static final Id<SyncTrinketKeyBindingsS2CPacket> PACKET_ID = new Id<>(Shappoli.identifier("s2c/sync_trinket_key_bindings_registry"));
    public static final PacketCodec<RegistryByteBuf, SyncTrinketKeyBindingsS2CPacket> PACKET_CODEC = PacketCodec.of(SyncTrinketKeyBindingsS2CPacket::write, SyncTrinketKeyBindingsS2CPacket::read);

    public static SyncTrinketKeyBindingsS2CPacket read(RegistryByteBuf buf) {
        try {
            Collection<TrinketKeyBinding> trinketKeyBindings = new ObjectArrayList<>();
            int count = buf.readVarInt();

            for (int i = 0; i < count; i++) {
                trinketKeyBindings.add(TrinketKeyBinding.DATA_TYPE.receive(buf));
            }

            return new SyncTrinketKeyBindingsS2CPacket(
                trinketKeyBindings.stream()
                    .collect(Collectors.toMap(
                        TrinketKeyBinding::getId,
                        Function.identity(),
                        (oldTrinketKeyBinding, newTrinketKeyBinding) -> newTrinketKeyBinding,
                        Object2ObjectOpenHashMap::new
                    ))
            );

        } catch (Exception e) {
            Shappoli.LOGGER.error(e.getMessage());
            throw e;
        }
    }

    public void write(RegistryByteBuf buf) {
        Collection<TrinketKeyBinding> trinketKeyBindings = this.trinketKeyBindingMap().values();
        buf.writeVarInt(trinketKeyBindings.size());

        trinketKeyBindings.forEach(essence -> TrinketKeyBinding.DATA_TYPE.send(buf, essence));
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }
}
