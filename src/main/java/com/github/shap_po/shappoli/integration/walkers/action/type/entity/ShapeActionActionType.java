package com.github.shap_po.shappoli.integration.walkers.action.type.entity;

import com.github.shap_po.shappoli.Shappoli;
import com.github.shap_po.shappoli.integration.walkers.util.WalkersUtil;
import io.github.apace100.apoli.action.factory.ActionTypeFactory;
import io.github.apace100.apoli.action.factory.EntityActions;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.calio.data.SerializableData;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Pair;

import java.util.function.Consumer;

public class ShapeActionActionType {
    public static void action(Entity entity, Consumer<Pair<Entity, Entity>> action) {
        if (!(entity instanceof ServerPlayerEntity player)) {
            return;
        }

        action.accept(new Pair<>(entity, WalkersUtil.getShape(player)));
    }

    public static ActionTypeFactory<Entity> getFactory() {
        ActionTypeFactory<Entity> factory = new ActionTypeFactory<>(
            Shappoli.identifier("shape_action"),
            new SerializableData()
                .add("bientity_action", ApoliDataTypes.BIENTITY_ACTION)
            ,
            (data, entity) -> action(entity, data.get("bientity_action"))
        );

        EntityActions.ALIASES.addPathAlias("action_on_shape", factory.getSerializerId().getPath());
        return factory;
    }
}
