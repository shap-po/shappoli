package com.github.shap_po.shappoli.integration.walkers.ability.factory;

import com.github.shap_po.shappoli.integration.walkers.ability.type.*;
import com.github.shap_po.shappoli.integration.walkers.registry.ShappoliWalkersRegistries;
import net.minecraft.entity.mob.Angerable;
import net.minecraft.entity.mob.ShulkerEntity;
import net.minecraft.entity.passive.PufferfishEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.registry.Registry;
import tocraft.walkers.ability.impl.generic.*;
import tocraft.walkers.ability.impl.specific.*;

public class ShapeAbilities {
    public static void register() {
        register(SimpleShapeAbility.getFactory(ClearEffectsAbility.ID, new ClearEffectsAbility<>()));
        register(ExplosionShapeAbility.getFactory());
        register(GetItemShapeAbility.getFactory());
        register(SimpleShapeAbility.getFactory(JumpAbility.ID, new JumpAbility<>()));
        register(SimpleShapeAbility.getFactory(RandomTeleportationAbility.ID, new RandomTeleportationAbility<>()));
        register(SaturateShapeAbility.getFactory());
        register(SimpleShapeAbility.getFactory(ShootDragonFireball.ID, new ShootDragonFireball<>()));
        register(ShootFireballShapeAbility.getFactory());
        register(SimpleShapeAbility.getFactory(ShootSnowballAbility.ID, new ShootSnowballAbility<>()));
        register(SimpleShapeAbility.getFactory(TeleportationAbility.ID, new TeleportationAbility<>()));
        register(ThrowPotionsShapeAbility.getFactory());

        register(SimpleShapeAbility.getFactory(AngerAbility.ID, new AngerAbility<>(), (player, shape) -> shape instanceof Angerable));
        register(SimpleShapeAbility.getFactory(ChickenAbility.ID, new ChickenAbility<>()));
        register(SimpleShapeAbility.getFactory(EvokerAbility.ID, new EvokerAbility<>()));
        register(SimpleShapeAbility.getFactory(GrassEaterAbility.ID, new GrassEaterAbility<>()));
        register(SimpleShapeAbility.getFactory(LlamaAbility.ID, new LlamaAbility<>()));
        register(SimpleShapeAbility.getFactory(PufferfishAbility.ID, new PufferfishAbility<>(), (player, shape) -> shape instanceof PufferfishEntity));
        register(SimpleShapeAbility.getFactory(RabbitAbility.ID, new RabbitAbility<>()));
        register(SimpleShapeAbility.getFactory(RaidAbility.ID, new RaidAbility<>()));
        register(SimpleShapeAbility.getFactory(SheepAbility.ID, new SheepAbility<>(), (player, shape) -> shape instanceof SheepEntity));
        register(SimpleShapeAbility.getFactory(ShulkerAbility.ID, new ShulkerAbility<>(), (player, shape) -> shape instanceof ShulkerEntity));
        register(SimpleShapeAbility.getFactory(SnifferAbility.ID, new SnifferAbility<>()));
        register(SimpleShapeAbility.getFactory(TurtleAbility.ID, new TurtleAbility<>()));
        register(SimpleShapeAbility.getFactory(WardenAbility.ID, new WardenAbility<>()));
        register(SimpleShapeAbility.getFactory(WitherAbility.ID, new WitherAbility<>()));
    }

    private static void register(ShapeAbilityFactory<?> shapeAbilityFactory) {
        Registry.register(ShappoliWalkersRegistries.SHAPE_ABILITY, shapeAbilityFactory.getSerializerId(), shapeAbilityFactory);
    }
}
