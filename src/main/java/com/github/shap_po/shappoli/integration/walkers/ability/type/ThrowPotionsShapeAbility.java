package com.github.shap_po.shappoli.integration.walkers.ability.type;

import com.github.shap_po.shappoli.Shappoli;
import com.github.shap_po.shappoli.integration.walkers.ability.factory.ShapeAbilityFactory;
import io.github.apace100.calio.data.SerializableData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Potion;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import tocraft.walkers.ability.impl.generic.ThrowPotionsAbility;

import java.util.List;

public class ThrowPotionsShapeAbility {
    public static void useAbility(ServerPlayerEntity player, LivingEntity shape, List<RegistryEntry<Potion>> potions) {
        new ThrowPotionsAbility<>(potions)
            .onUse(player, shape, shape.getWorld());
    }

    public static ShapeAbilityFactory<LivingEntity> getFactory() {
        return new ShapeAbilityFactory<>(
            Shappoli.identifier("throw_potions"),
            new SerializableData()
            //TODO: add potions list
            ,
            (data, playerAndShape) -> useAbility(
                playerAndShape.getLeft(), playerAndShape.getRight(),
                ThrowPotionsAbility.VALID_POTIONS
            )
        );
    }
}
