package com.github.shap_po.shappoli.mixin;

import com.github.shap_po.shappoli.access.SuppressiblePower;
import com.github.shap_po.shappoli.power.type.SuppressPowerPowerType;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.power.type.PowerType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PowerType.class)
public abstract class PowerTypeMixin implements SuppressiblePower {
    @Shadow
    private LivingEntity holder;
    @Unique
    private Long shappoli$suppressedUntil;
    @Unique
    private Entity shappoli$supressingEntity;

    @Override
    public Entity shappoli$getSupressingEntity() {
        return shappoli$isSuppressed() ? null : shappoli$supressingEntity;
    }

    @Override
    public boolean shappoli$suppressFor(int duration, Entity supressingEntity) {
        long newTime = shappoli$getTime() + duration;
        if (shappoli$suppressedUntil == null || newTime > shappoli$suppressedUntil) {
            shappoli$suppressedUntil = newTime;
            shappoli$supressingEntity = supressingEntity;
            return true;
        }
        return false;
    }

    @Override
    public boolean shappoli$isSuppressed() {
        return shappoli$suppressedUntil == null || shappoli$getTime() >= shappoli$suppressedUntil;
    }

    @ModifyReturnValue(method = "isActive", at = @At("RETURN"), remap = false)
    private boolean shappoli$deactivatePower(boolean original) {
        return original &&
            shappoli$isSuppressed() &&
            (shappoli$getThis() instanceof SuppressPowerPowerType ||
                !PowerHolderComponent.hasPowerType(holder, SuppressPowerPowerType.class, p -> p.doesApply(shappoli$getThis())));
    }

    @Unique
    private long shappoli$getTime() {
        return holder.getEntityWorld().getTime();
    }

    @Unique
    private PowerType shappoli$getThis() {
        return (PowerType) (Object) this;
    }
}
