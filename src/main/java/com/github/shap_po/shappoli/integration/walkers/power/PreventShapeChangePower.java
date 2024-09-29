package com.github.shap_po.shappoli.integration.walkers.power;

import com.github.shap_po.shappoli.Shappoli;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.factory.PowerFactories;
import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.calio.data.SerializableData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Pair;

import java.util.function.Predicate;

public class PreventShapeChangePower extends Power {
    private final Predicate<Pair<Entity, Entity>> bientityCondition;

    public PreventShapeChangePower(
        PowerType<?> type,
        LivingEntity entity,
        Predicate<Pair<Entity, Entity>> bientityCondition
    ) {
        super(type, entity);
        this.bientityCondition = bientityCondition;
    }

    public boolean doesApply(Entity shape) {
        return bientityCondition == null || bientityCondition.test(new Pair<>(entity, shape));
    }

    public static PowerFactory createFactory() {
        PowerFactory<Power> factory = new PowerFactory<>(
            Shappoli.identifier("prevent_shape_change"),
            new SerializableData()
                .add("bientity_condition", ApoliDataTypes.BIENTITY_CONDITION, null)
            ,
            data -> (type, player) -> new PreventShapeChangePower(type, player,
                data.get("bientity_condition")
            )
        ).allowCondition();

        PowerFactories.ALIASES.addPathAlias("prevent_morph", factory.getSerializerId().getPath());
        return factory;
    }
}
