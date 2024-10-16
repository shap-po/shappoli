package com.github.shap_po.shappoli.integration.origins.action.factory;

import com.github.shap_po.shappoli.integration.origins.action.type.bientity.CopyOriginActionType;
import io.github.apace100.apoli.action.factory.ActionTypeFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import net.minecraft.entity.Entity;
import net.minecraft.registry.Registry;
import net.minecraft.util.Pair;

public class BiEntityActionTypes {
    public static void register() {
        register(CopyOriginActionType.getFactory());
    }

    private static void register(ActionTypeFactory<Pair<Entity, Entity>> ActionTypeFactory) {
        Registry.register(ApoliRegistries.BIENTITY_ACTION, ActionTypeFactory.getSerializerId(), ActionTypeFactory);
    }
}
