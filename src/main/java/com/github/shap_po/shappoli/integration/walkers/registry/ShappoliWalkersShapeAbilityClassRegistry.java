package com.github.shap_po.shappoli.integration.walkers.registry;

import com.github.shap_po.shappoli.Shappoli;
import io.github.apace100.apoli.Apoli;
import io.github.apace100.apoli.power.PowerConfiguration;
import io.github.apace100.apoli.power.type.PowerType;
import io.github.apace100.apoli.registry.ApoliRegistries;
import io.github.apace100.calio.data.SerializableDataType;
import io.github.apace100.calio.util.IdentifierAlias;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import tocraft.walkers.Walkers;
import tocraft.walkers.ability.ShapeAbility;
import tocraft.walkers.ability.impl.generic.*;
import tocraft.walkers.ability.impl.specific.*;

public class ShappoliWalkersShapeAbilityClassRegistry {
    public static final IdentifierAlias ALIASES = new IdentifierAlias();
    public static final SerializableDataType<Class<? extends ShapeAbility<?>>> DATA_TYPE = SerializableDataType.registry(ShappoliWalkersRegistries.SHAPE_ABILITY_CLASS, Walkers.MODID, ALIASES, (clazz, id) -> "Shape ability " + id + "is undefined!");

    public static void register() {
        register(ClearEffectsAbility.ID, ClearEffectsAbility.class);
        register(ExplosionAbility.ID, ExplosionAbility.class);
        register(GetItemAbility.ID, GetItemAbility.class);
        register(JumpAbility.ID, JumpAbility.class);
        register(RandomTeleportationAbility.ID, RandomTeleportationAbility.class);
        register(SaturateAbility.ID, SaturateAbility.class);
        register(ShootDragonFireball.ID, ShootDragonFireball.class);
        register(ShootFireballAbility.ID, ShootFireballAbility.class);
        register(ShootSnowballAbility.ID, ShootSnowballAbility.class);
        register(TeleportationAbility.ID, TeleportationAbility.class);
        register(ThrowPotionsAbility.ID, ThrowPotionsAbility.class);

        register(AngerAbility.ID, AngerAbility.class);
        register(ChickenAbility.ID, ChickenAbility.class);
        register(EvokerAbility.ID, EvokerAbility.class);
        register(GrassEaterAbility.ID, GrassEaterAbility.class);
        register(LlamaAbility.ID, LlamaAbility.class);
        register(PufferfishAbility.ID, PufferfishAbility.class);
        register(RabbitAbility.ID, RabbitAbility.class);
        register(RaidAbility.ID, RaidAbility.class);
        register(SheepAbility.ID, SheepAbility.class);
        register(ShulkerAbility.ID, ShulkerAbility.class);
        register(SnifferAbility.ID, SnifferAbility.class);
        register(TurtleAbility.ID, TurtleAbility.class);
        register(WardenAbility.ID, WardenAbility.class);
        register(WitherAbility.ID, WitherAbility.class);

        ALIASES.addNamespaceAlias(Shappoli.MOD_ID, Walkers.MODID);
    }

    private static <T extends ShapeAbility<?>> void register(Identifier id, Class<T> abilityClass) {
        Registry.register(ShappoliWalkersRegistries.SHAPE_ABILITY_CLASS, id, abilityClass);
    }
}
