package com.github.shap_po.shappoli.integration.trinkets.power.factory.condition.item;

import com.github.shap_po.shappoli.Shappoli;
import com.github.shap_po.shappoli.integration.trinkets.data.ShappoliTrinketsDataTypes;
import com.github.shap_po.shappoli.integration.trinkets.data.TrinketSlotData;
import com.github.shap_po.shappoli.integration.trinkets.util.TrinketsUtil;
import com.github.shap_po.shappoli.util.InventoryUtil;
import dev.emi.trinkets.TrinketSlot;
import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

import java.util.List;

public class EquippableTrinketCondition {
    public static boolean condition(SerializableData.Instance data, ItemStack stack) {
        Entity entity = InventoryUtil.getHolder(stack);
        if (!(entity instanceof LivingEntity livingEntity)) {
            return false;
        }

        List<TrinketSlotData> slots = TrinketSlotData.getSlots(data);
        boolean onlyEmpty = data.getBoolean("only_empty");

        return TrinketsUtil.getSlots(livingEntity, slots).anyMatch(slot -> {
            if (onlyEmpty && !slot.inventory().getStack(slot.index()).isEmpty()) {
                return false;
            }
            return TrinketSlot.canInsert(stack, slot, livingEntity);
        });
    }

    public static ConditionFactory<ItemStack> getFactory() {
        return new ConditionFactory<>(
            Shappoli.identifier("is_equippable_trinket"),
            new SerializableData()
                .add("slot", ShappoliTrinketsDataTypes.TRINKET_SLOT, null)
                .add("slots", ShappoliTrinketsDataTypes.TRINKET_SLOTS, null)
                .add("only_empty", SerializableDataTypes.BOOLEAN, false)
            ,
            EquippableTrinketCondition::condition
        );
    }
}
