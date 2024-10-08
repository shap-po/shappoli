package com.github.shap_po.shappoli.power.factory.action.entity;

import com.github.shap_po.shappoli.Shappoli;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.apoli.power.factory.action.EntityActions;
import io.github.apace100.calio.data.SerializableData;
import net.minecraft.entity.Entity;
import net.minecraft.util.Pair;

import java.util.function.Consumer;

public class SelfBientityActionAction {
    public static void action(SerializableData.Instance data, Entity entity) {
        Consumer<Pair<Entity, Entity>> biEntityAction = data.get("action");
        if (biEntityAction != null) {
            biEntityAction.accept(new Pair<>(entity, entity));
        }
    }

    public static ActionFactory<Entity> getFactory() {
        ActionFactory<Entity> factory = new ActionFactory<>(
            Shappoli.identifier("self_bientity_action"),
            new SerializableData()
                .add("bientity_action", ApoliDataTypes.BIENTITY_ACTION, null)
                .addFunctionedDefault("action", ApoliDataTypes.BIENTITY_ACTION, data -> data.get("bientity_action"))
            ,
            SelfBientityActionAction::action
        );

        EntityActions.ALIASES.addPathAlias("self_bientity", factory.getSerializerId().getPath());
        EntityActions.ALIASES.addPathAlias("bientity_action", factory.getSerializerId().getPath());
        return factory;
    }
}
