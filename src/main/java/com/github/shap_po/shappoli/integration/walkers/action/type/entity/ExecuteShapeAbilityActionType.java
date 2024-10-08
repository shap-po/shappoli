package com.github.shap_po.shappoli.integration.walkers.action.type.entity;

import com.github.shap_po.shappoli.Shappoli;
import com.github.shap_po.shappoli.integration.walkers.ability.factory.ShapeAbilityFactory;
import com.github.shap_po.shappoli.integration.walkers.data.ShappoliWalkersDataTypes;
import com.github.shap_po.shappoli.integration.walkers.util.WalkersUtil;
import io.github.apace100.apoli.action.factory.ActionTypeFactory;
import io.github.apace100.calio.data.SerializableData;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;

public class ExecuteShapeAbilityActionType {
    public static void action(Entity entity, ShapeAbilityFactory.Instance shapeAbility) {
        if (!(entity instanceof ServerPlayerEntity player)) {
            return;
        }

        shapeAbility.accept(WalkersUtil.getPlayerShapePair(player));
    }

    public static ActionTypeFactory<Entity> getFactory() {
        return new ActionTypeFactory<>(
            Shappoli.identifier("execute_shape_ability"),
            new SerializableData()
                .add("ability", ShappoliWalkersDataTypes.SHAPE_ABILITY)
            ,
            (data, entity) -> action(entity, data.get("ability"))
        );
    }
}
