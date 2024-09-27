package com.github.shap_po.shappoli.integration.trinkets.power;

import com.github.shap_po.shappoli.Shappoli;
import com.github.shap_po.shappoli.integration.trinkets.data.ShappoliTrinketsDataTypes;
import com.github.shap_po.shappoli.integration.trinkets.data.TrinketSlotData;
import com.github.shap_po.shappoli.integration.trinkets.util.TrinketsUtil;
import dev.emi.trinkets.api.SlotReference;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.factory.PowerTypeFactory;
import io.github.apace100.apoli.power.type.PowerType;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Pair;
import net.minecraft.world.World;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public abstract class BasePreventTrinketChangePower extends PowerType {
    protected final Predicate<Pair<World, ItemStack>> itemCondition;
    protected final List<TrinketSlotData> slots;
    protected final boolean allowCreative;

    public BasePreventTrinketChangePower(
        Power power,
        LivingEntity entity,
        Predicate<Pair<World, ItemStack>> itemCondition,
        List<TrinketSlotData> slots,
        boolean allowInCreative
    ) {
        super(power, entity);
        this.itemCondition = itemCondition;
        this.slots = slots;
        this.allowCreative = allowInCreative;
    }

    public boolean doesApply(LivingEntity actor, SlotReference slotReference, ItemStack item) {
        if ((actor instanceof PlayerEntity player) && player.isCreative() && allowCreative) {
            return false;
        }
        return (
            (slots.isEmpty() || slots.stream().anyMatch(slot -> slot.test(slotReference))) &&
                (itemCondition == null || itemCondition.test(TrinketsUtil.getItemConditionPair(actor, item)))
        );
    }

    @FunctionalInterface
    public interface Factory {
        BasePreventTrinketChangePower create(
            Power type,
            LivingEntity entity,
            Predicate<Pair<World, ItemStack>> itemCondition,
            List<TrinketSlotData> slots,
            boolean allowInCreative
        );
    }

    public static PowerTypeFactory createFactory(String identifier, Factory serializerFactory) {
        Objects.requireNonNull(serializerFactory);
        return new PowerTypeFactory<>(
            Shappoli.identifier(identifier),
            new SerializableData()
                .add("item_condition", ApoliDataTypes.ITEM_CONDITION, null)
                .add("slot", ShappoliTrinketsDataTypes.TRINKET_SLOT, null)
                .add("slots", ShappoliTrinketsDataTypes.TRINKET_SLOTS, null)
                .add("allow_in_creative", SerializableDataTypes.BOOLEAN, true),
            data -> (type, player) ->
                serializerFactory.create(
                    type,
                    player,
                    data.get("item_condition"),
                    TrinketSlotData.getSlots(data),
                    data.getBoolean("allow_in_creative")
                )
        ).allowCondition();
    }
}
