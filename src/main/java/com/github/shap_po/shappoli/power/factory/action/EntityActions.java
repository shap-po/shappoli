package com.github.shap_po.shappoli.power.factory.action;

import com.github.shap_po.shappoli.power.factory.action.entity.ModifyTrinketAction;
import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.Entity;
import net.minecraft.registry.Registry;

public class EntityActions {
    public static void register() {
        if (FabricLoader.getInstance().isModLoaded("trinkets")) {
            register(ModifyTrinketAction.getFactory());
        }
    }

    private static void register(ActionFactory<Entity> actionFactory) {
        Registry.register(ApoliRegistries.ENTITY_ACTION, actionFactory.getSerializerId(), actionFactory);
    }
}
