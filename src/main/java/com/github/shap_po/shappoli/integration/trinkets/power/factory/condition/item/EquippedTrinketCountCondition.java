package com.github.shap_po.shappoli.integration.trinkets.power.factory.condition.item;

import com.github.shap_po.shappoli.Shappoli;
import com.github.shap_po.shappoli.integration.trinkets.data.ShappoliTrinketsDataTypes;
import com.github.shap_po.shappoli.integration.trinkets.data.TrinketSlotData;
import com.github.shap_po.shappoli.integration.trinkets.util.TrinketsUtil;
import com.github.shap_po.shappoli.util.InventoryUtil;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.apoli.power.factory.condition.ItemConditions;
import io.github.apace100.apoli.util.Comparison;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Pair;
import net.minecraft.world.World;

import java.util.List;

public class EquippedTrinketCountCondition {
    public static boolean condition(SerializableData.Instance data, Pair<World, ItemStack> worldAndStack) {
        ItemStack stack = worldAndStack.getRight();
        Entity entity = InventoryUtil.getHolder(stack);
        if (!(entity instanceof LivingEntity livingEntity)) {
            return false;
        }

        List<TrinketSlotData> slots = TrinketSlotData.getSlots(data);

        int count = TrinketsUtil.getTrinkets(livingEntity, slots)
            .filter(trinket -> trinket.getRight().getItem().equals(stack.getItem()))
            .mapToInt(trinket -> 1).sum();
        return data.<Comparison>get("comparison").compare(count, data.getInt("compare_to"));
    }

    public static ConditionFactory<Pair<World, ItemStack>> getFactory() {
        ConditionFactory<Pair<World, ItemStack>> factory = new ConditionFactory<>(
            Shappoli.identifier("equipped_trinket_count"),
            new SerializableData()
                .add("slot", ShappoliTrinketsDataTypes.TRINKET_SLOT, null)
                .add("slots", ShappoliTrinketsDataTypes.TRINKET_SLOTS, null)
                .add("comparison", ApoliDataTypes.COMPARISON, Comparison.GREATER_THAN_OR_EQUAL)
                .add("compare_to", SerializableDataTypes.INT, 1)
            ,
            EquippedTrinketCountCondition::condition
        );

        ItemConditions.ALIASES.addPathAlias("equipped_trinket", factory.getSerializerId().getPath());
        return factory;
    }
}
