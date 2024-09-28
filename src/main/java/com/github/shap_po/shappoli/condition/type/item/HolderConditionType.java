package com.github.shap_po.shappoli.condition.type.item;

import com.github.shap_po.shappoli.Shappoli;
import com.github.shap_po.shappoli.util.InventoryUtil;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.condition.factory.ConditionTypeFactory;
import io.github.apace100.apoli.condition.factory.ItemConditions;
import io.github.apace100.calio.data.SerializableData;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Pair;
import net.minecraft.world.World;

import java.util.function.Predicate;

public class HolderConditionType {
    public static boolean condition(SerializableData.Instance data, Pair<World, ItemStack> worldAndStack) {
        Entity holder = InventoryUtil.getHolder(worldAndStack.getRight());
        Predicate<Entity> entityCondition = data.get("condition");
        return holder != null && entityCondition != null && entityCondition.test(holder);
    }

    public static ConditionTypeFactory<Pair<World, ItemStack>> getFactory() {
        ConditionTypeFactory<Pair<World, ItemStack>> factory = new ConditionTypeFactory<>(
            Shappoli.identifier("holder_condition"),
            new SerializableData()
                .add("entity_condition", ApoliDataTypes.ENTITY_CONDITION, null)
                .addFunctionedDefault("condition", ApoliDataTypes.ENTITY_CONDITION, data -> data.get("entity_condition"))
            ,
            HolderConditionType::condition
        );

        ItemConditions.ALIASES.addPathAlias("holder", factory.getSerializerId().getPath());
        return factory;
    }
}
