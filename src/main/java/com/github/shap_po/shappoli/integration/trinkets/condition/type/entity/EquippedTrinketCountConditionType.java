package com.github.shap_po.shappoli.integration.trinkets.condition.type.entity;

import com.github.shap_po.shappoli.Shappoli;
import com.github.shap_po.shappoli.integration.trinkets.data.ShappoliTrinketsDataTypes;
import com.github.shap_po.shappoli.integration.trinkets.data.TrinketSlotData;
import com.github.shap_po.shappoli.integration.trinkets.util.TrinketsUtil;
import io.github.apace100.apoli.condition.factory.ConditionTypeFactory;
import io.github.apace100.apoli.condition.type.EntityConditionTypes;
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
import java.util.function.Predicate;

public class EquippedTrinketCountConditionType {
    public static boolean condition(
        Entity entity,
        Predicate<Pair<World, ItemStack>> itemCondition,
        List<TrinketSlotData> slots,
        Comparison comparison, int compareTo
    ) {
        if (!(entity instanceof LivingEntity livingEntity)) {
            return false;
        }

        int count = TrinketsUtil.getTrinkets(livingEntity, slots, itemCondition)
            .mapToInt(trinket -> 1)
            .sum();
        return comparison.compare(count, compareTo);
    }

    public static ConditionTypeFactory<Entity> getFactory() {
        ConditionTypeFactory<Entity> factory = new ConditionTypeFactory<>(
            Shappoli.identifier("equipped_trinket_count"),
            new SerializableData()
                .add("item_condition", ApoliDataTypes.ITEM_CONDITION)
                .add("slot", ShappoliTrinketsDataTypes.TRINKET_SLOT, null)
                .add("slots", ShappoliTrinketsDataTypes.TRINKET_SLOTS, null)
                .add("comparison", ApoliDataTypes.COMPARISON, Comparison.GREATER_THAN_OR_EQUAL)
                .add("compare_to", SerializableDataTypes.INT, 1)
            ,
            (data, entity) -> condition(
                entity,
                data.get("item_condition"),
                TrinketSlotData.getSlots(data),
                data.get("comparison"),
                data.getInt("compare_to")
            )
        );

        EntityConditionTypes.ALIASES.addPathAlias("equipped_trinket", factory.getSerializerId().getPath());
        return factory;
    }
}
