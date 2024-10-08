package com.github.shap_po.shappoli.integration.walkers.registry;

import com.github.shap_po.shappoli.Shappoli;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import tocraft.walkers.ability.ShapeAbility;

public class ShappoliWalkersRegistryKeys {
    public static final RegistryKey<Registry<Class<? extends ShapeAbility<?>>>> SHAPE_ABILITY_TYPE;

    static {
        SHAPE_ABILITY_TYPE = create("shape_ability_type");
    }

    private static <T> RegistryKey<Registry<T>> create(String path) {
        return RegistryKey.ofRegistry(Shappoli.identifier(path));
    }
}
