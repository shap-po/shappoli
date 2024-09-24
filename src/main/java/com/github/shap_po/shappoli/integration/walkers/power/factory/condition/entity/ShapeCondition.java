package com.github.shap_po.shappoli.integration.walkers.power.factory.condition.entity;

import com.github.shap_po.shappoli.Shappoli;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.apoli.power.factory.condition.EntityConditions;
import io.github.apace100.calio.data.SerializableData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Pair;
import tocraft.walkers.api.PlayerShape;

import java.util.function.Predicate;

public class ShapeCondition {
    public static boolean condition(SerializableData.Instance data, Entity entity) {
        if (!(entity instanceof PlayerEntity player)) {
            return false;
        }
        LivingEntity shape = PlayerShape.getCurrentShape(player);
        shape = shape == null ? player : shape; // Shape is null if the player is not transformed, default to player
        return data.<Predicate<Pair<Entity, Entity>>>get("bientity_condition").test(new Pair<>(player, shape));
    }

    public static ConditionFactory<Entity> getFactory() {
        ConditionFactory<Entity> factory = new ConditionFactory<>(
            Shappoli.identifier("shape_condition"),
            new SerializableData()
                .add("bientity_condition", ApoliDataTypes.BIENTITY_CONDITION)
            ,
            ShapeCondition::condition
        );

        EntityConditions.ALIASES.addPathAlias("shape", factory.getSerializerId().getPath());
        return factory;
    }
}
