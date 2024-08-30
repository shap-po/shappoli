package com.github.shap_po.shappoli.mixin.integration.trinkets;

import com.github.shap_po.shappoli.util.InventoryUtil;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.emi.trinkets.api.TrinketInventory;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
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
}
