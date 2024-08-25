package com.github.shap_po.shappoli.power.factory.condition.entity;

import com.github.shap_po.shappoli.Shappoli;
import com.github.shap_po.shappoli.data.ShappoliDataTypes;
import com.github.shap_po.shappoli.data.TrinketSlotData;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.calio.data.SerializableData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Pair;
import net.minecraft.world.World;

import java.util.List;
import java.util.function.Predicate;

public class EquippedTrinketCondition {
    public static boolean condition(SerializableData.Instance data, Entity entity) {
        if (!(entity instanceof LivingEntity livingEntity)) {
            return false;
        }

        Predicate<Pair<World, ItemStack>> itemCondition = data.get("item_condition");
        List<TrinketSlotData> slots = TrinketSlotData.getSlots(data);

        return TrinketSlotData.getTrinkets(livingEntity, slots, itemCondition).anyMatch(pair -> true);
    }

    public static ConditionFactory<Entity> getFactory() {
        return new ConditionFactory<>(
            Shappoli.identifier("equipped_trinket"),
            new SerializableData()
                .add("item_condition", ApoliDataTypes.ITEM_CONDITION, null)
                .add("slot", ShappoliDataTypes.TRINKET_SLOT, null)
                .add("slots", ShappoliDataTypes.TRINKET_SLOTS, null)
            ,
            EquippedTrinketCondition::condition
        );
    }
}
