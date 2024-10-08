package com.github.shap_po.shappoli.integration.walkers.power.factory.ability;

import com.github.shap_po.shappoli.Shappoli;
import io.github.apace100.calio.data.SerializableData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Pair;
import tocraft.walkers.ability.impl.specific.RabbitAbility;

public class RabbitShapeAbility {
    public static void useAbility(SerializableData.Instance data, Pair<ServerPlayerEntity, LivingEntity> playerAndShape) {
        new RabbitAbility<>()
            .onUse(playerAndShape.getLeft(), null, playerAndShape.getLeft().getWorld());
    }

    public static ShapeAbilityFactory getFactory() {
        return new ShapeAbilityFactory(
            Shappoli.identifier("rabbit"),
            new SerializableData()
            ,
            RabbitShapeAbility::useAbility
        );
    }
}
