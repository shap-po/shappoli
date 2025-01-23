package com.github.shap_po.shappoli.integration.trinkets.power.type;

import com.github.shap_po.shappoli.integration.trinkets.data.ShappoliTrinketsDataTypes;
import com.github.shap_po.shappoli.integration.trinkets.data.TrinketSlotFilter;
import com.github.shap_po.shappoli.integration.trinkets.util.TrinketsUtil;
import com.github.shap_po.shappoli.util.MiscUtil;
import dev.emi.trinkets.api.SlotReference;
import io.github.apace100.apoli.condition.EntityCondition;
import io.github.apace100.apoli.condition.ItemCondition;
import io.github.apace100.apoli.data.TypedDataObjectFactory;
import io.github.apace100.apoli.power.type.PowerType;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

import java.util.List;
import java.util.Optional;

public abstract class AbstractPreventTrinketChangePowerType extends PowerType {
    protected final Optional<ItemCondition> itemCondition;
    protected final List<TrinketSlotFilter> slots;
    protected final boolean allowInCreative;

    public AbstractPreventTrinketChangePowerType(
        Optional<ItemCondition> itemCondition,
        List<TrinketSlotFilter> slots,
        boolean allowInCreative,
        Optional<EntityCondition> condition
    ) {
        super(condition);
        this.itemCondition = itemCondition;
        this.slots = slots;
        this.allowInCreative = allowInCreative;
    }

    public boolean doesApply(LivingEntity entity, SlotReference slotReference, ItemStack item) {
        if ((entity instanceof PlayerEntity player) && player.isCreative() && allowInCreative) {
            return false;
        }
        return (slots.isEmpty() || slots.stream().anyMatch(slot -> slot.test(slotReference))) &&
            itemCondition.map(condition -> condition.test(TrinketsUtil.getItemConditionContext(entity, item))).orElse(true);
    }

    public Optional<ItemCondition> getItemCondition() {
        return itemCondition;
    }

    public List<TrinketSlotFilter> getSlots() {
        return slots;
    }

    public boolean isAllowInCreative() {
        return allowInCreative;
    }

    @FunctionalInterface
    public interface Constructor<T extends AbstractPreventTrinketChangePowerType> {
        T create(
            Optional<ItemCondition> itemCondition,
            List<TrinketSlotFilter> slots,
            boolean allowInCreative,
            Optional<EntityCondition> condition
        );
    }

    protected static <T extends AbstractPreventTrinketChangePowerType> TypedDataObjectFactory<T> createDataFactory(Constructor<T> constructor) {
        return PowerType.createConditionedDataFactory(
            new SerializableData()
                .add("item_condition", ItemCondition.DATA_TYPE.optional(), Optional.empty())
                .add("slot", ShappoliTrinketsDataTypes.TRINKET_SLOT, null)
                .add("slots", ShappoliTrinketsDataTypes.TRINKET_SLOT.list(), null)
                .add("allow_in_creative", SerializableDataTypes.BOOLEAN, false),
            (data, condition) -> constructor.create(
                data.get("item_condition"),
                MiscUtil.listFromData(data, "slot", "slots"),
                data.get("allow_in_creative"),
                condition
            ),
            (powerType, serializableData) -> serializableData.instance()
                .set("item_condition", powerType.getItemCondition())
                .set("slots", powerType.getSlots())
                .set("allow_in_creative", powerType.isAllowInCreative())
        );
    }
}
