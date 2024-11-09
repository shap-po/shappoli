package com.github.shap_po.shappoli.integration.walkers.ability.type;

import com.github.shap_po.shappoli.integration.walkers.ability.factory.ShapeAbilityFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import tocraft.walkers.ability.impl.generic.ExplosionAbility;

public class ExplosionShapeAbility {
    public static void useAbility(ServerPlayerEntity player, LivingEntity shape, float radius) {
        new ExplosionAbility<>(radius)
            .onUse(player, shape, shape.getWorld());
    }

    public static ShapeAbilityFactory<LivingEntity> getFactory() {
        return new ShapeAbilityFactory<>(
            ExplosionAbility.ID,
            new SerializableData()
                .add("explosion_radius", SerializableDataTypes.POSITIVE_DOUBLE, 3.0)
            ,
            (data, playerAndShape) -> useAbility(
                playerAndShape.getLeft(), playerAndShape.getRight(),
                data.getFloat("explosion_radius")
            )
        );
    }
}
