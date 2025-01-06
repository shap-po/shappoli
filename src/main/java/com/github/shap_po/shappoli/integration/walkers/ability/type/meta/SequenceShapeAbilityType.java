package com.github.shap_po.shappoli.integration.walkers.ability.type.meta;

import com.github.shap_po.shappoli.integration.walkers.ability.ShapeAbility;
import com.github.shap_po.shappoli.integration.walkers.ability.ShapeAbilityContext;
import com.github.shap_po.shappoli.integration.walkers.ability.type.ShapeAbilityType;
import com.github.shap_po.shappoli.integration.walkers.ability.type.ShapeAbilityTypes;
import io.github.apace100.apoli.action.ActionConfiguration;
import io.github.apace100.apoli.action.type.meta.SequenceMetaActionType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SequenceShapeAbilityType extends ShapeAbilityType implements SequenceMetaActionType<ShapeAbilityContext, ShapeAbility> {
    private final List<ShapeAbility> actions;

    public SequenceShapeAbilityType(List<ShapeAbility> actions) {
        this.actions = actions;
    }

    @Override
    protected void execute(PlayerEntity player, LivingEntity shape) {
        executeActions(new ShapeAbilityContext(player, shape));
    }

    @Override
    public @NotNull ActionConfiguration<?> getConfig() {
        return ShapeAbilityTypes.SEQUENCE;
    }

    @Override
    public List<ShapeAbility> actions() {
        return actions;
    }
}
