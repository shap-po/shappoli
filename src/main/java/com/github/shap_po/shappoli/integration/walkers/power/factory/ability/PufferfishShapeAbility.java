package com.github.shap_po.shappoli.integration.walkers.power.factory.ability;

import com.github.shap_po.shappoli.Shappoli;
import io.github.apace100.calio.data.SerializableData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.PufferfishEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Pair;
import tocraft.walkers.ability.impl.specific.PufferfishAbility;


public class PufferfishShapeAbility {
    public static void useAbility(SerializableData.Instance data, Pair<ServerPlayerEntity, LivingEntity> playerAndShape) {
        if (!(playerAndShape.getRight() instanceof PufferfishEntity pufferfish)) {
            return;
        }
        new PufferfishAbility<>()
            .onUse(playerAndShape.getLeft(), pufferfish, playerAndShape.getLeft().getWorld());
    }

    public static ShapeAbilityFactory getFactory() {
        return new ShapeAbilityFactory(
            Shappoli.identifier("pufferfish"),
            new SerializableData()
            ,
            PufferfishShapeAbility::useAbility
        );
    }
}
