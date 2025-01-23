package com.github.shap_po.shappoli.integration.trinkets.condition.type.entity;

import com.github.shap_po.shappoli.integration.trinkets.condition.type.ShappoliTrinketsEntityConditionTypes;
import com.github.shap_po.shappoli.integration.trinkets.data.ShappoliTrinketsDataTypes;
import com.github.shap_po.shappoli.integration.trinkets.data.TrinketSlotData;
import com.github.shap_po.shappoli.integration.trinkets.util.TrinketsUtil;
import com.github.shap_po.shappoli.util.MiscUtil;
import io.github.apace100.apoli.condition.ConditionConfiguration;
import io.github.apace100.apoli.condition.context.EntityConditionContext;
import io.github.apace100.apoli.condition.type.EntityConditionType;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.data.TypedDataObjectFactory;
import io.github.apace100.apoli.util.Comparison;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TrinketSlotCountEntityConditionType extends EntityConditionType {
    public static final TypedDataObjectFactory<TrinketSlotCountEntityConditionType> DATA_FACTORY = TypedDataObjectFactory.simple(
        new SerializableData()
            .add("slot", ShappoliTrinketsDataTypes.TRINKET_SLOT, null)
            .add("slots", ShappoliTrinketsDataTypes.TRINKET_SLOT.list(), null)
            .add("comparison", ApoliDataTypes.COMPARISON, Comparison.GREATER_THAN_OR_EQUAL)
            .add("compare_to", SerializableDataTypes.INT, 1),
        data -> new TrinketSlotCountEntityConditionType(
            MiscUtil.listFromData(data, "slot", "slots"),
            data.get("comparison"),
            data.getInt("compare_to")
        ),
        (conditionType, serializableData) -> serializableData.instance()
            .set("slots", conditionType.slots)
            .set("comparison", conditionType.comparison)
            .set("compare_to", conditionType.compareTo)
    );

    private final List<TrinketSlotData> slots;
    private final Comparison comparison;
    private final int compareTo;

    public TrinketSlotCountEntityConditionType(List<TrinketSlotData> slots, Comparison comparison, int compareTo) {
        this.slots = slots;
        this.comparison = comparison;
        this.compareTo = compareTo;
    }

    @Override
    public boolean test(EntityConditionContext context) {
        if (!(context.entity() instanceof LivingEntity livingEntity)) {
            return false;
        }

        int count = TrinketsUtil.getSlots(livingEntity, slots).mapToInt(slot -> 1).sum();
        return comparison.compare(count, compareTo);
    }

    @Override
    public @NotNull ConditionConfiguration<?> getConfig() {
        return ShappoliTrinketsEntityConditionTypes.TRINKET_SLOT_COUNT;
    }
}
