package com.github.shap_po.shappoli.power.factory.action;

import com.github.shap_po.shappoli.power.ReceiveActionPower;
import com.github.shap_po.shappoli.power.factory.action.bientity.SuppressPowerAction;
import com.github.shap_po.shappoli.power.factory.action.bientity.TeleportAction;
import com.github.shap_po.shappoli.power.factory.action.meta.SendActionAction;
import io.github.apace100.apoli.power.factory.action.ActionFactory;
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
                ReceiveActionPower::receiveBientityEvent,
                io.github.apace100.apoli.power.factory.action.BiEntityActions.ALIASES
            )
        );
    }

    private static void register(ActionFactory<Pair<Entity, Entity>> actionFactory) {
        Registry.register(ApoliRegistries.BIENTITY_ACTION, actionFactory.getSerializerId(), actionFactory);
    }
}
