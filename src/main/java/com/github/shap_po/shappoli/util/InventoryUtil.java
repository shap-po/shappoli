package com.github.shap_po.shappoli.util;

import io.github.apace100.apoli.access.EntityLinkedItemStack;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;

@SuppressWarnings({"UnusedReturnValue"})
public class InventoryUtil {
    @SuppressWarnings({"ConstantConditions"})
    public static Entity getHolder(ItemStack stack) {
        return ((EntityLinkedItemStack) (Object) stack).getEntity();
    }

    @SuppressWarnings({"ConstantConditions"})
    public static ItemStack setHolder(ItemStack stack, Entity holder) {
        ((EntityLinkedItemStack) (Object) stack).setEntity(holder);
        return stack;
    }
}
