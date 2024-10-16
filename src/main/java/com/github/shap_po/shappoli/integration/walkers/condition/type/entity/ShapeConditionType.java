package com.github.shap_po.shappoli.integration.walkers.condition.type.entity;

import com.github.shap_po.shappoli.Shappoli;
import com.github.shap_po.shappoli.integration.walkers.util.WalkersUtil;
import io.github.apace100.apoli.condition.factory.ConditionTypeFactory;
import io.github.apace100.apoli.condition.type.EntityConditionTypes;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.calio.data.SerializableData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Pair;

import java.util.function.Predicate;

public class ShapeConditionType {
    public static boolean condition(Entity entity, Predicate<Pair<Entity, Entity>> biEntityCondition) {
        if (!(entity instanceof PlayerEntity player)) {
            return false;
        }
        return biEntityCondition.test(new Pair<>(player, WalkersUtil.getShape(player)));
    }

    public static ConditionTypeFactory<Entity> getFactory() {
        ConditionTypeFactory<Entity> factory = new ConditionTypeFactory<>(
            Shappoli.identifier("shape_condition"),
            new SerializableData()
                .add("bientity_condition", ApoliDataTypes.BIENTITY_CONDITION)
            ,
            (data, entity) -> condition(entity, data.get("bientity_condition"))
        );

        EntityConditionTypes.ALIASES.addPathAlias("shape", factory.getSerializerId().getPath());
        return factory;
    }
}
