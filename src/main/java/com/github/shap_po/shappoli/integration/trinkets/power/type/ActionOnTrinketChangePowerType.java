package com.github.shap_po.shappoli.integration.trinkets.power.type;

import com.github.shap_po.shappoli.integration.trinkets.access.SyncingTrinketInventory;
import com.github.shap_po.shappoli.integration.trinkets.data.ShappoliTrinketsDataTypes;
import com.github.shap_po.shappoli.integration.trinkets.data.TrinketSlotData;
import com.github.shap_po.shappoli.integration.trinkets.util.TrinketsUtil;
import com.github.shap_po.shappoli.util.MiscUtil;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketInventory;
import io.github.apace100.apoli.action.EntityAction;
import io.github.apace100.apoli.action.ItemAction;
import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.condition.EntityCondition;
import io.github.apace100.apoli.condition.ItemCondition;
import io.github.apace100.apoli.data.TypedDataObjectFactory;
import io.github.apace100.apoli.power.PowerConfiguration;
import io.github.apace100.apoli.power.type.PowerType;
import io.github.apace100.calio.data.SerializableData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class ActionOnTrinketChangePowerType extends PowerType {
    public static final TypedDataObjectFactory<ActionOnTrinketChangePowerType> DATA_FACTORY = PowerType.createConditionedDataFactory(
        new SerializableData()
            .add("entity_action_on_equip", EntityAction.DATA_TYPE.optional(), Optional.empty())
            .add("item_action_on_equip", ItemAction.DATA_TYPE.optional(), Optional.empty())
            .add("entity_action_on_unequip", EntityAction.DATA_TYPE.optional(), Optional.empty())
            .add("item_action_on_unequip", ItemAction.DATA_TYPE.optional(), Optional.empty())
            .add("item_condition", ItemCondition.DATA_TYPE)
            .add("slot", ShappoliTrinketsDataTypes.TRINKET_SLOT, null)
            .add("slots", ShappoliTrinketsDataTypes.TRINKET_SLOT.list(), null)
            .validate(data -> MiscUtil.checkAtLeastOneFieldExists(data, "entity_action_on_equip", "item_action_on_equip", "entity_action_on_unequip", "item_action_on_unequip")),
        (data, condition) -> new ActionOnTrinketChangePowerType(
            data.get("entity_action_on_equip"),
            data.get("item_action_on_equip"),
            data.get("entity_action_on_unequip"),
            data.get("item_action_on_unequip"),
            data.get("item_condition"),
            MiscUtil.listFromData(data, "slot", "slots"),
            condition
        ),
        (powerType, serializableData) -> serializableData.instance()
            .set("entity_action_on_equip", powerType.entityActionOnEquip)
            .set("item_action_on_equip", powerType.itemActionOnEquip)
            .set("entity_action_on_unequip", powerType.entityActionOnUnequip)
            .set("item_action_on_unequip", powerType.itemActionOnUnequip)
            .set("item_condition", powerType.itemCondition)
            .set("slots", powerType.slots)
    );

    private final Optional<EntityAction> entityActionOnEquip;
    private final Optional<ItemAction> itemActionOnEquip;
    private final Optional<EntityAction> entityActionOnUnequip;
    private final Optional<ItemAction> itemActionOnUnequip;
    private final ItemCondition itemCondition;
    private final List<TrinketSlotData> slots;

    public ActionOnTrinketChangePowerType(
        Optional<EntityAction> entityActionOnEquip,
        Optional<ItemAction> itemActionOnEquip,
        Optional<EntityAction> entityActionOnUnequip,
        Optional<ItemAction> itemActionOnUnequip,
        ItemCondition itemCondition,
        List<TrinketSlotData> slots,
        Optional<EntityCondition> condition
    ) {
        super(condition);
        this.entityActionOnEquip = entityActionOnEquip;
        this.itemActionOnEquip = itemActionOnEquip;
        this.entityActionOnUnequip = entityActionOnUnequip;
        this.itemActionOnUnequip = itemActionOnUnequip;
        this.itemCondition = itemCondition;
        this.slots = slots;
    }

    public boolean doesApply(SlotReference slotReference, ItemStack item) {
        return ((slots.isEmpty() || slots.stream().anyMatch(slot -> slot.test(slotReference))) &&
            itemCondition.test(TrinketsUtil.getItemConditionContext(getHolder(), item)));
    }

    public void apply(SlotReference slotReference, boolean isEquipping) {
        Entity entity = getHolder();
        if (isEquipping) {
            entityActionOnEquip.ifPresent(entityAction -> entityAction.execute(entity));
            itemActionOnEquip.ifPresent(itemAction -> itemAction.accept(TrinketsUtil.getItemActionContext(entity, slotReference)));
        } else {
            entityActionOnUnequip.ifPresent(entityAction -> entityAction.execute(entity));
            itemActionOnUnequip.ifPresent(itemAction -> itemAction.accept(TrinketsUtil.getItemActionContext(entity, slotReference)));
        }
    }

    public static void handleTrinketChange(LivingEntity entity, TrinketInventory inventory, ItemStack stack, int slot, boolean isEquipping) {
        if (entity.getWorld().isClient || ((SyncingTrinketInventory) inventory).shappoli$isSyncing()) {
            return;
        }

        SlotReference ref = new SlotReference(inventory, slot);
        PowerHolderComponent.withPowerTypes(entity, ActionOnTrinketChangePowerType.class,
            p -> p.doesApply(ref, stack),
            p -> p.apply(ref, isEquipping)
        );
    }

    @Override
    public @NotNull PowerConfiguration<?> getConfig() {
        return ShappoliTrinketsPowerTypes.ACTION_ON_TRINKET_CHANGE;
    }
}
