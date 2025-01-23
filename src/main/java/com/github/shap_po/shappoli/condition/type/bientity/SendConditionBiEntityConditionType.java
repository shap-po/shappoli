package com.github.shap_po.shappoli.condition.type.bientity;

import com.github.shap_po.shappoli.condition.type.ShappoliBiEntityConditionTypes;
import com.github.shap_po.shappoli.condition.type.meta.SendConditionMetaConditionType;
import com.github.shap_po.shappoli.power.type.ReceiveConditionPowerType;
import io.github.apace100.apoli.condition.BiEntityCondition;
import io.github.apace100.apoli.condition.ConditionConfiguration;
import io.github.apace100.apoli.condition.context.BiEntityConditionContext;
import io.github.apace100.apoli.condition.type.BiEntityConditionType;
import io.github.apace100.apoli.power.PowerReference;
import net.minecraft.entity.Entity;
import org.jetbrains.annotations.NotNull;


public class SendConditionBiEntityConditionType extends BiEntityConditionType implements SendConditionMetaConditionType<BiEntityConditionContext, BiEntityCondition> {
    private final PowerReference receiver;

    public SendConditionBiEntityConditionType(PowerReference receiver) {
        this.receiver = receiver;
    }

    @Override
    public PowerReference getReceiver() {
        return receiver;
    }

    @Override
    public boolean test(BiEntityConditionContext context) {
        return SendConditionMetaConditionType.send(context.actor(), context, receiver, ReceiveConditionPowerType::receiveBientity);
    }

    @Override
    public @NotNull ConditionConfiguration<?> getConfig() {
        return ShappoliBiEntityConditionTypes.SEND_CONDITION;
    }
}
