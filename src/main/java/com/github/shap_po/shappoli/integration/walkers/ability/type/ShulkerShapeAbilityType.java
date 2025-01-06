package com.github.shap_po.shappoli.integration.walkers.ability.type;

import io.github.apace100.apoli.action.ActionConfiguration;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.ShulkerEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.NotNull;
import tocraft.walkers.ability.impl.specific.ShulkerAbility;

public class ShulkerShapeAbilityType extends ShapeAbilityType {
    private final ShulkerAbility<ShulkerEntity> ability = new ShulkerAbility<>();

    @Override
    protected void execute(PlayerEntity player, LivingEntity shape) {
        if (!(shape instanceof ShulkerEntity shulker)) return;
        ability.onUse(player, shulker, player.getWorld());
    }

    @Override
    public @NotNull ActionConfiguration<?> getConfig() {
        return ShapeAbilityTypes.SHULKER;
    }
}
