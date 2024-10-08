package com.github.shap_po.shappoli.integration.walkers.power.factory.ability;

import com.github.shap_po.shappoli.Shappoli;
import io.github.apace100.calio.data.SerializableData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Potion;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Pair;
import tocraft.walkers.ability.impl.generic.ThrowPotionsAbility;

import java.util.List;

public class ThrowPotionsShapeAbility {
    public static void useAbility(SerializableData.Instance data, Pair<ServerPlayerEntity, LivingEntity> playerAndShape) {
        List<Potion> potions = ThrowPotionsAbility.VALID_POTIONS;
        new ThrowPotionsAbility<>(potions)
            .onUse(playerAndShape.getLeft(), null, playerAndShape.getLeft().getWorld());
    }

    public static ShapeAbilityFactory getFactory() {
        return new ShapeAbilityFactory(
            Shappoli.identifier("throw_potions"),
            new SerializableData()
            //TODO: add potions list
            ,
            ThrowPotionsShapeAbility::useAbility
        );
    }
}
