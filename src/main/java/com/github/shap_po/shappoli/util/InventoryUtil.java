package com.github.shap_po.shappoli.util;

import io.github.apace100.apoli.access.EntityLinkedItemStack;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;

@SuppressWarnings({"UnusedReturnValue"})
public class InventoryUtil {
    @SuppressWarnings({"ConstantConditionTypes"})
    public static Entity getHolder(ItemStack stack) {
        return ((EntityLinkedItemStack) (Object) stack).apoli$getEntity();
    }

    @SuppressWarnings({"ConstantConditionTypes"})
    public static ItemStack setHolder(ItemStack stack, Entity holder) {
        ((EntityLinkedItemStack) (Object) stack).apoli$setEntity(holder);
        return stack;
    }
}
