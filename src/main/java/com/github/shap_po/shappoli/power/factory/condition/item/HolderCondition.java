package com.github.shap_po.shappoli.power.factory.condition.item;

import com.github.shap_po.shappoli.Shappoli;
import io.github.apace100.apoli.access.EntityLinkedItemStack;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.apoli.power.factory.condition.ItemConditions;
import io.github.apace100.calio.data.SerializableData;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Pair;
import net.minecraft.world.World;

import java.util.function.Predicate;

public class HolderCondition {
    public static boolean condition(SerializableData.Instance data, Pair<World, ItemStack> worldAndStack) {
        Entity holder = ((EntityLinkedItemStack) (Object) worldAndStack.getRight()).apoli$getEntity();
        Predicate<Entity> entityCondition = data.get("condition");
        return holder != null && entityCondition != null && entityCondition.test(holder);
    }

    public static ConditionFactory<Pair<World, ItemStack>> getFactory() {
        ConditionFactory<Pair<World, ItemStack>> factory = new ConditionFactory<>(
            Shappoli.identifier("holder_condition"),
            new SerializableData()
                .add("entity_condition", ApoliDataTypes.ENTITY_CONDITION, null)
                .addFunctionedDefault("condition", ApoliDataTypes.ENTITY_CONDITION, data -> data.get("entity_condition"))
            ,
            HolderCondition::condition
        );

        ItemConditions.ALIASES.addPathAlias("holder", factory.getSerializerId().getPath());
        return factory;
    }
}
