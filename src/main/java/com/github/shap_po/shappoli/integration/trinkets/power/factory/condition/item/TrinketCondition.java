package com.github.shap_po.shappoli.integration.trinkets.power.factory.condition.item;

import com.github.shap_po.shappoli.Shappoli;
import dev.emi.trinkets.api.TrinketsApi;
import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.apoli.power.factory.condition.ItemConditions;
import io.github.apace100.calio.data.SerializableData;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Pair;
import net.minecraft.world.World;

public class TrinketCondition {
    public static boolean condition(SerializableData.Instance data, Pair<World, ItemStack> worldAndStack) {
        return TrinketsApi.getTrinket(worldAndStack.getRight().getItem()) != TrinketsApi.getDefaultTrinket();
    }

    public static ConditionFactory<Pair<World, ItemStack>> getFactory() {
        ConditionFactory<Pair<World, ItemStack>> factory = new ConditionFactory<>(
            Shappoli.identifier("trinket"),
            new SerializableData(),
            TrinketCondition::condition
        );

        ItemConditions.ALIASES.addPathAlias("is_trinket", factory.getSerializerId().getPath());
        return factory;
    }
}
