package com.github.shap_po.shappoli.integration.walkers.action.type.entity;

import com.github.shap_po.shappoli.integration.walkers.ability.ShapeAbility;
import com.github.shap_po.shappoli.integration.walkers.action.type.ShappoliWalkersEntityActionTypes;
import io.github.apace100.apoli.action.ActionConfiguration;
import io.github.apace100.apoli.action.context.EntityActionContext;
import io.github.apace100.apoli.action.type.EntityActionType;
import io.github.apace100.apoli.data.TypedDataObjectFactory;
import io.github.apace100.calio.data.SerializableData;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;

public class ExecuteShapeAbilityEntityActionType extends EntityActionType {
    public static final TypedDataObjectFactory<ExecuteShapeAbilityEntityActionType> DATA_FACTORY = TypedDataObjectFactory.simple(
        new SerializableData()
            .add("ability", ShapeAbility.DATA_TYPE),
        data -> new ExecuteShapeAbilityEntityActionType(data.get("ability")),
        (actionType, serializableData) -> serializableData.instance()
            .set("ability", actionType.ability)
    );

    private final ShapeAbility ability;

    public ExecuteShapeAbilityEntityActionType(ShapeAbility ability) {
        this.ability = ability;
    }

    @Override
    public void accept(EntityActionContext context) {
        if (!(context.entity() instanceof ServerPlayerEntity player)) {
            return;
        }

        ability.execute(player);
    }

    @Override
    public @NotNull ActionConfiguration<?> getConfig() {
        return ShappoliWalkersEntityActionTypes.EXECUTE_SHAPE_ABILITY;
    }
}
