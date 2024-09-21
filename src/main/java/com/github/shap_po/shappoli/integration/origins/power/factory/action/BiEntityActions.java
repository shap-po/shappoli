package com.github.shap_po.shappoli.integration.origins.power.factory.action;

import com.github.shap_po.shappoli.integration.origins.power.factory.action.bientity.TransferOriginAction;
import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import net.minecraft.entity.Entity;
import net.minecraft.registry.Registry;
import net.minecraft.util.Pair;

public class BiEntityActions {
    public static void register() {
        register(TransferOriginAction.getFactory());
    }

    private static void register(ActionFactory<Pair<Entity, Entity>> actionFactory) {
        Registry.register(ApoliRegistries.BIENTITY_ACTION, actionFactory.getSerializerId(), actionFactory);
    }
}
