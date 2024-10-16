package com.github.shap_po.shappoli.condition.type.item;

import com.github.shap_po.shappoli.Shappoli;
import com.github.shap_po.shappoli.util.InventoryUtil;
import io.github.apace100.apoli.condition.factory.ConditionTypeFactory;
import io.github.apace100.apoli.condition.type.ItemConditionTypes;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.calio.data.SerializableData;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Pair;
import net.minecraft.world.World;

import java.util.function.Predicate;

public class HolderConditionType {
    public static boolean condition(ItemStack stack, Predicate<Entity> entityCondition) {
        Entity holder = InventoryUtil.getHolder(stack);
        return holder != null && entityCondition.test(holder);
    }

    public static ConditionTypeFactory<Pair<World, ItemStack>> getFactory() {
        ConditionTypeFactory<Pair<World, ItemStack>> factory = new ConditionTypeFactory<>(
            Shappoli.identifier("holder_condition"),
            new SerializableData()
                .add("entity_condition", ApoliDataTypes.ENTITY_CONDITION, null)
                .addFunctionedDefault("condition", ApoliDataTypes.ENTITY_CONDITION, data -> data.get("entity_condition"))
            ,
            (data, worldAndStack) -> condition(worldAndStack.getRight(), data.get("condition"))
        );

        ItemConditionTypes.ALIASES.addPathAlias("holder", factory.getSerializerId().getPath());
        return factory;
    }
}
