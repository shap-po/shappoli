package com.github.shap_po.shappoli.integration.trinkets.power.type;

import com.github.shap_po.shappoli.integration.trinkets.action.type.entity.ModifyTrinketsInventoryEntityActionType;
import com.github.shap_po.shappoli.integration.trinkets.data.ShappoliTrinketsDataTypes;
import com.github.shap_po.shappoli.integration.trinkets.data.TrinketSlotFilter;
import com.github.shap_po.shappoli.integration.trinkets.keybinding.TrinketKeyBinding;
import com.github.shap_po.shappoli.power.type.ActionOnKeyPressPowerType;
import com.github.shap_po.shappoli.util.MiscUtil;
import com.github.shap_po.shappoli.util.ShappoliKeyBindingReference;
import io.github.apace100.apoli.action.EntityAction;
import io.github.apace100.apoli.action.ItemAction;
import io.github.apace100.apoli.condition.EntityCondition;
import io.github.apace100.apoli.condition.ItemCondition;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.data.TypedDataObjectFactory;
import io.github.apace100.apoli.power.PowerConfiguration;
import io.github.apace100.apoli.power.type.PowerType;
import io.github.apace100.apoli.util.HudRender;
import io.github.apace100.apoli.util.InventoryUtil;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class ActionOnTrinketKeyPressPowerType extends ActionOnKeyPressPowerType {
    public static final TypedDataObjectFactory<ActionOnTrinketKeyPressPowerType> DATA_FACTORY = PowerType.createConditionedDataFactory(
        new SerializableData()
            .add("hud_render", ApoliDataTypes.HUD_RENDER, HudRender.DONT_RENDER)
            .add("cooldown", SerializableDataTypes.INT, 1)

            .add("entity_action", EntityAction.DATA_TYPE.optional(), Optional.empty())
            .add("item_action", ItemAction.DATA_TYPE.optional(), Optional.empty())
            .validate(data -> MiscUtil.checkAtLeastOneFieldExists(data, "entity_action", "item_action"))

            .add("item_condition", ItemCondition.DATA_TYPE.optional(), Optional.empty())

            .add("process_mode", ApoliDataTypes.PROCESS_MODE, InventoryUtil.ProcessMode.STACKS)
            .add("limit", SerializableDataTypes.INT, 0)

            .add("key", ShappoliTrinketsDataTypes.TRINKET_KEYBINDING)

            .add("continuous", SerializableDataTypes.BOOLEAN, false),
        (data, condition) -> new ActionOnTrinketKeyPressPowerType(
            data.get("hud_render"),
            data.getInt("cooldown"),

            data.get("entity_action"),
            data.get("item_action"),
            data.get("item_condition"),

            data.get("process_mode"),
            data.getInt("limit"),

            data.get("key"),
            data.getBoolean("continuous"),
            condition
        ),
        (powerType, serializableData) -> serializableData.instance()
            .set("hud_render", powerType.getRenderSettings())
            .set("cooldown", powerType.getCooldown())

            .set("entity_action", powerType.entityAction)
            .set("item_action", powerType.itemAction)
            .set("item_condition", powerType.itemCondition)

            .set("process_mode", powerType.processMode)
            .set("limit", powerType.limit)

            .set("key", powerType.trinketKeyBinding)

            .set("continuous", powerType.isContinuous())
    );

    private final Optional<ItemAction> itemAction;
    private final Optional<ItemCondition> itemCondition;
    private final InventoryUtil.ProcessMode processMode;
    private final int limit;
    private final TrinketKeyBinding trinketKeyBinding;

    public ActionOnTrinketKeyPressPowerType(
        HudRender hudRender,
        int cooldownDuration,

        Optional<EntityAction> entityAction,
        Optional<ItemAction> itemAction,
        Optional<ItemCondition> itemCondition,

        InventoryUtil.ProcessMode processMode,
        int limit,

        TrinketKeyBinding trinketKeyBinding,
        boolean continuous,

        Optional<EntityCondition> condition
    ) {
        super(
            entityAction,
            hudRender,
            cooldownDuration,
            trinketKeyBinding.getAllKeys(),
            true,
            continuous,
            condition
        );

        this.itemAction = itemAction;
        this.itemCondition = itemCondition;
        this.processMode = processMode;
        this.limit = limit;
        this.trinketKeyBinding = trinketKeyBinding;
    }

    @Override
    public void onUse(ShappoliKeyBindingReference key) {
        List<TrinketSlotFilter> slots = trinketKeyBinding.getTriggeredSlots(key);
        if (slots.isEmpty()) {
            return;
        }

        // use modify trinket inventory action and use power if success
        ModifyTrinketsInventoryEntityActionType action = new ModifyTrinketsInventoryEntityActionType(
            slots,
            processMode, limit,
            entityAction, itemAction, itemCondition);
        if (action.modify(getHolder())) {
            use();
        }
    }

    @Override
    public @NotNull PowerConfiguration<?> getConfig() {
        return ShappoliTrinketsPowerTypes.ACTION_ON_TRINKET_KEY_PRESS;
    }
}
