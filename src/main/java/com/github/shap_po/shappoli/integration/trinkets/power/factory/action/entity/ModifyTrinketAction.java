package com.github.shap_po.shappoli.integration.trinkets.power.factory.action.entity;

import com.github.shap_po.shappoli.Shappoli;
import com.github.shap_po.shappoli.integration.trinkets.data.ShappoliTrinketsDataTypes;
import com.github.shap_po.shappoli.integration.trinkets.data.TrinketSlotData;
import com.github.shap_po.shappoli.integration.trinkets.util.TrinketsUtil;
import dev.emi.trinkets.api.SlotReference;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.apoli.power.factory.action.EntityActions;
import io.github.apace100.apoli.util.InventoryUtil;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Pair;
import net.minecraft.world.World;

import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class ModifyTrinketAction {
    public static void action(SerializableData.Instance data, Entity entity) {
        if (!(entity instanceof LivingEntity livingEntity)) {
            return;
        }

        List<TrinketSlotData> slots = TrinketSlotData.getSlots(data);
        Function<ItemStack, Integer> processor = data.<InventoryUtil.ProcessMode>get("process_mode").getProcessor();
        int limit = data.getInt("limit");
        Consumer<Entity> entityAction = data.get("entity_action");
        Consumer<Pair<World, StackReference>> itemAction = data.get("item_action");
        Predicate<Pair<World, ItemStack>> itemCondition = data.get("item_condition");

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

    public static ActionFactory<Entity> getFactory() {
        ActionFactory<Entity> factory = new ActionFactory<>(
            Shappoli.identifier("modify_trinket"),
            new SerializableData()
                .add("slot", ShappoliTrinketsDataTypes.TRINKET_SLOT, null)
                .add("slots", ShappoliTrinketsDataTypes.TRINKET_SLOTS, null)
                .add("process_mode", ApoliDataTypes.PROCESS_MODE, InventoryUtil.ProcessMode.STACKS)
                .add("entity_action", ApoliDataTypes.ENTITY_ACTION, null)
                .add("item_action", ApoliDataTypes.ITEM_ACTION)
                .add("item_condition", ApoliDataTypes.ITEM_CONDITION, null)
                .add("limit", SerializableDataTypes.INT, 0)
            ,
            ModifyTrinketAction::action
        );

        EntityActions.ALIASES.addPathAlias("modify_trinkets", factory.getSerializerId().getPath());
        return factory;
    }
}
