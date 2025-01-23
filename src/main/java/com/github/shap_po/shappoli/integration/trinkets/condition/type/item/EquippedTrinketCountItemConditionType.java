package com.github.shap_po.shappoli.integration.trinkets.condition.type.item;

import com.github.shap_po.shappoli.integration.trinkets.condition.type.ShappoliTrinketsItemConditionTypes;
import com.github.shap_po.shappoli.integration.trinkets.data.ShappoliTrinketsDataTypes;
import com.github.shap_po.shappoli.integration.trinkets.data.TrinketSlotFilter;
import com.github.shap_po.shappoli.integration.trinkets.util.TrinketsUtil;
import com.github.shap_po.shappoli.util.InventoryUtil;
import com.github.shap_po.shappoli.util.MiscUtil;
import io.github.apace100.apoli.condition.ConditionConfiguration;
import io.github.apace100.apoli.condition.context.ItemConditionContext;
import io.github.apace100.apoli.condition.type.ItemConditionType;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.data.TypedDataObjectFactory;
import io.github.apace100.apoli.util.Comparison;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class EquippedTrinketCountItemConditionType extends ItemConditionType {
    public static final TypedDataObjectFactory<EquippedTrinketCountItemConditionType> DATA_FACTORY = TypedDataObjectFactory.simple(
        new SerializableData()
            .add("slot", ShappoliTrinketsDataTypes.TRINKET_SLOT, null)
            .add("slots", ShappoliTrinketsDataTypes.TRINKET_SLOT.list(), null)
            .add("comparison", ApoliDataTypes.COMPARISON, Comparison.GREATER_THAN_OR_EQUAL)
            .add("compare_to", SerializableDataTypes.INT, 1),
        data -> new EquippedTrinketCountItemConditionType(
            MiscUtil.listFromData(data, "slot", "slots"),
            data.get("comparison"),
            data.getInt("compare_to")
        ),
        (conditionType, serializableData) -> serializableData.instance()
            .set("slots", conditionType.slots)
            .set("comparison", conditionType.comparison)
            .set("compare_to", conditionType.compareTo)
    );

    private final List<TrinketSlotFilter> slots;
    private final Comparison comparison;
    private final int compareTo;

    public EquippedTrinketCountItemConditionType(List<TrinketSlotFilter> slots, Comparison comparison, int compareTo) {
        this.slots = slots;
        this.comparison = comparison;
        this.compareTo = compareTo;
    }

    @Override
    public boolean test(ItemConditionContext context) {
        Entity entity = InventoryUtil.getHolder(context.stack());
        if (!(entity instanceof LivingEntity livingEntity)) {
            return false;
        }

        int count = TrinketsUtil.getTrinkets(livingEntity, slots)
            .filter(trinket -> trinket.getRight().getItem().equals(context.stack().getItem()))
            .mapToInt(trinket -> 1).sum();
        return comparison.compare(count, compareTo);
    }

    @Override
    public @NotNull ConditionConfiguration<?> getConfig() {
        return ShappoliTrinketsItemConditionTypes.EQUIPPED_TRINKET_COUNT;
    }
}
