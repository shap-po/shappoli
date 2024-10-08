package com.github.shap_po.shappoli.integration.walkers.power.factory.ability;

import com.github.shap_po.shappoli.Shappoli;
import io.github.apace100.calio.data.SerializableData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.ShulkerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Pair;
import tocraft.walkers.ability.impl.specific.ShulkerAbility;

public class ShulkerShapeAbility {
    public static void useAbility(SerializableData.Instance data, Pair<ServerPlayerEntity, LivingEntity> playerAndShape) {
        if (!(playerAndShape.getRight() instanceof ShulkerEntity shulker)) {
            return;
        }
        new ShulkerAbility<>()
            .onUse(playerAndShape.getLeft(), shulker, playerAndShape.getLeft().getWorld());
    }

    public static ShapeAbilityFactory getFactory() {
        return new ShapeAbilityFactory(
            Shappoli.identifier("shulker"),
            new SerializableData()
            ,
            ShulkerShapeAbility::useAbility
        );
    }
}
