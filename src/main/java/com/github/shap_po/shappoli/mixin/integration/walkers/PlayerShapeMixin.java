package com.github.shap_po.shappoli.mixin.integration.walkers;

import com.github.shap_po.shappoli.integration.walkers.power.PreventMorphingPower;
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
        if (PowerHolderComponent.hasPower(player, PreventMorphingPower.class, p -> p.doesApply(entity))) {
            cir.setReturnValue(false);
        }
    }
}
