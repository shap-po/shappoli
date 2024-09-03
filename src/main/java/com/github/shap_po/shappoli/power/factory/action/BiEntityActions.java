package com.github.shap_po.shappoli.power.factory.action;


import com.github.shap_po.shappoli.power.ActionOnEventReceivePower;
import com.github.shap_po.shappoli.power.factory.action.bientity.SuppressPowerAction;
import com.github.shap_po.shappoli.power.factory.action.meta.SendEventAction;
import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import net.minecraft.entity.Entity;
import net.minecraft.registry.Registry;
import net.minecraft.util.Pair;

public class BiEntityActions {
    public static void register() {
        register(SuppressPowerAction.getFactory());

        register(
            SendEventAction.getFactory(
                Pair::getLeft,
                ActionOnEventReceivePower::receiveBientityEvent
            )
        );
        SendEventAction.addAlias(io.github.apace100.apoli.power.factory.action.BiEntityActions.ALIASES);
    }

    private static void register(ActionFactory<Pair<Entity, Entity>> actionFactory) {
        Registry.register(ApoliRegistries.BIENTITY_ACTION, actionFactory.getSerializerId(), actionFactory);
    }
}
