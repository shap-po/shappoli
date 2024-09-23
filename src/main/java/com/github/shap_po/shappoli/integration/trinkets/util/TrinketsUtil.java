package com.github.shap_po.shappoli.integration.trinkets.util;

import com.github.shap_po.shappoli.integration.trinkets.data.TrinketSlotData;
import dev.emi.trinkets.TrinketPlayerScreenHandler;
import dev.emi.trinkets.api.*;
import dev.emi.trinkets.payload.SyncInventoryPayload;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Pair;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class TrinketsUtil {
    public static Pair<World, StackReference> getItemActionPair(LivingEntity entity, SlotReference slotReference) {
        return new Pair<>(entity.getWorld(), StackReference.of(slotReference.inventory(), slotReference.index()));
    }

    public static Pair<World, ItemStack> getItemConditionPair(LivingEntity entity, ItemStack item) {
        return new Pair<>(entity.getWorld(), item);
    }


    /**
     * @param entity The entity to get the trinkets from
     * @return A stream of pairs of slot references and item stacks of the trinkets in the given slots.
     */
    public static Stream<Pair<SlotReference, ItemStack>> getTrinkets(LivingEntity entity) {
        return TrinketsApi.getTrinketComponent(entity).map(trinketComponent -> trinketComponent.getEquipped((i) -> true).stream()).orElse(Stream.empty());
    }

    /**
     * @param entity The entity to get the trinkets from
     * @param slots  The slots to get the trinkets from. If empty, all trinkets will be returned.
     * @return A stream of pairs of slot references and item stacks of the trinkets in the given slots.
     */
    public static Stream<Pair<SlotReference, ItemStack>> getTrinkets(LivingEntity entity, List<TrinketSlotData> slots) {
        return TrinketsUtil.getTrinkets(entity).filter(trinket -> slots.isEmpty() || slots.stream().anyMatch(slot -> slot.test(trinket.getLeft())));
    }

    /**
     * @param entity        The entity to get the trinkets from
     * @param slots         The slots to get the trinkets from. If empty, all trinkets will be returned.
     * @param itemCondition The condition that the item must meet to be included in the list.
     * @return A stream of pairs of slot references and item stacks of the trinkets in the given slots.
     */
    public static Stream<Pair<SlotReference, ItemStack>> getTrinkets(LivingEntity entity, List<TrinketSlotData> slots, Predicate<Pair<World, ItemStack>> itemCondition) {
        return TrinketsUtil.getTrinkets(entity, slots).filter(trinket -> itemCondition == null || itemCondition.test(TrinketsUtil.getItemConditionPair(entity, trinket.getRight())));
    }

    public static String getSlotId(SlotType slotType) {
        return slotType.getGroup() + "/" + slotType.getName();
    }

    public static String getSlotId(SlotType slotType, int index) {
        return getSlotId(slotType) + "/" + index;
    }

    /**
     * Get all slot references of the given entity.
     *
     * @param entity The entity to get the slots from.
     * @return A stream of slot references.
     */
    public static Stream<SlotReference> getSlots(LivingEntity entity) {
        return TrinketsApi.getTrinketComponent(entity)
            .map(trinketComponent -> trinketComponent.getInventory().entrySet().stream()
                .flatMap(group -> group.getValue().values().stream()
                    .flatMap(trinketInventory -> Stream.iterate(0, i -> i < trinketInventory.size(), i -> i + 1)
                        .map(i -> new SlotReference(trinketInventory, i))
                    )
                )
            ).orElse(Stream.empty());
    }

    /**
     * Get all slot references of the given entity that match the given slot data.
     *
     * @param entity The entity to get the slots from.
     * @param slots  List of slot data to filter the slots.
     * @return A stream of slot references.
     */
    public static Stream<SlotReference> getSlots(LivingEntity entity, List<TrinketSlotData> slots) {
        return getSlots(entity).filter(slot -> slots.stream().anyMatch(trinketSlotData -> trinketSlotData.test(slot)));
    }

    /**
     * Based on the ${@link dev.emi.trinkets.mixin.LivingEntityMixin#tick()} method.
     */
    public static void updateInventories(TrinketComponent trinket) {
        LivingEntity entity = trinket.getEntity();

        Set<TrinketInventory> inventoriesToSend = trinket.getTrackingUpdates();
        if (!inventoriesToSend.isEmpty()) {
            Map<String, NbtCompound> map = new HashMap<>();
            for (TrinketInventory trinketInventory : inventoriesToSend) {
                map.put(trinketInventory.getSlotType().getId(), trinketInventory.getSyncTag());
            }
            SyncInventoryPayload packet = new SyncInventoryPayload(entity.getId(), new HashMap<>(), map);
            for (ServerPlayerEntity player : PlayerLookup.tracking(entity)) {
                ServerPlayNetworking.send(player, packet);
            }

            if (entity instanceof ServerPlayerEntity serverPlayer) {
                ServerPlayNetworking.send(serverPlayer, packet);

                if (!inventoriesToSend.isEmpty()) {
                    ((TrinketPlayerScreenHandler) serverPlayer.playerScreenHandler).trinkets$updateTrinketSlots(false);
                }
            }
            inventoriesToSend.clear();
        }
    }
}
