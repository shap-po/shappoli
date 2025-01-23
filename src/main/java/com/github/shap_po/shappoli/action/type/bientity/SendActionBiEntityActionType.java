package com.github.shap_po.shappoli.action.type.bientity;

import com.github.shap_po.shappoli.action.type.ShappoliBiEntityActionTypes;
import com.github.shap_po.shappoli.action.type.meta.SendActionMetaActionType;
import com.github.shap_po.shappoli.power.type.ReceiveActionPowerType;
import io.github.apace100.apoli.action.ActionConfiguration;
import io.github.apace100.apoli.action.BiEntityAction;
import io.github.apace100.apoli.action.context.BiEntityActionContext;
import io.github.apace100.apoli.action.type.BiEntityActionType;
import io.github.apace100.apoli.power.PowerReference;
import net.minecraft.entity.Entity;
import org.jetbrains.annotations.NotNull;

public class SendActionBiEntityActionType extends BiEntityActionType implements SendActionMetaActionType<BiEntityActionContext, BiEntityAction> {
    private final PowerReference power;

    public SendActionBiEntityActionType(PowerReference power) {
        this.power = power;
    }

    @Override
    public PowerReference getReceiver() {
        return power;
    }

    @Override
    public void accept(BiEntityActionContext context) {
        SendActionMetaActionType.send(context.actor(), context, power, ReceiveActionPowerType::receiveBiEntityAction);
    }

    @Override
    public @NotNull ActionConfiguration<?> getConfig() {
        return ShappoliBiEntityActionTypes.SEND_ACTION;
    }
}
