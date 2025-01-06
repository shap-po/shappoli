package com.github.shap_po.shappoli.integration.walkers.ability.type;

import com.github.shap_po.shappoli.integration.walkers.ability.ShapeAbility;
import com.github.shap_po.shappoli.integration.walkers.ability.ShapeAbilityContext;
import io.github.apace100.apoli.action.type.AbstractActionType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;

public abstract class ShapeAbilityType extends AbstractActionType<ShapeAbilityContext, ShapeAbility> {
    @Override
    public void accept(ShapeAbilityContext context) {
        execute(context.player(), context.shape());
    }

    @Override
    public ShapeAbility createAction() {
        return new ShapeAbility(this);
    }

    protected abstract void execute(PlayerEntity player, LivingEntity shape);
}
