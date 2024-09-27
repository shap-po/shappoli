package com.github.shap_po.shappoli.mixin;

import com.github.shap_po.shappoli.power.ActionOnEntityCollisionPower;
import io.github.apace100.apoli.component.PowerHolderComponent;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Inject(method = "tick", at = @At("RETURN"))
    private void shappoli$actionOnEntityCollision(CallbackInfo ci) {
        PowerHolderComponent.withPowerTypes(
            (Entity) (Object) this,
            ActionOnEntityCollisionPower.class,
            (p) -> true,
            ActionOnEntityCollisionPower::apply
        );
    }
}
