package com.github.shap_po.shappoli.integration.walkers.action.factory;

import com.github.shap_po.shappoli.integration.walkers.action.type.bientity.SwitchShapeActionType;
import io.github.apace100.apoli.action.factory.ActionTypeFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import net.minecraft.entity.Entity;
import net.minecraft.registry.Registry;
import net.minecraft.util.Pair;

public class BiEntityActionTypes {
    public static void register() {
        register(SwitchShapeActionType.getFactory());
    }

    private static void register(ActionTypeFactory<Pair<Entity, Entity>> ActionTypeFactory) {
        Registry.register(ApoliRegistries.BIENTITY_ACTION, ActionTypeFactory.getSerializerId(), ActionTypeFactory);
    }
}
