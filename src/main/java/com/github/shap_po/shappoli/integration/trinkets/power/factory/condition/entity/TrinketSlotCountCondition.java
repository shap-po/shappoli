package com.github.shap_po.shappoli.integration.trinkets.power.factory.condition.entity;

import com.github.shap_po.shappoli.Shappoli;
import com.github.shap_po.shappoli.integration.trinkets.data.ShappoliTrinketsDataTypes;
import com.github.shap_po.shappoli.integration.trinkets.data.TrinketSlotData;
import com.github.shap_po.shappoli.integration.trinkets.util.TrinketsUtil;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.apoli.power.factory.condition.EntityConditions;
import io.github.apace100.apoli.util.Comparison;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;

public class TrinketSlotCountCondition {
    public static boolean condition(SerializableData.Instance data, Entity entity) {
        if (!(entity instanceof LivingEntity livingEntity)) {
            return false;
        }

        int count = TrinketsUtil.getSlots(livingEntity, TrinketSlotData.getSlots(data)).mapToInt(slot -> 1).sum();
        return data.<Comparison>get("comparison").compare(count, data.getInt("compare_to"));
    }

    public static ConditionFactory<Entity> getFactory() {
        ConditionFactory<Entity> factory = new ConditionFactory<>(
            Shappoli.identifier("trinket_slot_count"),
            new SerializableData()
                .add("slot", ShappoliTrinketsDataTypes.TRINKET_SLOT, null)
                .add("slots", ShappoliTrinketsDataTypes.TRINKET_SLOTS, null)
                .add("comparison", ApoliDataTypes.COMPARISON, Comparison.GREATER_THAN)
                .add("compare_to", SerializableDataTypes.INT, 0)
            ,
            TrinketSlotCountCondition::condition
        );

        EntityConditions.ALIASES.addPathAlias("trinket_slots_count", factory.getSerializerId().getPath());
        return factory;
    }
}
