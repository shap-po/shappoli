package com.github.shap_po.shappoli.data;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.SlotType;
import dev.emi.trinkets.api.TrinketsApi;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Pair;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class TrinketSlotData {
    public static final SerializableData DATA = new SerializableData()
        .add("name", SerializableDataTypes.STRING, null)
        .add("group", SerializableDataTypes.STRING, null)
        .add("index", SerializableDataTypes.INT, null);

    public final @Nullable String name;
    public final @Nullable String group;
    public final @Nullable Integer index;

    public TrinketSlotData(@Nullable String name, @Nullable String group, @Nullable Integer index) {
        this.name = name;
        this.group = group;
        this.index = index;
    }

    public boolean test(SlotReference slotReference) {
        SlotType slotType = slotReference.inventory().getSlotType();
        return (group == null || group.equals(slotType.getGroup())) &&
            (name == null || name.equals(slotType.getName())) &&
            (index == null || index == slotReference.index());
    }

    /**
     * @param data The data to get the slots from. Data can contain a single slot or/and a list of slots.
     * @return A list of trinket slot data from the given data.
     */
    public static List<TrinketSlotData> getSlots(SerializableData.Instance data) {
        List<TrinketSlotData> slots = new ArrayList<>();
        if (data.isPresent("slot")) {
            slots.add(data.get("slot"));
        }
        if (data.isPresent("slots")) {
            slots.addAll(data.get("slots"));
        }
        return slots;
    }

    /**
     * @param entity The entity to get the trinkets from
     * @param slots  The slots to get the trinkets from. If empty, all trinkets will be returned.
     * @return A stream of pairs of slot references and item stacks of the trinkets in the given slots.
     */
    public static Stream<Pair<SlotReference, ItemStack>> getTrinkets(LivingEntity entity, List<TrinketSlotData> slots) {
        return TrinketsApi.getTrinketComponent(entity).map(trinketComponent -> trinketComponent.getEquipped((i) -> true).stream()
            .filter(trinket -> slots.isEmpty() || slots.stream().anyMatch(slot -> slot.test(trinket.getLeft())))).orElse(Stream.empty());
    }

    /**
     * @param entity        The entity to get the trinkets from
     * @param slots         The slots to get the trinkets from. If empty, all trinkets will be returned.
     * @param itemCondition The condition that the item must meet to be included in the list.
     * @return A stream of pairs of slot references and item stacks of the trinkets in the given slots.
     */
    public static Stream<Pair<SlotReference, ItemStack>> getTrinkets(LivingEntity entity, List<TrinketSlotData> slots, Predicate<Pair<World, ItemStack>> itemCondition) {
        return TrinketSlotData.getTrinkets(entity, slots)
            .filter(trinket -> itemCondition == null || itemCondition.test(new Pair<>(entity.getWorld(), trinket.getRight())));
    }


    public static TrinketSlotData fromData(SerializableData.Instance dataInstance) {
        return new TrinketSlotData(
            dataInstance.get("name"),
            dataInstance.get("group"),
            dataInstance.isPresent("index") ? dataInstance.getInt("index") : null
        );
    }

    public static SerializableData.Instance toData(SerializableData data, TrinketSlotData trinketSlotData) {
        SerializableData.Instance dataInstance = data.new Instance();
        dataInstance.set("name", trinketSlotData.name);
        dataInstance.set("group", trinketSlotData.group);
        dataInstance.set("index", trinketSlotData.index);
        return dataInstance;
    }
}
