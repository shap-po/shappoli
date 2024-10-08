package com.github.shap_po.shappoli.integration.walkers.ability.type;

import com.github.shap_po.shappoli.integration.walkers.ability.factory.ShapeAbilityFactory;
import io.github.apace100.calio.data.SerializableData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import tocraft.walkers.ability.ShapeAbility;

import java.util.function.BiPredicate;

public class SimpleShapeAbility {
    public static <E extends LivingEntity> void useAbility(ShapeAbility<E> shapeAbility, ServerPlayerEntity player, E shape) {
        shapeAbility.onUse(player, shape, shape.getWorld());
    }

    public static <E extends LivingEntity> ShapeAbilityFactory<E> getFactory(Identifier id, ShapeAbility<E> shapeAbilitySupplier, BiPredicate<ServerPlayerEntity, LivingEntity> predicate) {
        return new ShapeAbilityFactory<>(
            id,
            new SerializableData(),
            (data, playerAndShape) -> {
                if (predicate.test(playerAndShape.getLeft(), playerAndShape.getRight())) {
                    useAbility(shapeAbilitySupplier, playerAndShape.getLeft(), playerAndShape.getRight());
                }
            }
        );
    }

    public static <E extends LivingEntity> ShapeAbilityFactory<E> getFactory(Identifier id, ShapeAbility<E> shapeAbilitySupplier) {
        return getFactory(id, shapeAbilitySupplier, (player, shape) -> true);
    }
}
