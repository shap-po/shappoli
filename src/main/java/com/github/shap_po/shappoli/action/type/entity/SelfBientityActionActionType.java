package com.github.shap_po.shappoli.action.type.entity;

import com.github.shap_po.shappoli.Shappoli;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.action.factory.ActionTypeFactory;
import io.github.apace100.apoli.action.factory.EntityActions;
import io.github.apace100.calio.data.SerializableData;
import net.minecraft.entity.Entity;
import net.minecraft.util.Pair;

import java.util.function.Consumer;

public class SelfBientityActionActionType {
    public static void action(SerializableData.Instance data, Entity entity) {
        Consumer<Pair<Entity, Entity>> biEntityAction = data.get("action");
        if (biEntityAction != null) {
            biEntityAction.accept(new Pair<>(entity, entity));
        }
    }

    public static ActionTypeFactory<Entity> getFactory() {
        ActionTypeFactory<Entity> factory = new ActionTypeFactory<>(
            Shappoli.identifier("self_bientity_action"),
            new SerializableData()
                .add("bientity_action", ApoliDataTypes.BIENTITY_ACTION, null)
                .addFunctionedDefault("action", ApoliDataTypes.BIENTITY_ACTION, data -> data.get("bientity_action"))
            ,
            SelfBientityActionActionType::action
        );

        EntityActions.ALIASES.addPathAlias("self_bientity", factory.getSerializerId().getPath());
        EntityActions.ALIASES.addPathAlias("bientity_action", factory.getSerializerId().getPath());
        return factory;
    }
}
