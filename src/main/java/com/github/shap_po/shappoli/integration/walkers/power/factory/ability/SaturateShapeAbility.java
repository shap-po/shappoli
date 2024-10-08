package com.github.shap_po.shappoli.integration.walkers.power.factory.ability;

import com.github.shap_po.shappoli.Shappoli;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Pair;
import tocraft.walkers.ability.impl.generic.SaturateAbility;

public class SaturateShapeAbility {
    public static void useAbility(SerializableData.Instance data, Pair<ServerPlayerEntity, LivingEntity> playerAndShape) {
        new SaturateAbility<>(data.getInt("food_level"), data.getFloat("saturation_level"))
            .onUse(playerAndShape.getLeft(), null, playerAndShape.getLeft().getWorld());
    }

    public static ShapeAbilityFactory getFactory() {
        return new ShapeAbilityFactory(
            Shappoli.identifier("saturate"),
            new SerializableData()
                .add("food_level", SerializableDataTypes.POSITIVE_INT, 6)
                .add("saturation_level", SerializableDataTypes.POSITIVE_DOUBLE, 0.1)
            ,
            SaturateShapeAbility::useAbility
        );
    }
}
