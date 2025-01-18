package com.github.shap_po.shappoli.mixin.integration.trinkets;

import com.github.shap_po.shappoli.integration.trinkets.component.item.ShappoliTrinketsDataComponentTypes;
import com.github.shap_po.shappoli.integration.trinkets.component.item.TrinketItemPowersComponent;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.component.ComponentHolder;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.function.Consumer;

@Mixin(value = ItemStack.class, priority = 999) // apply right after the Trinkets mixin
public abstract class ItemStackMixinClient implements ComponentHolder {
    @Unique
    private TooltipType shappoli$tooltipType;
    @Unique
    private List<Text> shappoli$tooltip;


    @Inject(method = "getTooltip", at = @At(value = "INVOKE", target = "Lnet/minecraft/text/MutableText;append(Lnet/minecraft/text/Text;)Lnet/minecraft/text/MutableText;"))
    private void shappoli$cacheTooltipStuff(Item.TooltipContext context, @Nullable PlayerEntity player, TooltipType type, CallbackInfoReturnable<List<Text>> cir, @Local List<Text> tooltip) {
        this.shappoli$tooltipType = type;
        this.shappoli$tooltip = tooltip;
    }

    @Inject(method = "appendAttributeModifiersTooltip", at = @At("HEAD"))
    private void shappoli$appendTrinketItemPowersTooltipWithoutModifiers(Consumer<Text> tooltipConsumer, @Nullable PlayerEntity player, CallbackInfo ci) {
        if (shappoli$tooltipType == null || shappoli$tooltip == null) {
            return;
        }

        TrinketItemPowersComponent itemPowers = this.get(ShappoliTrinketsDataComponentTypes.TRINKET_POWERS);
        if (itemPowers == null || itemPowers.isEmpty()) {
            return;
        }

        itemPowers.appendTooltip(player, (ItemStack) (Object) this, shappoli$tooltip, shappoli$tooltipType);
    }
}
