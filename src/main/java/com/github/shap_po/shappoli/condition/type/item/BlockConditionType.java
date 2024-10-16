package com.github.shap_po.shappoli.condition.type.item;

import com.github.shap_po.shappoli.Shappoli;
import io.github.apace100.apoli.condition.factory.ConditionTypeFactory;
import io.github.apace100.apoli.condition.type.ItemConditionTypes;
import io.github.apace100.calio.data.SerializableData;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Pair;
import net.minecraft.world.World;

public class BlockConditionType {
    public static boolean condition(ItemStack stack) {
        return stack.getItem() instanceof BlockItem;
    }

    public static ConditionTypeFactory<Pair<World, ItemStack>> getFactory() {
        ConditionTypeFactory<Pair<World, ItemStack>> factory = new ConditionTypeFactory<>(
            Shappoli.identifier("is_block"),
            new SerializableData(),
            (data, worldAndStack) -> condition(worldAndStack.getRight())
        );

        ItemConditionTypes.ALIASES.addPathAlias("block", factory.getSerializerId().getPath());
        return factory;
    }
}
