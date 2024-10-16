package com.github.shap_po.shappoli.action.factory;

import com.github.shap_po.shappoli.power.type.ReceiveActionPowerType;
import com.github.shap_po.shappoli.action.type.bientity.SuppressPowerActionType;
import com.github.shap_po.shappoli.action.type.bientity.TeleportActionType;
import com.github.shap_po.shappoli.action.type.meta.SendActionActionType;
import io.github.apace100.apoli.action.factory.ActionTypeFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import net.minecraft.entity.Entity;
import net.minecraft.registry.Registry;
import net.minecraft.util.Pair;

public class BiEntityActionTypes {
    public static void register() {
        register(SuppressPowerActionType.getFactory());
        register(TeleportActionType.getFactory());

        register(
            SendActionActionType.getFactory(
                Pair::getLeft,
                ReceiveActionPowerType::receiveBientityAction,
                io.github.apace100.apoli.action.type.BiEntityActionTypes.ALIASES
            )
        );
    }

    private static void register(ActionTypeFactory<Pair<Entity, Entity>> ActionTypeFactory) {
        Registry.register(ApoliRegistries.BIENTITY_ACTION, ActionTypeFactory.getSerializerId(), ActionTypeFactory);
    }
}
