package com.github.shap_po.shappoli.condition.type.item;

import com.github.shap_po.shappoli.condition.type.ShappoliItemConditionTypes;
import com.github.shap_po.shappoli.condition.type.meta.SendConditionMetaConditionType;
import com.github.shap_po.shappoli.power.type.ReceiveConditionPowerType;
import com.github.shap_po.shappoli.util.InventoryUtil;
import io.github.apace100.apoli.condition.ConditionConfiguration;
import io.github.apace100.apoli.condition.ItemCondition;
import io.github.apace100.apoli.condition.context.ItemConditionContext;
import io.github.apace100.apoli.condition.type.ItemConditionType;
import io.github.apace100.apoli.power.PowerReference;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;


public class SendConditionItemConditionType extends ItemConditionType implements SendConditionMetaConditionType<ItemConditionContext, ItemCondition> {
    private final PowerReference receiver;

    public SendConditionItemConditionType(PowerReference receiver) {
        this.receiver = receiver;
    }

    @Override
    public PowerReference getReceiver() {
        return receiver;
    }

    @Override
    public boolean test(ItemConditionContext context) {
        return SendConditionMetaConditionType.send(InventoryUtil.getHolder(context.stack()), context, receiver, ReceiveConditionPowerType::receiveItem);
    }

    @Override
    public @NotNull ConditionConfiguration<?> getConfig() {
        return ShappoliItemConditionTypes.SEND_CONDITION;
    }
}
