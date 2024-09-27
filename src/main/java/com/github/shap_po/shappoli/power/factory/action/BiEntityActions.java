package com.github.shap_po.shappoli.power.factory.action;

import com.github.shap_po.shappoli.power.ReceiveActionPower;
import com.github.shap_po.shappoli.power.factory.action.bientity.SuppressPowerAction;
import com.github.shap_po.shappoli.power.factory.action.bientity.TeleportAction;
import com.github.shap_po.shappoli.power.factory.action.meta.SendActionAction;
import io.github.apace100.apoli.action.factory.ActionTypeFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import net.minecraft.entity.Entity;
import net.minecraft.registry.Registry;
import net.minecraft.util.Pair;

public class BiEntityActions {
    public static void register() {
        register(SuppressPowerAction.getFactory());
        register(TeleportAction.getFactory());

        register(
            SendActionAction.getFactory(
                Pair::getLeft,
                ReceiveActionPower::receiveBientityAction,
                io.github.apace100.apoli.action.factory.BiEntityActions.ALIASES
            )
        );
    }

    private static void register(ActionTypeFactory<Pair<Entity, Entity>> ActionTypeFactory) {
        Registry.register(ApoliRegistries.BIENTITY_ACTION, ActionTypeFactory.getSerializerId(), ActionTypeFactory);
    }
}
