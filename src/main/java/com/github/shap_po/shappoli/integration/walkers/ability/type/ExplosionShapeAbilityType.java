package com.github.shap_po.shappoli.integration.walkers.ability.type;

import io.github.apace100.apoli.action.ActionConfiguration;
import io.github.apace100.apoli.data.TypedDataObjectFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.NotNull;
import tocraft.walkers.ability.impl.generic.ExplosionAbility;

public class ExplosionShapeAbilityType extends ShapeAbilityType {
    public static final TypedDataObjectFactory<ExplosionShapeAbilityType> DATA_FACTORY = TypedDataObjectFactory.simple(
        new SerializableData()
            .add("explosion_radius", SerializableDataTypes.POSITIVE_FLOAT, 3.0f),
        data -> new ExplosionShapeAbilityType(data.getFloat("explosion_radius")),
        (abilityType, serializableData) -> serializableData.instance()
            .set("explosion_radius", abilityType.explosionRadius)
    );

    private final float explosionRadius;
    private final ExplosionAbility<LivingEntity> ability;

    public ExplosionShapeAbilityType(float explosionRadius) {
        this.explosionRadius = explosionRadius;
        this.ability = new ExplosionAbility<>(explosionRadius);
    }

    @Override
    protected void execute(PlayerEntity player, LivingEntity shape) {
        ability.onUse(player, shape, player.getWorld());
    }

    @Override
    public @NotNull ActionConfiguration<?> getConfig() {
        return ShapeAbilityTypes.EXPLOSION;
    }
}
