package com.github.shap_po.shappoli.integration.trinkets.condition.type.item;

import com.github.shap_po.shappoli.integration.trinkets.condition.type.ShappoliTrinketsItemConditionTypes;
import com.github.shap_po.shappoli.integration.trinkets.data.ShappoliTrinketsDataTypes;
import com.github.shap_po.shappoli.integration.trinkets.data.TrinketSlotData;
import com.github.shap_po.shappoli.integration.trinkets.util.TrinketsUtil;
import com.github.shap_po.shappoli.util.InventoryUtil;
import com.github.shap_po.shappoli.util.MiscUtil;
import io.github.apace100.apoli.condition.ConditionConfiguration;
import io.github.apace100.apoli.condition.type.ItemConditionType;
import io.github.apace100.apoli.data.TypedDataObjectFactory;
import io.github.apace100.calio.data.SerializableData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class EquippedTrinketItemConditionType extends ItemConditionType {
    public static final TypedDataObjectFactory<EquippedTrinketItemConditionType> DATA_FACTORY = TypedDataObjectFactory.simple(
        new SerializableData()
            .add("slot", ShappoliTrinketsDataTypes.TRINKET_SLOT, null)
            .add("slots", ShappoliTrinketsDataTypes.TRINKET_SLOT.list(), null),
        data -> new EquippedTrinketItemConditionType(
            MiscUtil.listFromData(data, "slot", "slots")
        ),
        (conditionType, serializableData) -> serializableData.instance()
            .set("slots", conditionType.slots)
    );

    private final List<TrinketSlotData> slots;

    public EquippedTrinketItemConditionType(List<TrinketSlotData> slots) {
        this.slots = slots;
    }

    @Override
    public boolean test(World world, ItemStack stack) {
        Entity entity = InventoryUtil.getHolder(stack);
        if (!(entity instanceof LivingEntity livingEntity)) {
            return false;
        }

        return TrinketsUtil.getTrinkets(livingEntity, slots)
            .anyMatch(trinket -> trinket.getRight().equals(stack));
    }

    @Override
    public @NotNull ConditionConfiguration<?> getConfig() {
        return ShappoliTrinketsItemConditionTypes.EQUIPPED_TRINKET;
    }
}
