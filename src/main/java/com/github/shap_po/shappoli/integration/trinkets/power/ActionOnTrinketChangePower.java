package com.github.shap_po.shappoli.integration.trinkets.power;

import com.github.shap_po.shappoli.Shappoli;
import com.github.shap_po.shappoli.integration.trinkets.access.SyncingTrinketInventory;
import com.github.shap_po.shappoli.integration.trinkets.data.ShappoliTrinketsDataTypes;
import com.github.shap_po.shappoli.integration.trinkets.data.TrinketSlotData;
import com.github.shap_po.shappoli.integration.trinkets.util.TrinketsUtil;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketInventory;
import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.calio.data.SerializableData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Pair;
import net.minecraft.world.World;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class ActionOnTrinketChangePower extends Power {
    private final Consumer<Entity> entityActionOnEquip;
    private final Consumer<Pair<World, ItemStack>> itemActionOnEquip;
    private final Consumer<Entity> entityActionOnUnequip;
    private final Consumer<Pair<World, ItemStack>> itemActionOnUnequip;
    private final Predicate<ItemStack> itemCondition;
    private final List<TrinketSlotData> slots;

    public ActionOnTrinketChangePower(
        PowerType<?> type,
        LivingEntity entity,
        Consumer<Entity> entityActionOnEquip,
        Consumer<Pair<World, ItemStack>> itemActionOnEquip,
        Consumer<Entity> entityActionOnUnequip,
        Consumer<Pair<World, ItemStack>> itemActionOnUnequip,
        Predicate<ItemStack> itemCondition,
        List<TrinketSlotData> slots
    ) {
        super(type, entity);
        this.entityActionOnEquip = entityActionOnEquip;
        this.itemActionOnEquip = itemActionOnEquip;
        this.entityActionOnUnequip = entityActionOnUnequip;
        this.itemActionOnUnequip = itemActionOnUnequip;
        this.itemCondition = itemCondition;
        this.slots = slots;
    }

    public boolean doesApply(SlotReference slotReference, ItemStack item) {
        return ((slots.isEmpty() || slots.stream().anyMatch(slot -> slot.test(slotReference))) &&
            (itemCondition == null || itemCondition.test(TrinketsUtil.getItemConditionPair(entity, item)))
        );
    }

    public void apply(SlotReference slotReference, boolean isEquipping) {
        if (isEquipping) {
            if (entityActionOnEquip != null) {
                entityActionOnEquip.accept(entity);
            }
            if (itemActionOnEquip != null) {
                itemActionOnEquip.accept(TrinketsUtil.getItemActionPair(entity, slotReference));
            }
        } else {
            if (entityActionOnUnequip != null) {
                entityActionOnUnequip.accept(entity);
            }
            if (itemActionOnUnequip != null) {
                itemActionOnUnequip.accept(TrinketsUtil.getItemActionPair(entity, slotReference));
            }
        }
    }

    public static void handleTrinketChange(LivingEntity entity, TrinketInventory inventory, ItemStack stack, int slot, boolean isEquipping) {
        if (entity.getWorld().isClient || ((SyncingTrinketInventory) inventory).shappoli$isSyncing()) {
            return;
        }

        SlotReference ref = new SlotReference(inventory, slot);
        PowerHolderComponent.withPowers(entity, ActionOnTrinketChangePower.class,
            p -> p.doesApply(ref, stack),
            p -> p.apply(ref, isEquipping)
        );
    }

    public static PowerFactory createFactory() {
        return new PowerFactory<>(
            Shappoli.identifier("action_on_trinket_change"),
            new SerializableData()
                .add("entity_action_on_equip", ApoliDataTypes.ENTITY_ACTION, null)
                .add("item_action_on_equip", ApoliDataTypes.ITEM_ACTION, null)
                .add("entity_action_on_unequip", ApoliDataTypes.ENTITY_ACTION, null)
                .add("item_action_on_unequip", ApoliDataTypes.ITEM_ACTION, null)
                .add("item_condition", ApoliDataTypes.ITEM_CONDITION)
                .add("slot", ShappoliTrinketsDataTypes.TRINKET_SLOT, null)
                .add("slots", ShappoliTrinketsDataTypes.TRINKET_SLOTS, null)
            ,
            data -> (type1, player) -> new ActionOnTrinketChangePower(
                type1,
                player,
                data.get("entity_action_on_equip"),
                data.get("item_action_on_equip"),
                data.get("entity_action_on_unequip"),
                data.get("item_action_on_unequip"),
                data.get("item_condition"),
                TrinketSlotData.getSlots(data)
            )
        ).allowCondition();
    }
}
