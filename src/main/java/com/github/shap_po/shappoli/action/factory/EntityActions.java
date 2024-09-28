package com.github.shap_po.shappoli.action.factory;

import com.github.shap_po.shappoli.power.type.ReceiveActionPowerType;
import com.github.shap_po.shappoli.action.type.entity.SelfBientityActionActionType;
import com.github.shap_po.shappoli.action.type.meta.SendActionActionType;
import io.github.apace100.apoli.action.factory.ActionTypeFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import net.minecraft.entity.Entity;
import net.minecraft.registry.Registry;

public class EntityActions {
    public static void register() {
        register(SelfBientityActionActionType.getFactory());

        register(
            SendActionActionType.getFactory(
                entity -> entity,
                ReceiveActionPowerType::receiveEntityAction,
                io.github.apace100.apoli.action.factory.EntityActions.ALIASES
            )
        );
    }

    private static void register(ActionTypeFactory<Entity> ActionTypeFactory) {
        Registry.register(ApoliRegistries.ENTITY_ACTION, ActionTypeFactory.getSerializerId(), ActionTypeFactory);
    }
}
