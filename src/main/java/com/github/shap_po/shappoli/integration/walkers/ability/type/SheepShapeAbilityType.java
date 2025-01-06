package com.github.shap_po.shappoli.integration.walkers.ability.type;

import io.github.apace100.apoli.action.ActionConfiguration;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.NotNull;
import tocraft.walkers.ability.impl.specific.SheepAbility;

public class SheepShapeAbilityType extends ShapeAbilityType {
    private final SheepAbility<SheepEntity> ability = new SheepAbility<>();

    @Override
    protected void execute(PlayerEntity player, LivingEntity shape) {
        if (!(shape instanceof SheepEntity sheep)) return;
        ability.onUse(player, sheep, player.getWorld());
    }

    @Override
    public @NotNull ActionConfiguration<?> getConfig() {
        return ShapeAbilityTypes.SHEEP;
    }
}
