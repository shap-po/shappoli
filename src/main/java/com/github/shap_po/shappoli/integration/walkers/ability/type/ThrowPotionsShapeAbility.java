package com.github.shap_po.shappoli.integration.walkers.ability.type;

import com.github.shap_po.shappoli.integration.walkers.ability.factory.ShapeAbilityFactory;
import com.github.shap_po.shappoli.util.MiscUtil;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Potion;
import net.minecraft.registry.Registries;
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
            ThrowPotionsAbility.ID,
            new SerializableData()
                .add("potion", SerializableDataType.registryEntry(Registries.POTION), null)
                .add("potions", SerializableDataType.registryEntry(Registries.POTION).list(), null)
            ,
            (data, playerAndShape) -> {
                List<RegistryEntry<Potion>> potions = MiscUtil.listFromData(data, "potion", "potions");
                if (potions.isEmpty()) {
                    potions = ThrowPotionsAbility.VALID_POTIONS;
                }
                
                useAbility(
                    playerAndShape.getLeft(), playerAndShape.getRight(),
                    potions
                );
            }
        );
    }
}
