package com.github.shap_po.shappoli.mixin.integration.trinkets;

import com.github.shap_po.shappoli.integration.trinkets.power.ActionOnTrinketChangePower;
import com.github.shap_po.shappoli.util.InventoryUtil;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.emi.trinkets.api.TrinketInventory;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TrinketInventory.class)
public class TrinketInventoryMixin {
    @ModifyReturnValue(method = "getStack", at = @At("RETURN"))
    private ItemStack shappoli$setHolder(ItemStack stack) {
        InventoryUtil.setHolder(stack, shappoli$getEntity());
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


    @Inject(method = "removeStack(I)Lnet/minecraft/item/ItemStack;", at = @At("HEAD"))
    private void shappoli$onRemoveStack(int slot, CallbackInfoReturnable<ItemStack> cir) {
        ActionOnTrinketChangePower.handleTrinketChange(shappoli$getEntity(), shappoli$getInventory(), shappoli$getStack(slot), slot, false);
    }

    @Inject(method = "removeStack(II)Lnet/minecraft/item/ItemStack;", at = @At("HEAD"))
    private void shappoli$onRemoveStack(int slot, int amount, CallbackInfoReturnable<ItemStack> cir) {
        if (amount >= shappoli$getStack(slot).getCount()) { // only call if the stack is fully removed
            shappoli$onRemoveStack(slot, cir);
        }
    }

    @WrapMethod(method = "setStack")
    private void shappoli$onSetStack(int slot, ItemStack stack, Operation<Void> original) {
        ItemStack oldStack = shappoli$getStack(slot);
        if (ItemStack.areEqual(oldStack, stack)) {
            original.call(slot, stack);
            return;
        }
        ActionOnTrinketChangePower.handleTrinketChange(shappoli$getEntity(), shappoli$getInventory(), oldStack, slot, false);
        original.call(slot, stack); // update the stack before handling the equip action
        ActionOnTrinketChangePower.handleTrinketChange(shappoli$getEntity(), shappoli$getInventory(), stack, slot, true);
    }


    @Unique
    private TrinketInventory shappoli$getInventory() {
        return (TrinketInventory) (Object) this;
    }

    @Unique
    private ItemStack shappoli$getStack(int slot) {
        return shappoli$getInventory().getStack(slot);
    }

    @Unique
    private LivingEntity shappoli$getEntity() {
        return shappoli$getInventory().getComponent().getEntity();
    }
}
