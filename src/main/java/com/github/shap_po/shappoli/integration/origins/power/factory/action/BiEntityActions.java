package com.github.shap_po.shappoli.integration.origins.power.factory.action;

import com.github.shap_po.shappoli.integration.origins.power.factory.action.bientity.CopyOriginAction;
import io.github.apace100.apoli.action.factory.ActionTypeFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import net.minecraft.entity.Entity;
import net.minecraft.registry.Registry;
import net.minecraft.util.Pair;

public class BiEntityActions {
    public static void register() {
        register(CopyOriginAction.getFactory());
    }

    private static void register(ActionTypeFactory<Pair<Entity, Entity>> ActionTypeFactory) {
        Registry.register(ApoliRegistries.BIENTITY_ACTION, ActionTypeFactory.getSerializerId(), ActionTypeFactory);
    }
}
