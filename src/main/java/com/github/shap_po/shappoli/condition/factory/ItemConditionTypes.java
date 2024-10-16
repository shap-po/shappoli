package com.github.shap_po.shappoli.condition.factory;

import com.github.shap_po.shappoli.condition.type.item.BlockConditionType;
import com.github.shap_po.shappoli.condition.type.item.HolderConditionType;
import com.github.shap_po.shappoli.condition.type.meta.SendConditionConditionType;
import com.github.shap_po.shappoli.power.type.ReceiveConditionPowerType;
import com.github.shap_po.shappoli.util.InventoryUtil;
import io.github.apace100.apoli.condition.factory.ConditionTypeFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registry;
import net.minecraft.util.Pair;
import net.minecraft.world.World;

public class ItemConditionTypes {
    public static void register() {
        register(BlockConditionType.getFactory());
        register(HolderConditionType.getFactory());

        register(
            SendConditionConditionType.getFactory(
                worldAndStack -> InventoryUtil.getHolder(worldAndStack.getRight()),
                ReceiveConditionPowerType::receiveItem,
                io.github.apace100.apoli.condition.type.ItemConditionTypes.ALIASES
            )
        );
    }

    private static void register(ConditionTypeFactory<Pair<World, ItemStack>> ConditionTypeFactory) {
        Registry.register(ApoliRegistries.ITEM_CONDITION, ConditionTypeFactory.getSerializerId(), ConditionTypeFactory);
    }
}
