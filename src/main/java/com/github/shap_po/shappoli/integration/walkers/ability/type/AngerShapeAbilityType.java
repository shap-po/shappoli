package com.github.shap_po.shappoli.integration.walkers.ability.type;

import io.github.apace100.apoli.action.ActionConfiguration;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.NotNull;
import tocraft.walkers.ability.impl.specific.AngerAbility;

public class AngerShapeAbilityType extends ShapeAbilityType {
    private final AngerAbility<MobEntity> ability = new AngerAbility<>();

    @Override
    protected void execute(PlayerEntity player, LivingEntity shape) {
        if (!(shape instanceof MobEntity mob)) return;
        ability.onUse(player, mob, player.getWorld());
    }

    @Override
    public @NotNull ActionConfiguration<?> getConfig() {
        return ShapeAbilityTypes.ANGER;
    }
}
