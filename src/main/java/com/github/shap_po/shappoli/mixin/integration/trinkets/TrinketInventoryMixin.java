package com.github.shap_po.shappoli.mixin.integration.trinkets;

import com.github.shap_po.shappoli.util.InventoryUtil;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.emi.trinkets.api.TrinketInventory;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(TrinketInventory.class)
public class TrinketInventoryMixin {
    @ModifyReturnValue(method = "getStack", at = @At("RETURN"))
    private ItemStack shappoli$modifyGetStack(ItemStack stack) {
        LivingEntity entity = ((TrinketInventory) (Object) this).getComponent().getEntity();
        InventoryUtil.setHolder(stack, entity);
        return stack;
    }

    @WrapOperation(
        method = "update",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/util/collection/DefaultedList;ofSize(ILjava/lang/Object;)Lnet/minecraft/util/collection/DefaultedList;")
    )
    private <E> DefaultedList<E> shappoli$fixNegativeSize(int size, E defaultValue, Operation<DefaultedList<E>> original) {
        size = Math.max(size, 0);
        return (original).call(size, defaultValue);
    }
}
