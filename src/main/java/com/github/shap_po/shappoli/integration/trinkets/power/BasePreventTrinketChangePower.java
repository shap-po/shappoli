package com.github.shap_po.shappoli.integration.trinkets.power;

import com.github.shap_po.shappoli.Shappoli;
import com.github.shap_po.shappoli.integration.trinkets.data.ShappoliTrinketsDataTypes;
import com.github.shap_po.shappoli.integration.trinkets.data.TrinketSlotData;
import com.github.shap_po.shappoli.integration.trinkets.util.TrinketsUtil;
import dev.emi.trinkets.api.SlotReference;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Pair;
import net.minecraft.world.World;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public abstract class BasePreventTrinketChangePower extends Power {
    protected final Predicate<Entity> entityCondition;
    protected final Predicate<Pair<World, ItemStack>> itemCondition;
    protected final List<TrinketSlotData> slots;
    protected final boolean allowCreative;

    public BasePreventTrinketChangePower(
        PowerType<?> type,
        LivingEntity entity,
        Predicate<Entity> entityCondition,
        Predicate<Pair<World, ItemStack>> itemCondition,
        List<TrinketSlotData> slots,
        boolean allowInCreative
    ) {
        super(type, entity);
        this.entityCondition = entityCondition;
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
                (entityCondition == null || entityCondition.test(actor)) &&
                (itemCondition == null || itemCondition.test(TrinketsUtil.getItemConditionPair(actor, item)))
        );
    }

    @FunctionalInterface
    public interface Factory {
        BasePreventTrinketChangePower create(
            PowerType<?> type,
            LivingEntity entity,
            Predicate<Entity> entityCondition,
            Predicate<Pair<World, ItemStack>> itemCondition,
            List<TrinketSlotData> slots,
            boolean allowInCreative
        );
    }


    public static PowerFactory createFactory(String identifier, Factory serializerFactory) {
        Objects.requireNonNull(serializerFactory);
        return new PowerFactory<>(
            Shappoli.identifier(identifier),
            new SerializableData()
                .add("entity_condition", ApoliDataTypes.ENTITY_CONDITION, null)
                .add("item_condition", ApoliDataTypes.ITEM_CONDITION, null)
                .add("slot", ShappoliTrinketsDataTypes.TRINKET_SLOT, null)
                .add("slots", ShappoliTrinketsDataTypes.TRINKET_SLOTS, null)
                .add("allow_in_creative", SerializableDataTypes.BOOLEAN, true),
            data -> (type, player) ->
                serializerFactory.create(
                    type,
                    player,
                    data.get("entity_condition"),
                    data.get("item_condition"),
                    TrinketSlotData.getSlots(data),
                    data.getBoolean("allow_in_creative")
                )
        ).allowCondition();
    }
}
