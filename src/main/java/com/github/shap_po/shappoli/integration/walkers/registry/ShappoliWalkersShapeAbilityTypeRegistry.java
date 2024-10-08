package com.github.shap_po.shappoli.integration.walkers.registry;

import com.github.shap_po.shappoli.Shappoli;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import tocraft.walkers.ability.ShapeAbility;
import tocraft.walkers.ability.impl.generic.*;
import tocraft.walkers.ability.impl.specific.*;

public class ShappoliWalkersShapeAbilityTypeRegistry {
    public static void register() {
        register(Shappoli.identifier("clear_effects"), ClearEffectsAbility.class);
        register(Shappoli.identifier("explosion"), ExplosionAbility.class);
        register(Shappoli.identifier("jump_ability"), JumpAbility.class);
        register(Shappoli.identifier("random_teleportation"), RandomTeleportationAbility.class);
        register(Shappoli.identifier("saturate"), SaturateAbility.class);
        register(Shappoli.identifier("shoot_dragon_fireball"), ShootDragonFireball.class);
        register(Shappoli.identifier("shoot_fireball"), ShootFireballAbility.class);
        register(Shappoli.identifier("teleportation"), TeleportationAbility.class);
        register(Shappoli.identifier("throw_potions"), ThrowPotionsAbility.class);

        register(Shappoli.identifier("anger"), AngerAbility.class);
        register(Shappoli.identifier("chicken"), ChickenAbility.class);
        register(Shappoli.identifier("evoker"), EvokerAbility.class);
        register(Shappoli.identifier("grass_eater"), GrassEaterAbility.class);
        register(Shappoli.identifier("llama"), LlamaAbility.class);
        register(Shappoli.identifier("pufferfish"), PufferfishAbility.class);
        register(Shappoli.identifier("rabbit"), RabbitAbility.class);
        register(Shappoli.identifier("raid"), RaidAbility.class);
        register(Shappoli.identifier("sheep"), SheepAbility.class);
        register(Shappoli.identifier("shulker"), ShulkerAbility.class);
        register(Shappoli.identifier("sniffer"), SnifferAbility.class);
        register(Shappoli.identifier("turtle"), TurtleAbility.class);
        register(Shappoli.identifier("warden"), WardenAbility.class);
        register(Shappoli.identifier("wither"), WitherAbility.class);
    }

    private static <T extends ShapeAbility<?>> void register(Identifier id, Class<T> abilityClass) {
        Registry.register(ShappoliWalkersRegistries.SHAPE_ABILITY_TYPE, id, abilityClass);
    }
}
