package com.github.shap_po.shappoli.mixin.integration.trinkets;

import com.github.shap_po.shappoli.integration.trinkets.power.PreventTrinketUnequipPower;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.emi.trinkets.SurvivalTrinketSlot;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketInventory;
import io.github.apace100.apoli.component.PowerHolderComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SurvivalTrinketSlot.class)
public class SurvivalTrinketSlotMixin extends Slot {
    @Shadow
    @Final
    private TrinketInventory trinketInventory;
    @Shadow
    @Final
    private int slotOffset;

    public SurvivalTrinketSlotMixin(Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }

    @ModifyReturnValue(method = "canTakeItems", at = @At("RETURN"), remap = false)
    public boolean shappoli$preventTrinketUnequip(boolean original, PlayerEntity player) {
        ItemStack stack = this.getStack();
        SlotReference slotRef = new SlotReference(trinketInventory, slotOffset);

        return !PowerHolderComponent.hasPower(player, PreventTrinketUnequipPower.class,
            p -> p.doesApply(player, slotRef, stack)
        ) && original;
    }

}
