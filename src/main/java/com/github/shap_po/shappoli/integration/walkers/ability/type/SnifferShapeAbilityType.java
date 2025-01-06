package com.github.shap_po.shappoli.integration.walkers.ability.type;

import io.github.apace100.apoli.action.ActionConfiguration;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.NotNull;
import tocraft.walkers.ability.impl.specific.SnifferAbility;

public class SnifferShapeAbilityType extends ShapeAbilityType {
    private final SnifferAbility<LivingEntity> ability = new SnifferAbility<>();

    @Override
    protected void execute(PlayerEntity player, LivingEntity shape) {
        ability.onUse(player, shape, player.getWorld());
    }

    @Override
    public @NotNull ActionConfiguration<?> getConfig() {
        return ShapeAbilityTypes.SNIFFER;
    }
}
