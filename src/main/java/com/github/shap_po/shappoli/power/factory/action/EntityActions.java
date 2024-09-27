package com.github.shap_po.shappoli.power.factory.action;

import com.github.shap_po.shappoli.power.ReceiveActionPower;
import com.github.shap_po.shappoli.power.factory.action.entity.SelfBientityActionAction;
import com.github.shap_po.shappoli.power.factory.action.meta.SendActionAction;
import io.github.apace100.apoli.action.factory.ActionTypeFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import net.minecraft.entity.Entity;
import net.minecraft.registry.Registry;

public class EntityActions {
    public static void register() {
        register(SelfBientityActionAction.getFactory());

        register(
            SendActionAction.getFactory(
                entity -> entity,
                ReceiveActionPower::receiveEntityAction,
                io.github.apace100.apoli.action.factory.EntityActions.ALIASES
            )
        );
    }

    private static void register(ActionTypeFactory<Entity> ActionTypeFactory) {
        Registry.register(ApoliRegistries.ENTITY_ACTION, ActionTypeFactory.getSerializerId(), ActionTypeFactory);
    }
}
