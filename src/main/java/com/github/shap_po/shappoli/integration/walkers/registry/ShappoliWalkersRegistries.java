package com.github.shap_po.shappoli.integration.walkers.registry;

import com.github.shap_po.shappoli.integration.walkers.power.factory.ability.ShapeAbilityFactory;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import tocraft.walkers.ability.ShapeAbility;


public class ShappoliWalkersRegistries {
    public static final Registry<Class<? extends ShapeAbility<?>>> SHAPE_ABILITY_TYPE;
    public static final Registry<ShapeAbilityFactory> SHAPE_ABILITY;

    static {
        SHAPE_ABILITY_TYPE = create(ShappoliWalkersRegistryKeys.SHAPE_ABILITY_TYPE);
        SHAPE_ABILITY = create(ShappoliWalkersRegistryKeys.SHAPE_ABILITY);
    }

    private static <T> Registry<T> create(RegistryKey<Registry<T>> registryKey) {
        return FabricRegistryBuilder.createSimple(registryKey).buildAndRegister();
    }
}
