package com.github.shap_po.shappoli.integration.walkers.condition.type.entity;

import com.github.shap_po.shappoli.integration.walkers.condition.type.ShappoliWalkersEntityConditionTypes;
import io.github.apace100.apoli.condition.ConditionConfiguration;
import io.github.apace100.apoli.condition.context.EntityConditionContext;
import io.github.apace100.apoli.condition.type.EntityConditionType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.NotNull;
import tocraft.walkers.api.PlayerAbilities;

public class CanUseShapeAbilityEntityConditionType extends EntityConditionType {
    @Override
    public boolean test(EntityConditionContext context) {
        if (!(context.entity() instanceof PlayerEntity player)) {
            return false;
        }
        return PlayerAbilities.canUseAbility(player);
    }

    @Override
    public @NotNull ConditionConfiguration<?> getConfig() {
        return ShappoliWalkersEntityConditionTypes.CAN_USE_SHAPE_ABILITY;
    }
}
