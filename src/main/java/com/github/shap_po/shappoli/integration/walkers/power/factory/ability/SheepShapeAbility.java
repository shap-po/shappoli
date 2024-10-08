package com.github.shap_po.shappoli.integration.walkers.power.factory.ability;

import com.github.shap_po.shappoli.Shappoli;
import io.github.apace100.calio.data.SerializableData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Pair;
import tocraft.walkers.ability.impl.specific.SheepAbility;

public class SheepShapeAbility {
    public static void useAbility(SerializableData.Instance data, Pair<ServerPlayerEntity, LivingEntity> playerAndShape) {
        if (!(playerAndShape.getRight() instanceof SheepEntity sheep)) {
            return;
        }
        new SheepAbility<>()
            .onUse(playerAndShape.getLeft(), sheep, playerAndShape.getLeft().getWorld());
    }

    public static ShapeAbilityFactory getFactory() {
        return new ShapeAbilityFactory(
            Shappoli.identifier("sheep"),
            new SerializableData()
            ,
            SheepShapeAbility::useAbility
        );
    }
}
