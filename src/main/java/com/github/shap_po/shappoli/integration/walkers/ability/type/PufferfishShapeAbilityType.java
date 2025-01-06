package com.github.shap_po.shappoli.integration.walkers.ability.type;

import io.github.apace100.apoli.action.ActionConfiguration;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.PufferfishEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.NotNull;
import tocraft.walkers.ability.impl.specific.PufferfishAbility;

public class PufferfishShapeAbilityType extends ShapeAbilityType {
    private final PufferfishAbility<PufferfishEntity> ability = new PufferfishAbility<>();

    @Override
    protected void execute(PlayerEntity player, LivingEntity shape) {
        if (!(shape instanceof PufferfishEntity pufferfish)) return;
        ability.onUse(player, pufferfish, player.getWorld());
    }

    @Override
    public @NotNull ActionConfiguration<?> getConfig() {
        return ShapeAbilityTypes.PUFFERFISH;
    }
}
