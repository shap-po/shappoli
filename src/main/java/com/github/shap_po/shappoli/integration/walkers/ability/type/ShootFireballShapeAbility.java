package com.github.shap_po.shappoli.integration.walkers.ability.type;

import com.github.shap_po.shappoli.integration.walkers.ability.factory.ShapeAbilityFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import tocraft.walkers.ability.impl.generic.ShootFireballAbility;

public class ShootFireballShapeAbility {
    public static void useAbility(ServerPlayerEntity player, LivingEntity shape, boolean isLarge) {
        new ShootFireballAbility<>(isLarge)
            .onUse(player, shape, shape.getWorld());
    }

    public static ShapeAbilityFactory<LivingEntity> getFactory() {
        return new ShapeAbilityFactory<>(
            ShootFireballAbility.ID,
            new SerializableData()
                .add("is_large", SerializableDataTypes.BOOLEAN, false)
            ,
            (data, playerAndShape) -> useAbility(
                playerAndShape.getLeft(), playerAndShape.getRight(),
                data.getBoolean("is_large")
            )
        );
    }
}
