package com.github.shap_po.shappoli.integration.walkers.power.factory.ability;

import com.github.shap_po.shappoli.Shappoli;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Pair;
import tocraft.walkers.ability.impl.generic.ShootFireballAbility;

public class ShootFireballShapeAbility {
    public static void useAbility(SerializableData.Instance data, Pair<ServerPlayerEntity, LivingEntity> playerAndShape) {
        new ShootFireballAbility<>(data.getBoolean("is_large"))
            .onUse(playerAndShape.getLeft(), null, playerAndShape.getLeft().getWorld());
    }

    public static ShapeAbilityFactory getFactory() {
        return new ShapeAbilityFactory(
            Shappoli.identifier("shoot_fireball"),
            new SerializableData()
                .add("is_large", SerializableDataTypes.BOOLEAN, false)
            ,
            ShootFireballShapeAbility::useAbility
        );
    }
}
