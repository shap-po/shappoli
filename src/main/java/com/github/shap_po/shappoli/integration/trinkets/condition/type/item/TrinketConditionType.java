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
    public static boolean condition(SerializableData.Instance data, Pair<World, ItemStack> worldAndStack) {
        return TrinketsApi.getTrinket(worldAndStack.getRight().getItem()) != TrinketsApi.getDefaultTrinket();
    }

    public static ConditionTypeFactory<Pair<World, ItemStack>> getFactory() {
        ConditionTypeFactory<Pair<World, ItemStack>> factory = new ConditionTypeFactory<>(
            Shappoli.identifier("trinket"),
            new SerializableData(),
            TrinketConditionType::condition
        );

        ItemConditions.ALIASES.addPathAlias("is_trinket", factory.getSerializerId().getPath());
        return factory;
    }
}
