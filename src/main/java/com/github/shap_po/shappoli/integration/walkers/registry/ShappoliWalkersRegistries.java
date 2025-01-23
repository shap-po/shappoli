package com.github.shap_po.shappoli.integration.walkers.registry;

import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import tocraft.walkers.ability.ShapeAbility;


public class ShappoliWalkersRegistries {
    public static final Registry<Class<? extends ShapeAbility<?>>> SHAPE_ABILITY_CLASS = create(ShappoliWalkersRegistryKeys.SHAPE_ABILITY_CLASS);

    private static <T> Registry<T> create(RegistryKey<Registry<T>> registryKey) {
        return FabricRegistryBuilder.createSimple(registryKey).buildAndRegister();
    }
}
