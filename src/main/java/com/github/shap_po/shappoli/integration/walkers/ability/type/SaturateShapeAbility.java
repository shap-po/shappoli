package com.github.shap_po.shappoli.integration.walkers.ability.type;

import com.github.shap_po.shappoli.integration.walkers.ability.factory.ShapeAbilityFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import tocraft.walkers.ability.impl.generic.SaturateAbility;

public class SaturateShapeAbility {
    public static void useAbility(ServerPlayerEntity player, LivingEntity shape, int foodLevel, float saturationLevel) {
        new SaturateAbility<>(foodLevel, saturationLevel)
            .onUse(player, shape, shape.getWorld());
    }

    public static ShapeAbilityFactory<LivingEntity> getFactory() {
        return new ShapeAbilityFactory<>(
            SaturateAbility.ID,
            new SerializableData()
                .add("food_level", SerializableDataTypes.POSITIVE_INT, 6)
                .add("saturation_level", SerializableDataTypes.POSITIVE_DOUBLE, 0.1)
            ,
            (data, playerAndShape) -> useAbility(
                playerAndShape.getLeft(), playerAndShape.getRight(),
                data.getInt("food_level"), data.getFloat("saturation_level")
            )
        );
    }
}
