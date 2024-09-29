package com.github.shap_po.shappoli.power.factory.condition.item;

import com.github.shap_po.shappoli.Shappoli;
import com.github.shap_po.shappoli.util.InventoryUtil;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.calio.data.SerializableData;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;

import java.util.function.Predicate;

public class HolderCondition {
    public static boolean condition(SerializableData.Instance data, ItemStack stack) {
        Entity holder = InventoryUtil.getHolder(stack);
        Predicate<Entity> entityCondition = data.get("condition");
        return holder != null && entityCondition != null && entityCondition.test(holder);
    }

    public static ConditionFactory<ItemStack> getFactory() {
        return new ConditionFactory<>(
            Shappoli.identifier("holder"),
            new SerializableData()
                .add("entity_condition", ApoliDataTypes.ENTITY_CONDITION, null)
                .addFunctionedDefault("condition", ApoliDataTypes.ENTITY_CONDITION, data -> data.get("entity_condition"))
            ,
            HolderCondition::condition
        );
    }
}
