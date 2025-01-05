package com.github.shap_po.shappoli.power.type;

import com.github.shap_po.shappoli.util.MiscUtil;
import io.github.apace100.apoli.action.BiEntityAction;
import io.github.apace100.apoli.action.EntityAction;
import io.github.apace100.apoli.action.ItemAction;
import io.github.apace100.apoli.action.context.BiEntityActionContext;
import io.github.apace100.apoli.action.context.EntityActionContext;
import io.github.apace100.apoli.action.context.ItemActionContext;
import io.github.apace100.apoli.condition.BiEntityCondition;
import io.github.apace100.apoli.condition.EntityCondition;
import io.github.apace100.apoli.condition.ItemCondition;
import io.github.apace100.apoli.data.TypedDataObjectFactory;
import io.github.apace100.apoli.power.PowerConfiguration;
import io.github.apace100.apoli.power.type.PowerType;
import io.github.apace100.calio.data.SerializableData;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class ReceiveActionPowerType extends PowerType {
    public static final TypedDataObjectFactory<ReceiveActionPowerType> DATA_FACTORY = PowerType.createConditionedDataFactory(
        new SerializableData()
            .add("action", EntityAction.DATA_TYPE.optional(), Optional.empty())
            .add("bientity_action", BiEntityAction.DATA_TYPE.optional(), Optional.empty())
            .add("bientity_condition", BiEntityCondition.DATA_TYPE.optional(), Optional.empty())
            .add("entity_action", EntityAction.DATA_TYPE.optional(), Optional.empty())
            .add("entity_condition", EntityCondition.DATA_TYPE.optional(), Optional.empty())
            .add("item_action", ItemAction.DATA_TYPE.optional(), Optional.empty())
            .add("item_condition", ItemCondition.DATA_TYPE.optional(), Optional.empty())
            .validate(data -> MiscUtil.checkAtLeastOneFieldExists(data, "action", "bientity_action", "entity_action", "item_action")),
        (data, condition) -> new ReceiveActionPowerType(
            data.get("action"),
            data.get("bientity_action"),
            data.get("bientity_condition"),
            data.get("entity_action"),
            data.get("entity_condition"),
            data.get("item_action"),
            data.get("item_condition"),
            condition
        ),
        (powerType, serializableData) -> serializableData.instance()
            .set("action", powerType.action)
            .set("bientity_action", powerType.biEntityAction)
            .set("bientity_condition", powerType.biEntityCondition)
            .set("entity_action", powerType.entityAction)
            .set("entity_condition", powerType.entityCondition)
            .set("item_action", powerType.itemAction)
            .set("item_condition", powerType.itemCondition)
    );

    private final Optional<EntityAction> action;
    private final Optional<BiEntityAction> biEntityAction;
    private final Optional<BiEntityCondition> biEntityCondition;
    private final Optional<EntityAction> entityAction;
    private final Optional<EntityCondition> entityCondition;
    private final Optional<ItemAction> itemAction;
    private final Optional<ItemCondition> itemCondition;

    public ReceiveActionPowerType(
        Optional<EntityAction> action,
        Optional<BiEntityAction> biEntityAction,
        Optional<BiEntityCondition> biEntityCondition,
        Optional<EntityAction> entityAction,
        Optional<EntityCondition> entityCondition,
        Optional<ItemAction> itemAction,
        Optional<ItemCondition> itemCondition,
        Optional<EntityCondition> condition
    ) {
        super(condition);
        this.action = action;
        this.biEntityAction = biEntityAction;
        this.biEntityCondition = biEntityCondition;
        this.entityAction = entityAction;
        this.entityCondition = entityCondition;
        this.itemAction = itemAction;
        this.itemCondition = itemCondition;
    }

    public void receiveBiEntityAction(BiEntityActionContext context) {
        if (biEntityCondition.map(biEntityCondition -> biEntityCondition.test(context.forCondition())).orElse(true)) {
            biEntityAction.ifPresent(biEntityAction -> biEntityAction.accept(context));
            receiveAnyAction();
        }
    }

    public void receiveEntityAction(EntityActionContext context) {
        if (entityCondition.map(entityCondition -> entityCondition.test(context.forCondition())).orElse(true)) {
            entityAction.ifPresent(entityAction -> entityAction.accept(context));
            receiveAnyAction();
        }
    }

    public void receiveItemAction(ItemActionContext context) {
        if (itemCondition.map(itemCondition -> itemCondition.test(context.forCondition())).orElse(true)) {
            itemAction.ifPresent(itemAction -> itemAction.accept(context));
            receiveAnyAction();
        }
    }

    private void receiveAnyAction() {
        action.ifPresent(action -> action.execute(getHolder()));
    }

    @Override
    public @NotNull PowerConfiguration<?> getConfig() {
        return ShappoliPowerTypes.RECEIVE_ACTION;
    }
}
