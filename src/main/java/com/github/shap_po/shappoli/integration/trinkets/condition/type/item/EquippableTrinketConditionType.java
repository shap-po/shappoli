package com.github.shap_po.shappoli.integration.trinkets.condition.type.item;

import com.github.shap_po.shappoli.Shappoli;
import com.github.shap_po.shappoli.integration.trinkets.data.ShappoliTrinketsDataTypes;
import com.github.shap_po.shappoli.integration.trinkets.data.TrinketSlotData;
import com.github.shap_po.shappoli.integration.trinkets.util.TrinketsUtil;
import com.github.shap_po.shappoli.util.InventoryUtil;
import dev.emi.trinkets.TrinketSlot;
import io.github.apace100.apoli.condition.factory.ConditionTypeFactory;
import io.github.apace100.apoli.condition.type.ItemConditionTypes;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Pair;
import net.minecraft.world.World;

import java.util.List;

public class EquippableTrinketConditionType {
    public static boolean condition(
        ItemStack stack,
        List<TrinketSlotData> slots,
        boolean onlyEmpty
    ) {
        Entity entity = InventoryUtil.getHolder(stack);
        if (!(entity instanceof LivingEntity livingEntity)) {
            return false;
        }

        return TrinketsUtil.getSlots(livingEntity, slots).anyMatch(slot -> {
            if (onlyEmpty && !slot.inventory().getStack(slot.index()).isEmpty()) {
                return false;
            }
            return TrinketSlot.canInsert(stack, slot, livingEntity);
        });
    }

    public static ConditionTypeFactory<Pair<World, ItemStack>> getFactory() {
        ConditionTypeFactory<Pair<World, ItemStack>> factory = new ConditionTypeFactory<>(
            Shappoli.identifier("equippable_trinket"),
            new SerializableData()
                .add("slot", ShappoliTrinketsDataTypes.TRINKET_SLOT, null)
                .add("slots", ShappoliTrinketsDataTypes.TRINKET_SLOTS, null)
                .add("only_empty", SerializableDataTypes.BOOLEAN, false)
            ,
            (data, worldAndStack) -> condition(
                worldAndStack.getRight(),
                TrinketSlotData.getSlots(data),
                data.getBoolean("only_empty")
            )
        );

        ItemConditionTypes.ALIASES.addPathAlias("is_equippable_trinket", factory.getSerializerId().getPath());
        return factory;
    }
}
