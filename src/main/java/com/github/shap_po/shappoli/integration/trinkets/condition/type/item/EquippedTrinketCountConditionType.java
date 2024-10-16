package com.github.shap_po.shappoli.integration.trinkets.condition.type.item;

import com.github.shap_po.shappoli.Shappoli;
import com.github.shap_po.shappoli.integration.trinkets.data.ShappoliTrinketsDataTypes;
import com.github.shap_po.shappoli.integration.trinkets.data.TrinketSlotData;
import com.github.shap_po.shappoli.integration.trinkets.util.TrinketsUtil;
import com.github.shap_po.shappoli.util.InventoryUtil;
import io.github.apace100.apoli.condition.factory.ConditionTypeFactory;
import io.github.apace100.apoli.condition.type.ItemConditionTypes;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.util.Comparison;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Pair;
import net.minecraft.world.World;

import java.util.List;

public class EquippedTrinketCountConditionType {
    public static boolean condition(
        ItemStack stack,
        List<TrinketSlotData> slots,
        Comparison comparison, int compareTo
    ) {
        Entity entity = InventoryUtil.getHolder(stack);
        if (!(entity instanceof LivingEntity livingEntity)) {
            return false;
        }

        int count = TrinketsUtil.getTrinkets(livingEntity, slots)
            .filter(trinket -> trinket.getRight().getItem().equals(stack.getItem()))
            .mapToInt(trinket -> 1).sum();
        return comparison.compare(count, compareTo);
    }

    public static ConditionTypeFactory<Pair<World, ItemStack>> getFactory() {
        ConditionTypeFactory<Pair<World, ItemStack>> factory = new ConditionTypeFactory<>(
            Shappoli.identifier("equipped_trinket_count"),
            new SerializableData()
                .add("slot", ShappoliTrinketsDataTypes.TRINKET_SLOT, null)
                .add("slots", ShappoliTrinketsDataTypes.TRINKET_SLOTS, null)
                .add("comparison", ApoliDataTypes.COMPARISON, Comparison.GREATER_THAN_OR_EQUAL)
                .add("compare_to", SerializableDataTypes.INT, 1)
            ,
            (data, worldAndStack) -> condition(
                worldAndStack.getRight(),
                TrinketSlotData.getSlots(data),
                data.get("comparison"),
                data.getInt("compare_to")
            )
        );

        ItemConditionTypes.ALIASES.addPathAlias("equipped_trinket", factory.getSerializerId().getPath());
        return factory;
    }
}
