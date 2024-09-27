package com.github.shap_po.shappoli.mixin.integration.trinkets;

import com.github.shap_po.shappoli.integration.trinkets.power.PreventTrinketEquipPower;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.emi.trinkets.TrinketSlot;
import dev.emi.trinkets.api.SlotReference;
import io.github.apace100.apoli.component.PowerHolderComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(TrinketSlot.class)
public interface TrinketSlotMixin {
    @ModifyReturnValue(method = "canInsert", at = @At("RETURN"))
    private static boolean shappoli$preventTrinketEquip(boolean original, ItemStack stack, SlotReference slotRef, LivingEntity entity) {
        return !PowerHolderComponent.hasPowerType(entity, PreventTrinketEquipPower.class,
            p -> p.doesApply(entity, slotRef, stack)
        ) && original;
    }
}
