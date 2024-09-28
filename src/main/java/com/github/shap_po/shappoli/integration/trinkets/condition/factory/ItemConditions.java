package com.github.shap_po.shappoli.integration.trinkets.condition.factory;

import com.github.shap_po.shappoli.integration.trinkets.condition.type.item.EquippableTrinketConditionType;
import com.github.shap_po.shappoli.integration.trinkets.condition.type.item.EquippedTrinketCountConditionType;
import com.github.shap_po.shappoli.integration.trinkets.condition.type.item.TrinketConditionType;
import io.github.apace100.apoli.condition.factory.ConditionTypeFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registry;
import net.minecraft.util.Pair;
import net.minecraft.world.World;

public class ItemConditions {
    public static void register() {
        register(EquippableTrinketConditionType.getFactory());
        register(EquippedTrinketCountConditionType.getFactory());
        register(TrinketConditionType.getFactory());
    }

    private static void register(ConditionTypeFactory<Pair<World, ItemStack>> ConditionTypeFactory) {
        Registry.register(ApoliRegistries.ITEM_CONDITION, ConditionTypeFactory.getSerializerId(), ConditionTypeFactory);
    }
}
