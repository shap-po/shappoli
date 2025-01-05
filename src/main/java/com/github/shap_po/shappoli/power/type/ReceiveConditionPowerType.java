package com.github.shap_po.shappoli.power.type;

import com.github.shap_po.shappoli.util.MiscUtil;
import io.github.apace100.apoli.condition.BiEntityCondition;
import io.github.apace100.apoli.condition.EntityCondition;
import io.github.apace100.apoli.condition.ItemCondition;
import io.github.apace100.apoli.condition.context.BiEntityConditionContext;
import io.github.apace100.apoli.condition.context.EntityConditionContext;
import io.github.apace100.apoli.condition.context.ItemConditionContext;
import io.github.apace100.apoli.data.TypedDataObjectFactory;
import io.github.apace100.apoli.power.PowerConfiguration;
import io.github.apace100.apoli.power.type.PowerType;
import io.github.apace100.calio.data.SerializableData;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class ReceiveConditionPowerType extends PowerType {
    public static final TypedDataObjectFactory<ReceiveConditionPowerType> DATA_FACTORY = PowerType.createConditionedDataFactory(
        new SerializableData()
            .add("bientity_condition", BiEntityCondition.DATA_TYPE.optional(), Optional.empty())
            .add("entity_condition", EntityCondition.DATA_TYPE.optional(), Optional.empty())
            .add("item_condition", ItemCondition.DATA_TYPE.optional(), Optional.empty())
            .validate(data -> MiscUtil.checkAtLeastOneFieldExists(data, "bientity_condition", "entity_condition", "item_condition")),
        (data, condition) -> new ReceiveConditionPowerType(
            data.get("bientity_condition"),
            data.get("entity_condition"),
            data.get("item_condition"),
            condition
        ),
        (powerType, serializableData) -> serializableData.instance()
            .set("bientity_condition", powerType.biEntityCondition)
            .set("entity_condition", powerType.entityCondition)
            .set("item_condition", powerType.itemCondition)
    );

    private final Optional<BiEntityCondition> biEntityCondition;
    private final Optional<EntityCondition> entityCondition;
    private final Optional<ItemCondition> itemCondition;

    public ReceiveConditionPowerType(
        Optional<BiEntityCondition> biEntityCondition,
        Optional<EntityCondition> entityCondition,
        Optional<ItemCondition> itemCondition,
        Optional<EntityCondition> condition
    ) {
        super(condition);
        this.biEntityCondition = biEntityCondition;
        this.entityCondition = entityCondition;
        this.itemCondition = itemCondition;
    }

    public boolean receiveBientity(BiEntityConditionContext context) {
        return biEntityCondition.map(condition -> condition.test(context)).orElse(true);
    }

    public boolean receiveEntity(EntityConditionContext context) {
        return entityCondition.map(condition -> condition.test(context)).orElse(true);
    }

    public boolean receiveItem(ItemConditionContext context) {
        return itemCondition.map(condition -> condition.test(context)).orElse(true);
    }

    @Override
    public @NotNull PowerConfiguration<?> getConfig() {
        return ShappoliPowerTypes.RECEIVE_CONDITION;
    }
}
