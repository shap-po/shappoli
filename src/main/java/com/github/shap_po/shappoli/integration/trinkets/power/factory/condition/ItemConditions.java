package com.github.shap_po.shappoli.integration.trinkets.power.factory.condition;

import com.github.shap_po.shappoli.integration.trinkets.power.factory.condition.item.EquippableTrinketCondition;
import com.github.shap_po.shappoli.integration.trinkets.power.factory.condition.item.TrinketCondition;
import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registry;
import net.minecraft.util.Pair;
import net.minecraft.world.World;

public class ItemConditions {
    public static void register() {
        register(EquippableTrinketCondition.getFactory());
        register(TrinketCondition.getFactory());
    }

    private static void register(ConditionFactory<Pair<World, ItemStack>> conditionFactory) {
        Registry.register(ApoliRegistries.ITEM_CONDITION, conditionFactory.getSerializerId(), conditionFactory);
    }
}
