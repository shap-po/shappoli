package com.github.shap_po.shappoli.mixin.integration.trinkets;


import com.github.shap_po.shappoli.integration.trinkets.power.ActionOnTrinketChangePower;
import dev.emi.trinkets.SurvivalTrinketSlot;
import dev.emi.trinkets.api.TrinketInventory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = PlayerScreenHandler.class, priority = 999) // apply just before the Trinkets mixin
public abstract class PlayerScreenHandlerMixin extends ScreenHandler {
    @Shadow
    @Final
    private PlayerEntity owner;

    protected PlayerScreenHandlerMixin(@Nullable ScreenHandlerType<?> type, int syncId) {
        super(type, syncId);
    }

    /**
     * For some reason, {@link PlayerScreenHandler#quickMove} does not call the {@link TrinketInventory#setStack} method when unequipping a trinket, handle it here
     */
    @Inject(method = "quickMove", at = @At("HEAD"))
    private void quickMove(PlayerEntity player, int index, CallbackInfoReturnable<ItemStack> info) {
        Slot slot = slots.get(index);
        if (!(slot instanceof SurvivalTrinketSlot trinketSlot)) {
            return;
        }
        if (trinketSlot.hasStack()) {
            ItemStack stack = trinketSlot.getStack();
            ActionOnTrinketChangePower.handleTrinketChange(owner, (TrinketInventory) trinketSlot.inventory, stack, trinketSlot.getIndex(), false);
        }
    }
}
