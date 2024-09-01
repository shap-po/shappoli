package com.github.shap_po.shappoli.integration.trinkets.power;

import com.github.shap_po.shappoli.Shappoli;
import com.github.shap_po.shappoli.integration.trinkets.data.ShappoliTrinketsDataTypes;
import com.github.shap_po.shappoli.integration.trinkets.data.TrinketSlotData;
import com.github.shap_po.shappoli.integration.trinkets.util.TrinketsUtil;
import dev.emi.trinkets.api.SlotReference;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.factory.PowerFactories;
import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Pair;
import net.minecraft.world.World;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class ActionOnTrinketChangePower extends Power {
    private final Consumer<Entity> entityActionOnEquip;
    private final Consumer<Pair<World, StackReference>> itemActionOnEquip;
    private final Consumer<Entity> entityActionOnUnequip;
    private final Consumer<Pair<World, StackReference>> itemActionOnUnequip;
    private final Predicate<Pair<World, ItemStack>> itemCondition;
    private final List<TrinketSlotData> slots;
    private final boolean executeWhenSpawned;

    public ActionOnTrinketChangePower(
        PowerType<?> type,
        LivingEntity entity,
        Consumer<Entity> entityActionOnEquip,
        Consumer<Pair<World, StackReference>> itemActionOnEquip,
        Consumer<Entity> entityActionOnUnequip,
        Consumer<Pair<World, StackReference>> itemActionOnUnequip,
        Predicate<Pair<World, ItemStack>> itemCondition,
        List<TrinketSlotData> slots,
        boolean executeWhenSpawned
    ) {
        super(type, entity);
        this.entityActionOnEquip = entityActionOnEquip;
        this.itemActionOnEquip = itemActionOnEquip;
        this.entityActionOnUnequip = entityActionOnUnequip;
        this.itemActionOnUnequip = itemActionOnUnequip;
        this.itemCondition = itemCondition;
        this.slots = slots;
        this.executeWhenSpawned = executeWhenSpawned;
    }


    public boolean doesApply(LivingEntity actor, SlotReference slotReference, ItemStack item, boolean justSpawned) {
        return (
            (executeWhenSpawned || !justSpawned) &&
                (slots.isEmpty() || slots.stream().anyMatch(slot -> slot.test(slotReference))) &&
                (itemCondition == null || itemCondition.test(TrinketsUtil.getItemConditionPair(actor, item)))
        );
    }

    public void apply(LivingEntity actor, SlotReference slotReference, boolean isEquipping) {
        if (isEquipping) {
            if (entityActionOnEquip != null) {
                entityActionOnEquip.accept(actor);
            }
            if (itemActionOnEquip != null) {
                itemActionOnEquip.accept(TrinketsUtil.getItemActionPair(actor, slotReference));
            }
        } else {
            if (entityActionOnUnequip != null) {
                entityActionOnUnequip.accept(actor);
            }
            if (itemActionOnUnequip != null) {
                // FIXME: stack is a copy of an item, not the actual item, IDK how to fix it yet
                itemActionOnUnequip.accept(TrinketsUtil.getItemActionPair(actor, slotReference));
            }
        }

    }

    public static PowerFactory createFactory() {
        PowerFactory<Power> factory = new PowerFactory<>(
            Shappoli.identifier("action_on_trinket_change"),
            new SerializableData()
                .add("entity_action_on_equip", ApoliDataTypes.ENTITY_ACTION, null)
                .add("item_action_on_equip", ApoliDataTypes.ITEM_ACTION, null)
                .add("entity_action_on_unequip", ApoliDataTypes.ENTITY_ACTION, null)
                .add("item_action_on_unequip", ApoliDataTypes.ITEM_ACTION, null)
                .add("item_condition", ApoliDataTypes.ITEM_CONDITION)
                .add("slot", ShappoliTrinketsDataTypes.TRINKET_SLOT, null)
                .add("slots", ShappoliTrinketsDataTypes.TRINKET_SLOTS, null)
                .add("execute_when_spawned", SerializableDataTypes.BOOLEAN, false)
            ,
            data -> (type, player) -> new ActionOnTrinketChangePower(
                type,
                player,
                data.get("entity_action_on_equip"),
                data.get("item_action_on_equip"),
                data.get("entity_action_on_unequip"),
                data.get("item_action_on_unequip"),
                data.get("item_condition"),
                TrinketSlotData.getSlots(data),
                data.getBoolean("execute_when_spawned")
            )
        ).allowCondition();

        PowerFactories.ALIASES.addPathAlias("action_on_trinket_update", factory.getSerializerId().getPath());
        return factory;
    }
}
