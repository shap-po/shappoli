package com.github.shap_po.shappoli.action.type.entity;

import com.github.shap_po.shappoli.action.type.ShappoliEntityActionTypes;
import com.github.shap_po.shappoli.action.type.meta.SendActionMetaActionType;
import com.github.shap_po.shappoli.power.type.ReceiveActionPowerType;
import io.github.apace100.apoli.action.ActionConfiguration;
import io.github.apace100.apoli.action.EntityAction;
import io.github.apace100.apoli.action.context.EntityActionContext;
import io.github.apace100.apoli.action.type.EntityActionType;
import io.github.apace100.apoli.power.PowerReference;
import net.minecraft.entity.Entity;
import org.jetbrains.annotations.NotNull;

public class SendActionEntityActionType extends EntityActionType implements SendActionMetaActionType<EntityActionContext, EntityAction> {
    private final PowerReference power;

    public SendActionEntityActionType(PowerReference power) {
        this.power = power;
    }

    @Override
    public PowerReference getReceiver() {
        return power;
    }

    @Override
    public void accept(EntityActionContext context) {
        SendActionMetaActionType.send(context.entity(), context, power, ReceiveActionPowerType::receiveEntityAction);
    }

    @Override
    public @NotNull ActionConfiguration<?> getConfig() {
        return ShappoliEntityActionTypes.SEND_ACTION;
    }
}
