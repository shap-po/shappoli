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

import java.util.List;

public class TrinketSlotCountConditionType {
    public static boolean condition(
        Entity entity,
        List<TrinketSlotData> slots,
        Comparison comparison, int compareTo
    ) {
        if (!(entity instanceof LivingEntity livingEntity)) {
            return false;
        }

        int count = TrinketsUtil.getSlots(livingEntity, slots).mapToInt(slot -> 1).sum();
        return comparison.compare(count, compareTo);
    }

    public static ConditionTypeFactory<Entity> getFactory() {
        ConditionTypeFactory<Entity> factory = new ConditionTypeFactory<>(
            Shappoli.identifier("trinket_slot_count"),
            new SerializableData()
                .add("slot", ShappoliTrinketsDataTypes.TRINKET_SLOT, null)
                .add("slots", ShappoliTrinketsDataTypes.TRINKET_SLOTS, null)
                .add("comparison", ApoliDataTypes.COMPARISON, Comparison.GREATER_THAN)
                .add("compare_to", SerializableDataTypes.INT, 0)
            ,
            (data, entity) -> condition(
                entity,
                TrinketSlotData.getSlots(data),
                data.get("comparison"),
                data.getInt("compare_to")
            )
        );

        EntityConditionTypes.ALIASES.addPathAlias("trinket_slots_count", factory.getSerializerId().getPath());
        return factory;
    }
}
