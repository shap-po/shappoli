package com.github.shap_po.shappoli.integration.walkers.registry;

import com.github.shap_po.shappoli.Shappoli;
import com.github.shap_po.shappoli.integration.walkers.power.factory.ability.ShapeAbilityFactory;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import tocraft.walkers.ability.ShapeAbility;

public class ShappoliWalkersRegistryKeys {
    public static final RegistryKey<Registry<Class<? extends ShapeAbility<?>>>> SHAPE_ABILITY_TYPE;
    public static final RegistryKey<Registry<ShapeAbilityFactory>> SHAPE_ABILITY;

    static {
        SHAPE_ABILITY_TYPE = create("shape_ability_type");
        SHAPE_ABILITY = create("shape_ability");
    }

    private static <T> RegistryKey<Registry<T>> create(String path) {
        return RegistryKey.ofRegistry(Shappoli.identifier(path));
    }
}
