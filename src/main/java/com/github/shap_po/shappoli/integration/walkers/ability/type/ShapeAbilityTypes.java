package com.github.shap_po.shappoli.integration.walkers.ability.type;

import com.github.shap_po.shappoli.Shappoli;
import com.github.shap_po.shappoli.integration.walkers.ability.ShapeAbility;
import com.github.shap_po.shappoli.integration.walkers.ability.type.meta.SequenceShapeAbilityType;
import com.github.shap_po.shappoli.integration.walkers.registry.ShappoliWalkersRegistries;
import io.github.apace100.apoli.action.ActionConfiguration;
import io.github.apace100.apoli.action.type.meta.SequenceMetaActionType;
import io.github.apace100.calio.data.SerializableDataType;
import io.github.apace100.calio.util.IdentifierAlias;
import net.minecraft.registry.Registry;
import tocraft.walkers.Walkers;
import tocraft.walkers.ability.impl.generic.*;
import tocraft.walkers.ability.impl.specific.*;

public class ShapeAbilityTypes {
    public static final IdentifierAlias ALIASES = new IdentifierAlias();
    public static final SerializableDataType<ActionConfiguration<ShapeAbilityType>> DATA_TYPE = SerializableDataType.registry(ShappoliWalkersRegistries.SHAPE_ABILITY_TYPE, Shappoli.MOD_ID, ALIASES, (configurations, id) -> "Shape ability type \"" + id + "\" is undefined!");

    public static final ActionConfiguration<SequenceShapeAbilityType> SEQUENCE = register(SequenceMetaActionType.createConfiguration(ShapeAbility.DATA_TYPE, SequenceShapeAbilityType::new));

    public static final ActionConfiguration<AngerShapeAbilityType> ANGER = register(ActionConfiguration.simple(AngerAbility.ID, AngerShapeAbilityType::new));
    public static final ActionConfiguration<ChickenShapeAbilityType> CHICKEN = register(ActionConfiguration.simple(ChickenAbility.ID, ChickenShapeAbilityType::new));
    public static final ActionConfiguration<ClearEffectsShapeAbilityType> CLEAR_EFFECTS = register(ActionConfiguration.simple(ClearEffectsAbility.ID, ClearEffectsShapeAbilityType::new));
    public static final ActionConfiguration<EvokerShapeAbilityType> EVOKER = register(ActionConfiguration.simple(EvokerAbility.ID, EvokerShapeAbilityType::new));
    public static final ActionConfiguration<ExplosionShapeAbilityType> EXPLOSION = register(ActionConfiguration.of(ExplosionAbility.ID, ExplosionShapeAbilityType.DATA_FACTORY));
    public static final ActionConfiguration<GetItemShapeAbilityType> GET_ITEM = register(ActionConfiguration.of(GetItemAbility.ID, GetItemShapeAbilityType.DATA_FACTORY));
    public static final ActionConfiguration<GrassEaterShapeAbilityType> GRASS_EATER = register(ActionConfiguration.simple(GrassEaterAbility.ID, GrassEaterShapeAbilityType::new));
    public static final ActionConfiguration<JumpShapeAbilityType> JUMP = register(ActionConfiguration.simple(JumpAbility.ID, JumpShapeAbilityType::new));
    public static final ActionConfiguration<LlamaShapeAbilityType> LLAMA = register(ActionConfiguration.simple(LlamaAbility.ID, LlamaShapeAbilityType::new));
    public static final ActionConfiguration<PufferfishShapeAbilityType> PUFFERFISH = register(ActionConfiguration.simple(PufferfishAbility.ID, PufferfishShapeAbilityType::new));
    public static final ActionConfiguration<RabbitShapeAbilityType> RABBIT = register(ActionConfiguration.simple(RabbitAbility.ID, RabbitShapeAbilityType::new));
    public static final ActionConfiguration<RaidShapeAbilityType> RAID = register(ActionConfiguration.simple(RaidAbility.ID, RaidShapeAbilityType::new));
    public static final ActionConfiguration<RandomTeleportationShapeAbilityType> RANDOM_TELEPORTATION = register(ActionConfiguration.simple(RandomTeleportationAbility.ID, RandomTeleportationShapeAbilityType::new));
    public static final ActionConfiguration<SaturateShapeAbilityType> SATURATE = register(ActionConfiguration.of(SaturateAbility.ID, SaturateShapeAbilityType.DATA_FACTORY));
    public static final ActionConfiguration<SheepShapeAbilityType> SHEEP = register(ActionConfiguration.simple(SheepAbility.ID, SheepShapeAbilityType::new));
    public static final ActionConfiguration<ShootDragonFireballShapeAbilityType> SHOOT_DRAGON_FIREBALL = register(ActionConfiguration.simple(ShootDragonFireball.ID, ShootDragonFireballShapeAbilityType::new));
    public static final ActionConfiguration<ShootFireballShapeAbilityType> SHOOT_FIREBALL = register(ActionConfiguration.of(ShootFireballAbility.ID, ShootFireballShapeAbilityType.DATA_FACTORY));
    public static final ActionConfiguration<ShootSnowballShapeAbilityType> SHOOT_SNOWBALL = register(ActionConfiguration.simple(ShootSnowballAbility.ID, ShootSnowballShapeAbilityType::new));
    public static final ActionConfiguration<ShulkerShapeAbilityType> SHULKER = register(ActionConfiguration.simple(ShulkerAbility.ID, ShulkerShapeAbilityType::new));
    public static final ActionConfiguration<SnifferShapeAbilityType> SNIFFER = register(ActionConfiguration.simple(SnifferAbility.ID, SnifferShapeAbilityType::new));
    public static final ActionConfiguration<TeleportationShapeAbilityType> TELEPORTATION = register(ActionConfiguration.simple(TeleportationAbility.ID, TeleportationShapeAbilityType::new));
    public static final ActionConfiguration<ThrowPotionsShapeAbilityType> THROW_POTIONS = register(ActionConfiguration.of(ThrowPotionsAbility.ID, ThrowPotionsShapeAbilityType.DATA_FACTORY));
    public static final ActionConfiguration<TurtleShapeAbilityType> TURTLE = register(ActionConfiguration.simple(TurtleAbility.ID, TurtleShapeAbilityType::new));
    public static final ActionConfiguration<WardenShapeAbilityType> WARDEN = register(ActionConfiguration.simple(WardenAbility.ID, WardenShapeAbilityType::new));
    public static final ActionConfiguration<WitherShapeAbilityType> WITHER = register(ActionConfiguration.simple(WitherAbility.ID, WitherShapeAbilityType::new));

    public static void register() {
        ALIASES.addNamespaceAlias(Shappoli.MOD_ID, Walkers.MODID);
        ALIASES.addPathAlias("and", SEQUENCE.id().getPath());
    }

    @SuppressWarnings("unchecked")
    public static <T extends ShapeAbilityType> ActionConfiguration<T> register(ActionConfiguration<T> configuration) {
        ActionConfiguration<ShapeAbilityType> casted = (ActionConfiguration<ShapeAbilityType>) configuration;
        Registry.register(ShappoliWalkersRegistries.SHAPE_ABILITY_TYPE, casted.id(), casted);

        return configuration;
    }
}
