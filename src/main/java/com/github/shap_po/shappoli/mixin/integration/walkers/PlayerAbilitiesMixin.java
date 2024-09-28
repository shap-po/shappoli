package com.github.shap_po.shappoli.mixin.integration.walkers;

import com.github.shap_po.shappoli.integration.walkers.power.type.PreventShapeAbilityUsePowerType;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import io.github.apace100.apoli.component.PowerHolderComponent;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import tocraft.walkers.api.PlayerAbilities;

@Mixin(PlayerAbilities.class)
public class PlayerAbilitiesMixin {
    @ModifyReturnValue(method = "canUseAbility", at = @At("RETURN"))
    private static boolean shappoli$preventAbilityUse(boolean original, PlayerEntity player) {
        return original && !PowerHolderComponent.hasPowerType(player, PreventShapeAbilityUsePowerType.class, PreventShapeAbilityUsePowerType::doesApply);
    }
}
