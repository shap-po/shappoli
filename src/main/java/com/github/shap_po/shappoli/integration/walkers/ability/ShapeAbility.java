package com.github.shap_po.shappoli.integration.walkers.ability;

import com.github.shap_po.shappoli.integration.walkers.ability.type.ShapeAbilityType;
import com.github.shap_po.shappoli.integration.walkers.ability.type.ShapeAbilityTypes;
import com.github.shap_po.shappoli.integration.walkers.ability.type.meta.SequenceShapeAbilityType;
import com.github.shap_po.shappoli.integration.walkers.util.WalkersUtil;
import io.github.apace100.apoli.action.AbstractAction;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.calio.data.SerializableDataType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;

public final class ShapeAbility extends AbstractAction<ShapeAbilityContext, ShapeAbilityType> {
    public static final SerializableDataType<ShapeAbility> DATA_TYPE = SerializableDataType.lazy(() -> ApoliDataTypes.actions("type", ShapeAbilityTypes.DATA_TYPE, SequenceShapeAbilityType::new, ShapeAbility::new));

    public ShapeAbility(ShapeAbilityType actionType) {
        super(actionType);
    }

    public void execute(PlayerEntity player, LivingEntity shape) {
        accept(new ShapeAbilityContext(player, shape));
    }

    public void execute(PlayerEntity player) {
        execute(player, WalkersUtil.getShape(player));
    }
}
