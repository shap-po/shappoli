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
import net.minecraft.util.Pair;
import net.minecraft.world.World;

public class ItemConditions {
    public static void register() {
        register(HolderCondition.getFactory());
        register(BlockCondition.getFactory());

        register(
            SendConditionCondition.getFactory(
                worldAndStack -> InventoryUtil.getHolder(worldAndStack.getRight()),
                ReceiveConditionPower::receiveItem,
                io.github.apace100.apoli.power.factory.condition.ItemConditions.ALIASES
            )
        );
    }

    private static void register(ConditionFactory<Pair<World, ItemStack>> conditionFactory) {
        Registry.register(ApoliRegistries.ITEM_CONDITION, conditionFactory.getSerializerId(), conditionFactory);
    }
}
