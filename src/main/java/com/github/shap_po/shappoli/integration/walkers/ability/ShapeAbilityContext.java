package com.github.shap_po.shappoli.integration.walkers.ability;

import io.github.apace100.apoli.condition.context.BiEntityConditionContext;
import io.github.apace100.apoli.util.context.TypeActionContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;

public record ShapeAbilityContext(PlayerEntity player, LivingEntity shape) implements TypeActionContext<BiEntityConditionContext> {
    @Override
    public BiEntityConditionContext forCondition() {
        return new BiEntityConditionContext(player(), shape());
    }
}
