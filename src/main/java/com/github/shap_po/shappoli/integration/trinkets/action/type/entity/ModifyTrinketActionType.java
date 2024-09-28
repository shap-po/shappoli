package com.github.shap_po.shappoli.integration.trinkets.action.type.entity;

import com.github.shap_po.shappoli.Shappoli;
import com.github.shap_po.shappoli.integration.trinkets.data.ShappoliTrinketsDataTypes;
import com.github.shap_po.shappoli.integration.trinkets.data.TrinketSlotData;
import com.github.shap_po.shappoli.integration.trinkets.util.TrinketsUtil;
import dev.emi.trinkets.api.SlotReference;
import io.github.apace100.apoli.action.factory.ActionTypeFactory;
import io.github.apace100.apoli.action.factory.EntityActions;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.util.InventoryUtil;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Pair;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class ModifyTrinketActionType {
    public static void action(
        Entity entity,
        List<TrinketSlotData> slots,
        Function<ItemStack, Integer> processor,
        int limit,
        @Nullable Consumer<Entity> entityAction,
        Consumer<Pair<World, StackReference>> itemAction,
        @Nullable Predicate<Pair<World, ItemStack>> itemCondition
    ) {
        if (!(entity instanceof LivingEntity livingEntity)) {
            return;
        }

        int processedItems = 0;
        modifyingItemsLoop:
        for (Iterator<Pair<SlotReference, ItemStack>> iter = TrinketsUtil.getTrinkets(livingEntity, slots, itemCondition).iterator(); iter.hasNext(); ) {
            Pair<SlotReference, ItemStack> trinket = iter.next();

            int amount = processor.apply(trinket.getRight());
            for (int i = 0; i < amount; i++) {
                if (entityAction != null) {
                    entityAction.accept(entity);
                }

                itemAction.accept(TrinketsUtil.getItemActionPair(livingEntity, trinket.getLeft()));
                ++processedItems;

                if (limit > 0 && processedItems >= limit) {
                    break modifyingItemsLoop;
                }
            }
        }
    }

    public static ActionTypeFactory<Entity> getFactory() {
        ActionTypeFactory<Entity> factory = new ActionTypeFactory<>(
            Shappoli.identifier("modify_trinket"),
            new SerializableData()
                .add("slot", ShappoliTrinketsDataTypes.TRINKET_SLOT, null)
                .add("slots", ShappoliTrinketsDataTypes.TRINKET_SLOTS, null)
                .add("process_mode", ApoliDataTypes.PROCESS_MODE, InventoryUtil.ProcessMode.STACKS)
                .add("limit", SerializableDataTypes.INT, 0)
                .add("entity_action", ApoliDataTypes.ENTITY_ACTION, null)
                .add("item_action", ApoliDataTypes.ITEM_ACTION)
                .add("item_condition", ApoliDataTypes.ITEM_CONDITION, null)
            ,
            (data, entity) -> action(
                entity,
                TrinketSlotData.getSlots(data),
                data.get("process_mode"),
                data.getInt("limit"),
                data.get("entity_action"),
                data.get("item_action"),
                data.get("item_condition")
            )
        );

        EntityActions.ALIASES.addPathAlias("modify_trinkets", factory.getSerializerId().getPath());
        return factory;
    }
}
