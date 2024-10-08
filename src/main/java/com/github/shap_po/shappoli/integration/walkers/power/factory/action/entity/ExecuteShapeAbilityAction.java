package com.github.shap_po.shappoli.integration.walkers.power.factory.action.entity;

import com.github.shap_po.shappoli.Shappoli;
import com.github.shap_po.shappoli.integration.walkers.data.ShappoliWalkersDataTypes;
import com.github.shap_po.shappoli.integration.walkers.power.factory.ability.ShapeAbilityFactory;
import com.github.shap_po.shappoli.integration.walkers.util.WalkersUtil;
import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.calio.data.SerializableData;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;

public class ExecuteShapeAbilityAction {
    public static void action(SerializableData.Instance data, Entity entity) {
        if (!(entity instanceof ServerPlayerEntity player)) {
            return;
        }

        ShapeAbilityFactory.Instance shapeAbility = data.get("ability");
        if (shapeAbility == null) {
            return;
        }

        shapeAbility.accept(WalkersUtil.getPlayerShapePair(player));
    }

    public static ActionFactory<Entity> getFactory() {
        return new ActionFactory<>(
            Shappoli.identifier("execute_shape_ability"),
            new SerializableData()
                .add("ability", ShappoliWalkersDataTypes.SHAPE_ABILITY)
            ,
            ExecuteShapeAbilityAction::action
        );
    }
}
