package com.github.shap_po.shappoli.integration.backport.apoli.power.factory.condition.bientity;

import com.github.shap_po.shappoli.Shappoli;
import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.calio.data.SerializableData;
import net.minecraft.entity.Entity;
import net.minecraft.util.Pair;

import java.util.Objects;

/**
 * Backport of the apoli:equal bientity condition from {@link io.github.apace100.apoli.power.factory.condition.bientity.EqualCondition}
 */
public class EqualCondition {
    public static boolean condition(SerializableData.Instance data, Pair<Entity, Entity> actorAndTarget) {
        return Objects.equals(actorAndTarget.getLeft(), actorAndTarget.getRight());
    }

    public static ConditionFactory<Pair<Entity, Entity>> getFactory() {
        return new ConditionFactory<>(
            Shappoli.identifier("equal"),
            new SerializableData(),
            EqualCondition::condition
        );
    }
}
