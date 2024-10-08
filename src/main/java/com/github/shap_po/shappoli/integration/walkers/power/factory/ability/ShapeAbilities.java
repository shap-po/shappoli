package com.github.shap_po.shappoli.integration.walkers.power.factory.ability;

import com.github.shap_po.shappoli.integration.walkers.registry.ShappoliWalkersRegistries;
import net.minecraft.registry.Registry;

public class ShapeAbilities {
    public static void register() {
        register(AngerShapeAbility.getFactory());
        register(ChickenShapeAbility.getFactory());
        register(ClearEffectsShapeAbility.getFactory());
        register(EvokerShapeAbility.getFactory());
        register(ExplosionShapeAbility.getFactory());
        register(GrassEaterShapeAbility.getFactory());
        register(JumpShapeAbility.getFactory());
        register(LlamaShapeAbility.getFactory());
        register(PufferfishShapeAbility.getFactory());
        register(RabbitShapeAbility.getFactory());
        register(RaidShapeAbility.getFactory());
        register(RandomTeleportationShapeAbility.getFactory());
        register(SaturateShapeAbility.getFactory());
        register(SheepShapeAbility.getFactory());
        register(ShootDragonFireballShapeAbility.getFactory());
        register(ShootFireballShapeAbility.getFactory());
        register(ShootSnowballShapeAbility.getFactory());
        register(ShulkerShapeAbility.getFactory());
        register(SnifferShapeAbility.getFactory());
        register(TeleportationShapeAbility.getFactory());
        register(ThrowPotionsShapeAbility.getFactory());
        register(TurtleShapeAbility.getFactory());
        register(WardenShapeAbility.getFactory());
        register(WitherShapeAbility.getFactory());
    }

    private static void register(ShapeAbilityFactory shapeAbilityFactory) {
        Registry.register(ShappoliWalkersRegistries.SHAPE_ABILITY, shapeAbilityFactory.getSerializerId(), shapeAbilityFactory);
    }
}
