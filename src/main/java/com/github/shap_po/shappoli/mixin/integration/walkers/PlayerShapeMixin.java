package com.github.shap_po.shappoli.mixin.integration.walkers;

import com.github.shap_po.shappoli.integration.walkers.power.ActionOnShapeChangePower;
import com.github.shap_po.shappoli.integration.walkers.power.PreventShapeChangePower;
import io.github.apace100.apoli.component.PowerHolderComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tocraft.walkers.api.PlayerShape;

@Mixin(PlayerShape.class)
public class PlayerShapeMixin {
    @Inject(method = "updateShapes", at = @At(value = "HEAD"), cancellable = true)
    private static void shappoli$preventShapeChange(ServerPlayerEntity player, LivingEntity entity, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity shape = entity == null ? player : entity;
        if (PowerHolderComponent.hasPowerType(player, PreventShapeChangePower.class, p -> p.doesApply(shape))) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "updateShapes", at = @At(value = "RETURN"))
    private static void shappoli$onShapeChange(ServerPlayerEntity player, LivingEntity entity, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity shape = entity == null ? player : entity;
        if (cir.getReturnValue()) {
            PowerHolderComponent.withPowerTypes(player, ActionOnShapeChangePower.class, p -> p.doesApply(shape), p -> p.apply(shape));
        }
    }
}
