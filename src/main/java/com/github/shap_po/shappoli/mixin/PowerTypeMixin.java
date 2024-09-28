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
public class PowerTypeMixin<T extends PowerType> implements SuppressiblePower {
    @Shadow
    protected LivingEntity entity;

    @Unique
    @SuppressWarnings({"unchecked"})
    T power = (T) (Object) this;

    @Unique
    private Long shappoli$suppressedUntil;
    @Unique
    private Entity shappoli$supressingEntity;

    @Unique
    private boolean shappoli$hasConditions;

    @Override
    public boolean shappoli$hasConditions() {
        return shappoli$hasConditions;
    }

    @Override
    public void shappoli$setHasConditions(boolean hasConditions) {
        shappoli$hasConditions = hasConditions;
    }

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
    private boolean shappoli$deactivatePower(boolean original) {
        return original &&
            shappoli$isSuppressed() &&
            (power instanceof SuppressPowerPowerType ||
                !PowerHolderComponent.hasPowerType(entity, SuppressPowerPowerType.class, p -> p.doesApply(power)));
    }

    @Unique
    private long getTime() {
        return entity.getEntityWorld().getTime();
    }
}
