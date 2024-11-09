package com.github.shap_po.shappoli.integration.walkers.ability.type;

import com.github.shap_po.shappoli.integration.walkers.ability.factory.ShapeAbilityFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import tocraft.walkers.ability.impl.generic.GetItemAbility;

public class GetItemShapeAbility {
    public static void useAbility(ServerPlayerEntity player, LivingEntity shape, ItemStack stack) {
        new GetItemAbility<>(stack)
            .onUse(player, shape, shape.getWorld());
    }

    public static ShapeAbilityFactory<LivingEntity> getFactory() {
        return new ShapeAbilityFactory<>(
            GetItemAbility.ID,
            new SerializableData()
                .add("item", SerializableDataTypes.ITEM_STACK, ItemStack.EMPTY)
            ,
            (data, playerAndShape) -> useAbility(
                playerAndShape.getLeft(), playerAndShape.getRight(),
                data.get("item")
            )
        );
    }
}
