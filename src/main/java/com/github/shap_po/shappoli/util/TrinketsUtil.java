package com.github.shap_po.shappoli.util;

import com.github.shap_po.shappoli.data.TrinketSlotData;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Pair;
import net.minecraft.world.World;

import java.util.List;
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

    public static <T> Iterable<T> getIterable(Stream<T> stream) {
        return stream::iterator;
    }
}
