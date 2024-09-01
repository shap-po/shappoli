package com.github.shap_po.shappoli.mixin;

import com.github.shap_po.shappoli.power.ModifyVillagerReputationPower;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import io.github.apace100.apoli.component.PowerHolderComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(VillagerEntity.class)
public abstract class VillagerEntityMixin extends Entity {
    public VillagerEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @ModifyReturnValue(method = "getReputation", at = @At("RETURN"))
    private int shappoli$modifyReputation(int reputation, PlayerEntity player) {
        return Math.round(PowerHolderComponent.modify(player, ModifyVillagerReputationPower.class, reputation, (p) -> p.doesApply((LivingEntity) (Object) this)));
    }
}
