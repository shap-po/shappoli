package com.github.shap_po.shappoli.condition.type.item;

import com.github.shap_po.shappoli.Shappoli;
import io.github.apace100.apoli.condition.factory.ConditionTypeFactory;
import io.github.apace100.apoli.condition.factory.ItemConditions;
import io.github.apace100.calio.data.SerializableData;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Pair;
import net.minecraft.world.World;

public class BlockConditionType {
    public static boolean condition(SerializableData.Instance data, Pair<World, ItemStack> worldAndStack) {
        return worldAndStack.getRight().getItem() instanceof BlockItem;
    }

    public static ConditionTypeFactory<Pair<World, ItemStack>> getFactory() {
        ConditionTypeFactory<Pair<World, ItemStack>> factory = new ConditionTypeFactory<>(
            Shappoli.identifier("block"),
            new SerializableData(),
            BlockConditionType::condition
        );

        ItemConditions.ALIASES.addPathAlias("is_block", factory.getSerializerId().getPath());
        return factory;
    }
}
