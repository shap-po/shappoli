package com.github.shap_po.shappoli.integration.trinkets.condition.type.item;

import com.github.shap_po.shappoli.Shappoli;
import dev.emi.trinkets.api.TrinketsApi;
import io.github.apace100.apoli.condition.factory.ConditionTypeFactory;
import io.github.apace100.apoli.condition.factory.ItemConditions;
import io.github.apace100.calio.data.SerializableData;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Pair;
import net.minecraft.world.World;

public class TrinketConditionType {
    public static boolean condition(ItemStack stack) {
        return TrinketsApi.getTrinket(stack.getItem()) != TrinketsApi.getDefaultTrinket();
    }

    public static ConditionTypeFactory<Pair<World, ItemStack>> getFactory() {
        ConditionTypeFactory<Pair<World, ItemStack>> factory = new ConditionTypeFactory<>(
            Shappoli.identifier("is_trinket"),
            new SerializableData(),
            (data, worldAndStack) -> condition(worldAndStack.getRight())
        );

        ItemConditions.ALIASES.addPathAlias("trinket", factory.getSerializerId().getPath());
        return factory;
    }
}
