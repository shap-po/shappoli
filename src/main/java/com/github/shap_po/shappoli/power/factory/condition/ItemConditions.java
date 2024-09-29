package com.github.shap_po.shappoli.power.factory.condition;

import com.github.shap_po.shappoli.power.ReceiveConditionPower;
import com.github.shap_po.shappoli.power.factory.condition.item.BlockCondition;
import com.github.shap_po.shappoli.power.factory.condition.item.HolderCondition;
import com.github.shap_po.shappoli.power.factory.condition.meta.SendConditionCondition;
import com.github.shap_po.shappoli.util.InventoryUtil;
import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registry;

public class ItemConditions {
    public static void register() {
        register(BlockCondition.getFactory());
        register(HolderCondition.getFactory());

        register(
            SendConditionCondition.getFactory(
                InventoryUtil::getHolder,
                ReceiveConditionPower::receiveItem
            )
        );
    }

    private static void register(ConditionFactory<ItemStack> conditionFactory) {
        Registry.register(ApoliRegistries.ITEM_CONDITION, conditionFactory.getSerializerId(), conditionFactory);
    }
}
