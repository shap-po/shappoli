package com.github.shap_po.shappoli.power.factory.condition.item;

import com.github.shap_po.shappoli.Shappoli;
import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.apoli.power.factory.condition.ItemConditions;
import io.github.apace100.calio.data.SerializableData;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Pair;
import net.minecraft.world.World;

public class BlockCondition {
    public static boolean condition(SerializableData.Instance data, Pair<World, ItemStack> worldAndStack) {
        return worldAndStack.getRight().getItem() instanceof BlockItem;
    }

    public static ConditionFactory<Pair<World, ItemStack>> getFactory() {
        ConditionFactory<Pair<World, ItemStack>> factory = new ConditionFactory<>(
            Shappoli.identifier("block"),
            new SerializableData(),
            BlockCondition::condition
        );

        ItemConditions.ALIASES.addPathAlias("is_block", factory.getSerializerId().getPath());
        return factory;
    }
}
