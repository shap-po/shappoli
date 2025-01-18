package com.github.shap_po.shappoli.integration.trinkets.networking.s2c;

import com.github.shap_po.shappoli.Shappoli;
import com.github.shap_po.shappoli.integration.trinkets.slk.SlotLinkedKey;
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

public record SyncSlotLinkedKeysS2CPacket(Map<Identifier, SlotLinkedKey> slotLinkedKeyMap) implements CustomPayload {
    public static final Id<SyncSlotLinkedKeysS2CPacket> PACKET_ID = new Id<>(Shappoli.identifier("s2c/sync_slot_essence_keys_registry"));
    public static final PacketCodec<RegistryByteBuf, SyncSlotLinkedKeysS2CPacket> PACKET_CODEC = PacketCodec.of(SyncSlotLinkedKeysS2CPacket::write, SyncSlotLinkedKeysS2CPacket::read);

    public static SyncSlotLinkedKeysS2CPacket read(RegistryByteBuf buf) {
        try {
            Collection<SlotLinkedKey> slotLinkedKeys = new ObjectArrayList<>();
            int slotLinkedKeybindCount = buf.readVarInt();

            for (int i = 0; i < slotLinkedKeybindCount; i++) {
                slotLinkedKeys.add(SlotLinkedKey.DATA_TYPE.receive(buf));
            }

            return new SyncSlotLinkedKeysS2CPacket(
                slotLinkedKeys.stream()
                    .collect(Collectors.toMap(
                        SlotLinkedKey::getId,
                        Function.identity(),
                        (oldSlotLinkedKeybinding, newSlotLinkedKeybinding) -> newSlotLinkedKeybinding,
                        Object2ObjectOpenHashMap::new
                    ))
            );

        } catch (Exception e) {
            Shappoli.LOGGER.error(e.getMessage());
            throw e;
        }
    }

    public void write(RegistryByteBuf buf) {
        Collection<SlotLinkedKey> slotLinkedKeys = this.slotLinkedKeyMap().values();
        buf.writeVarInt(slotLinkedKeys.size());

        slotLinkedKeys.forEach(essence -> SlotLinkedKey.DATA_TYPE.send(buf, essence));
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }
}
