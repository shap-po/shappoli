package com.github.shap_po.shappoli.integration.walkers.action.type.entity;

import com.github.shap_po.shappoli.integration.walkers.action.type.ShappoliWalkersEntityActionTypes;
import com.github.shap_po.shappoli.integration.walkers.util.WalkersUtil;
import io.github.apace100.apoli.action.ActionConfiguration;
import io.github.apace100.apoli.action.BiEntityAction;
import io.github.apace100.apoli.action.context.EntityActionContext;
import io.github.apace100.apoli.action.type.EntityActionType;
import io.github.apace100.apoli.data.TypedDataObjectFactory;
import io.github.apace100.calio.data.SerializableData;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;

public class ShapeActionEntityActionType extends EntityActionType {
    public static final TypedDataObjectFactory<ShapeActionEntityActionType> DATA_FACTORY = TypedDataObjectFactory.simple(
        new SerializableData()
            .add("bientity_action", BiEntityAction.DATA_TYPE),
        data -> new ShapeActionEntityActionType(
            data.get("bientity_action")
        ),
        (actionType, serializableData) -> serializableData.instance()
            .set("bientity_action", actionType.biEntityAction)
    );

    private final BiEntityAction biEntityAction;

    public ShapeActionEntityActionType(BiEntityAction biEntityAction) {
        this.biEntityAction = biEntityAction;
    }

    @Override
    public void accept(EntityActionContext context) {
        if (!(context.entity() instanceof ServerPlayerEntity player)) {
            return;
        }
        biEntityAction.execute(context.entity(), WalkersUtil.getShape(player));
    }

    @Override
    public @NotNull ActionConfiguration<?> getConfig() {
        return ShappoliWalkersEntityActionTypes.SHAPE_ACTION;
    }

}
