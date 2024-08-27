package com.github.shap_po.shappoli.power.factory.action;

import com.github.shap_po.shappoli.power.ActionOnEventReceivePower;
import com.github.shap_po.shappoli.power.factory.action.meta.SendEventAction;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import net.minecraft.entity.Entity;
import net.minecraft.registry.Registry;

public class EntityActions {
    public static void register() {
        register(
            SendEventAction.getFactory(
                ApoliDataTypes.ENTITY_ACTION, ApoliDataTypes.ENTITY_CONDITION,
                entity -> entity,
                entity -> entity,
                ActionOnEventReceivePower::receiveEntityEvent
            )
        );
        SendEventAction.addAlias(io.github.apace100.apoli.power.factory.action.EntityActions.ALIASES);
    }

    private static void register(ActionFactory<Entity> actionFactory) {
        Registry.register(ApoliRegistries.ENTITY_ACTION, actionFactory.getSerializerId(), actionFactory);
    }
}
