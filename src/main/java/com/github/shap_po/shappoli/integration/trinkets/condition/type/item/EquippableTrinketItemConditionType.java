package com.github.shap_po.shappoli.integration.trinkets.condition.type.item;

import com.github.shap_po.shappoli.integration.trinkets.condition.type.ShappoliTrinketsItemConditionTypes;
import com.github.shap_po.shappoli.integration.trinkets.data.ShappoliTrinketsDataTypes;
import com.github.shap_po.shappoli.integration.trinkets.data.TrinketSlotData;
import com.github.shap_po.shappoli.integration.trinkets.util.TrinketsUtil;
import com.github.shap_po.shappoli.util.InventoryUtil;
import com.github.shap_po.shappoli.util.MiscUtil;
import dev.emi.trinkets.TrinketSlot;
import io.github.apace100.apoli.condition.ConditionConfiguration;
import io.github.apace100.apoli.condition.type.ItemConditionType;
import io.github.apace100.apoli.data.TypedDataObjectFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class EquippableTrinketItemConditionType extends ItemConditionType {
    public static final TypedDataObjectFactory<EquippableTrinketItemConditionType> DATA_FACTORY = TypedDataObjectFactory.simple(
        new SerializableData()
            .add("slot", ShappoliTrinketsDataTypes.TRINKET_SLOT, null)
            .add("slots", ShappoliTrinketsDataTypes.TRINKET_SLOT.list(), null)
            .add("only_empty", SerializableDataTypes.BOOLEAN, false),
        data -> new EquippableTrinketItemConditionType(
            MiscUtil.listFromData(data, "slot", "slots"),
            data.getBoolean("only_empty")
        ),
        (conditionType, serializableData) -> serializableData.instance()
            .set("slots", conditionType.slots)
            .set("only_empty", conditionType.onlyEmpty)
    );

    private final List<TrinketSlotData> slots;
    private final boolean onlyEmpty;

    public EquippableTrinketItemConditionType(List<TrinketSlotData> slots, boolean onlyEmpty) {
        this.slots = slots;
        this.onlyEmpty = onlyEmpty;
    }

    @Override
    public boolean test(World world, ItemStack stack) {
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

    @Override
    public @NotNull ConditionConfiguration<?> getConfig() {
        return ShappoliTrinketsItemConditionTypes.EQUIPPABLE_TRINKET;
    }
}
