package com.github.shap_po.shappoli.action.type.item;

import com.github.shap_po.shappoli.action.type.ShappoliItemActionTypes;
import com.github.shap_po.shappoli.action.type.meta.SendActionMetaActionType;
import com.github.shap_po.shappoli.power.type.ReceiveActionPowerType;
import com.github.shap_po.shappoli.util.InventoryUtil;
import io.github.apace100.apoli.action.ActionConfiguration;
import io.github.apace100.apoli.action.ItemAction;
import io.github.apace100.apoli.action.context.ItemActionContext;
import io.github.apace100.apoli.action.type.ItemActionType;
import io.github.apace100.apoli.power.PowerReference;
import net.minecraft.inventory.StackReference;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class SendActionItemActionType extends ItemActionType implements SendActionMetaActionType<ItemActionContext, ItemAction> {
    private final PowerReference power;

    public SendActionItemActionType(PowerReference power) {
        this.power = power;
    }

    @Override
    public PowerReference getReceiver() {
        return power;
    }

    @Override
    public void accept(ItemActionContext context) {
        SendActionMetaActionType.send(InventoryUtil.getHolder(context.stackReference().get()), context, power, ReceiveActionPowerType::receiveItemAction);
    }

    @Override
    public @NotNull ActionConfiguration<?> getConfig() {
        return ShappoliItemActionTypes.SEND_ACTION;
    }
}
