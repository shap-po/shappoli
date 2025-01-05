package com.github.shap_po.shappoli.condition.type.entity;

import com.github.shap_po.shappoli.condition.type.ShappoliEntityConditionTypes;
import com.github.shap_po.shappoli.condition.type.meta.SendConditionMetaConditionType;
import com.github.shap_po.shappoli.power.type.ReceiveConditionPowerType;
import io.github.apace100.apoli.condition.ConditionConfiguration;
import io.github.apace100.apoli.condition.EntityCondition;
import io.github.apace100.apoli.condition.context.EntityConditionContext;
import io.github.apace100.apoli.condition.type.EntityConditionType;
import io.github.apace100.apoli.power.PowerReference;
import net.minecraft.entity.Entity;
import org.jetbrains.annotations.NotNull;


public class SendConditionEntityConditionType extends EntityConditionType implements SendConditionMetaConditionType<EntityConditionContext, EntityCondition> {
    private final PowerReference receiver;

    public SendConditionEntityConditionType(PowerReference receiver) {
        this.receiver = receiver;
    }

    @Override
    public PowerReference getReceiver() {
        return receiver;
    }

    @Override
    public boolean test(Entity entity) {
        return SendConditionMetaConditionType.send(entity, new EntityConditionContext(entity), receiver, ReceiveConditionPowerType::receiveEntity);
    }

    @Override
    public @NotNull ConditionConfiguration<?> getConfig() {
        return ShappoliEntityConditionTypes.SEND_CONDITION;
    }
}
