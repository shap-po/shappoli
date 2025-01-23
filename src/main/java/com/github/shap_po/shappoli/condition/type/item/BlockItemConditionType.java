package com.github.shap_po.shappoli.condition.type.item;

import com.github.shap_po.shappoli.condition.type.ShappoliItemConditionTypes;
import io.github.apace100.apoli.condition.ConditionConfiguration;
import io.github.apace100.apoli.condition.context.ItemConditionContext;
import io.github.apace100.apoli.condition.type.ItemConditionType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class BlockItemConditionType extends ItemConditionType {
    @Override
    public boolean test(ItemConditionContext context) {
        return context.stack().getItem() instanceof BlockItem;
    }


    @Override
    public @NotNull ConditionConfiguration<?> getConfig() {
        return ShappoliItemConditionTypes.BLOCK;
    }
}
