package com.github.shap_po.shappoli.integration.walkers.power.factory.ability;

import com.github.shap_po.shappoli.Shappoli;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Pair;
import tocraft.walkers.ability.impl.generic.ExplosionAbility;

public class ExplosionShapeAbility {
    public static void useAbility(SerializableData.Instance data, Pair<ServerPlayerEntity, LivingEntity> playerAndShape) {
        new ExplosionAbility<>(data.getFloat("explosion_radius"))
            .onUse(playerAndShape.getLeft(), null, playerAndShape.getLeft().getWorld());
    }

    public static ShapeAbilityFactory getFactory() {
        return new ShapeAbilityFactory(
            Shappoli.identifier("explosion"),
            new SerializableData()
                .add("explosion_radius", SerializableDataTypes.POSITIVE_DOUBLE, 3.0)
            ,
            ExplosionShapeAbility::useAbility
        );
    }
}
