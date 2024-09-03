package com.github.shap_po.shappoli.mixin;

import com.github.shap_po.shappoli.access.SuppressiblePower;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import io.github.apace100.apoli.power.Power;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Power.class)
public class PowerMixin implements SuppressiblePower {
    @Shadow
    protected LivingEntity entity;

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
        long newTime = getTime() + duration;
        if (shappoli$suppressedUntil == null || newTime > shappoli$suppressedUntil) {
            shappoli$suppressedUntil = newTime;
            shappoli$supressingEntity = supressingEntity;
            return true;
        }
        return false;
    }

    @Override
    public boolean shappoli$isSuppressed() {
        return shappoli$suppressedUntil == null || getTime() >= shappoli$suppressedUntil;
    }

    @ModifyReturnValue(method = "isActive", at = @At("RETURN"), remap = false)
    private boolean shappoli$disablePower(boolean original) {
        return original && shappoli$isSuppressed();
    }

    @Unique
    private long getTime() {
        return entity.getEntityWorld().getTime();
    }
}
