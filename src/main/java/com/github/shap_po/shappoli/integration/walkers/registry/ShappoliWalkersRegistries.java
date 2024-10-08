package com.github.shap_po.shappoli.integration.walkers.registry;

import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import tocraft.walkers.ability.ShapeAbility;


public class ShappoliWalkersRegistries {
    public static final Registry<Class<? extends ShapeAbility<?>>> SHAPE_ABILITY_TYPE;

    static {
        SHAPE_ABILITY_TYPE = create(ShappoliWalkersRegistryKeys.SHAPE_ABILITY_TYPE);
    }

    private static <T> Registry<T> create(RegistryKey<Registry<T>> registryKey) {
        return FabricRegistryBuilder.createSimple(registryKey).buildAndRegister();
    }
}
