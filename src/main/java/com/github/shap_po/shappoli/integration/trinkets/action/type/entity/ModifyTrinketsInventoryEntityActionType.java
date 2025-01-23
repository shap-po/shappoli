package com.github.shap_po.shappoli.integration.trinkets.action.type.entity;

import com.github.shap_po.shappoli.integration.trinkets.action.type.ShappoliTrinketsEntityActionTypes;
import com.github.shap_po.shappoli.integration.trinkets.data.ShappoliTrinketsDataTypes;
import com.github.shap_po.shappoli.integration.trinkets.data.TrinketSlotData;
import com.github.shap_po.shappoli.integration.trinkets.util.TrinketsUtil;
import com.github.shap_po.shappoli.util.MiscUtil;
import dev.emi.trinkets.api.SlotReference;
import io.github.apace100.apoli.action.ActionConfiguration;
import io.github.apace100.apoli.action.EntityAction;
import io.github.apace100.apoli.action.ItemAction;
import io.github.apace100.apoli.action.context.EntityActionContext;
import io.github.apace100.apoli.action.type.EntityActionType;
import io.github.apace100.apoli.condition.ItemCondition;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.data.TypedDataObjectFactory;
import io.github.apace100.apoli.util.InventoryUtil;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class ModifyTrinketsInventoryEntityActionType extends EntityActionType {
    public static final TypedDataObjectFactory<ModifyTrinketsInventoryEntityActionType> DATA_FACTORY = TypedDataObjectFactory.simple(
        new SerializableData()
            .add("slot", ShappoliTrinketsDataTypes.TRINKET_SLOT, null)
            .add("slots", ShappoliTrinketsDataTypes.TRINKET_SLOT.list(), null)
            .add("process_mode", ApoliDataTypes.PROCESS_MODE, InventoryUtil.ProcessMode.STACKS)
            .add("limit", SerializableDataTypes.INT, 0)
            .add("entity_action", EntityAction.DATA_TYPE.optional(), Optional.empty())
            .add("item_action", ItemAction.DATA_TYPE.optional(), Optional.empty())
            .add("item_condition", ItemCondition.DATA_TYPE.optional(), Optional.empty()),
        data -> new ModifyTrinketsInventoryEntityActionType(
            MiscUtil.listFromData(data, "slot", "slots"),
            data.get("process_mode"),
            data.getInt("limit"),
            data.get("entity_action"),
            data.get("item_action"),
            data.get("item_condition")
        ),
        (actionType, serializableData) -> serializableData.instance()
            .set("slots", actionType.slots)
            .set("process_mode", actionType.processMode)
            .set("limit", actionType.limit)
            .set("entity_action", actionType.entityAction)
            .set("item_action", actionType.itemAction)
            .set("item_condition", actionType.itemCondition)
    );

    private final List<TrinketSlotData> slots;
    private final InventoryUtil.ProcessMode processMode;
    private final int limit;
    private final Optional<EntityAction> entityAction;
    private final Optional<ItemAction> itemAction;
    private final Optional<ItemCondition> itemCondition;

    public ModifyTrinketsInventoryEntityActionType(
        List<TrinketSlotData> slots,
        InventoryUtil.ProcessMode processMode,
        int limit,
        Optional<EntityAction> entityAction,
        Optional<ItemAction> itemAction,
        Optional<ItemCondition> itemCondition
    ) {
        this.slots = slots;
        this.processMode = processMode;
        this.limit = limit;
        this.entityAction = entityAction;
        this.itemAction = itemAction;
        this.itemCondition = itemCondition;
    }

    public boolean modify(Entity entity) {
        if (!(entity instanceof LivingEntity livingEntity)) {
            return false;
        }

        int processedItems = 0;
        modifyingItemsLoop:
        for (Iterator<Pair<SlotReference, ItemStack>> iter = TrinketsUtil.getTrinkets(livingEntity, slots, itemCondition).iterator(); iter.hasNext(); ) {
            Pair<SlotReference, ItemStack> trinket = iter.next();

            int amount = processMode.applyAsInt(trinket.getRight());
            for (int i = 0; i < amount; i++) {
                entityAction.ifPresent(entityAction -> entityAction.execute(entity));
                itemAction.ifPresent(itemAction -> itemAction.accept(TrinketsUtil.getItemActionContext(livingEntity, trinket.getLeft())));

                ++processedItems;

                if (limit > 0 && processedItems >= limit) {
                    break modifyingItemsLoop;
                }
            }
        }

        return processedItems > 0;
    }

    @Override
    public void accept(EntityActionContext context) {
        modify(context.entity());
    }

    @Override
    public @NotNull ActionConfiguration<?> getConfig() {
        return ShappoliTrinketsEntityActionTypes.MODIFY_TRINKETS_INVENTORY;
    }
}
