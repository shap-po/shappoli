package com.github.shap_po.shappoli.mixin.integration.trinkets;

import com.github.shap_po.shappoli.integration.trinkets.access.SyncingTrinketInventory;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.emi.trinkets.api.LivingEntityTrinketComponent;
import dev.emi.trinkets.api.TrinketInventory;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntityTrinketComponent.class)
public class LivingEntityTrinketComponentMixin {
    @WrapOperation(method = "update",at= @At(value = "INVOKE", target = "Ldev/emi/trinkets/api/TrinketInventory;setStack(ILnet/minecraft/item/ItemStack;)V"))
    private void shappoli$onUpdate(TrinketInventory instance, int slot, ItemStack stack, Operation<Void> original) {
        SyncingTrinketInventory syncingTrinketInventory = (SyncingTrinketInventory) instance;
        syncingTrinketInventory.shappoli$setSyncing(true);
        original.call(instance, slot,stack);
        syncingTrinketInventory.shappoli$setSyncing(false);
    }
}
