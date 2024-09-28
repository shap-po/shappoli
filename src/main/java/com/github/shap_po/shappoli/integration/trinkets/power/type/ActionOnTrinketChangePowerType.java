package com.github.shap_po.shappoli.integration.trinkets.power.type;

import com.github.shap_po.shappoli.Shappoli;
import com.github.shap_po.shappoli.integration.trinkets.access.SyncingTrinketInventory;
import com.github.shap_po.shappoli.integration.trinkets.data.ShappoliTrinketsDataTypes;
import com.github.shap_po.shappoli.integration.trinkets.data.TrinketSlotData;
import com.github.shap_po.shappoli.integration.trinkets.util.TrinketsUtil;
import com.github.shap_po.shappoli.util.MiscUtil;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketInventory;
import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.factory.PowerTypeFactory;
import io.github.apace100.apoli.power.factory.PowerTypes;
import io.github.apace100.apoli.power.type.PowerType;
import io.github.apace100.calio.data.SerializableData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Pair;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class ActionOnTrinketChangePowerType extends PowerType {
    private final @Nullable Consumer<Entity> entityActionOnEquip;
    private final @Nullable Consumer<Pair<World, StackReference>> itemActionOnEquip;
    private final @Nullable Consumer<Entity> entityActionOnUnequip;
    private final @Nullable Consumer<Pair<World, StackReference>> itemActionOnUnequip;
    private final @Nullable Predicate<Pair<World, ItemStack>> itemCondition;
    private final List<TrinketSlotData> slots;

    public ActionOnTrinketChangePowerType(
        Power power,
        LivingEntity entity,
        @Nullable Consumer<Entity> entityActionOnEquip,
        @Nullable Consumer<Pair<World, StackReference>> itemActionOnEquip,
        @Nullable Consumer<Entity> entityActionOnUnequip,
        @Nullable Consumer<Pair<World, StackReference>> itemActionOnUnequip,
        @Nullable Predicate<Pair<World, ItemStack>> itemCondition,
        List<TrinketSlotData> slots
    ) {
        super(power, entity);
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
        PowerHolderComponent.withPowerTypes(entity, ActionOnTrinketChangePowerType.class,
            p -> p.doesApply(ref, stack),
            p -> p.apply(ref, isEquipping)
        );
    }

    public static PowerTypeFactory getFactory() {
        PowerTypeFactory<?> factory = new PowerTypeFactory<>(
            Shappoli.identifier("action_on_trinket_change"),
            new SerializableData()
                .add("entity_action_on_equip", ApoliDataTypes.ENTITY_ACTION, null)
                .add("item_action_on_equip", ApoliDataTypes.ITEM_ACTION, null)
                .add("entity_action_on_unequip", ApoliDataTypes.ENTITY_ACTION, null)
                .add("item_action_on_unequip", ApoliDataTypes.ITEM_ACTION, null)
                .add("item_condition", ApoliDataTypes.ITEM_CONDITION)
                .add("slot", ShappoliTrinketsDataTypes.TRINKET_SLOT, null)
                .add("slots", ShappoliTrinketsDataTypes.TRINKET_SLOTS, null)
                .postProcessor(data -> MiscUtil.checkHasAtLeastOneField(data, "entity_action_on_equip", "item_action_on_equip", "entity_action_on_unequip", "item_action_on_unequip"))
            ,
            data -> (type, player) -> new ActionOnTrinketChangePowerType(
                type,
                player,
                data.get("entity_action_on_equip"),
                data.get("item_action_on_equip"),
                data.get("entity_action_on_unequip"),
                data.get("item_action_on_unequip"),
                data.get("item_condition"),
                TrinketSlotData.getSlots(data)
            )
        ).allowCondition();

        PowerTypes.ALIASES.addPathAlias("action_on_trinket_update", factory.getSerializerId().getPath());
        return factory;
    }
}
