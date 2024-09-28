package com.github.shap_po.shappoli.integration.walkers.power.type;

import com.github.shap_po.shappoli.Shappoli;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.factory.PowerTypeFactory;
import io.github.apace100.apoli.power.factory.PowerTypes;
import io.github.apace100.apoli.power.type.PowerType;
import io.github.apace100.calio.data.SerializableData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Pair;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class PreventShapeChangePowerType extends PowerType {
    private final @Nullable Predicate<Pair<Entity, Entity>> bientityCondition;

    public PreventShapeChangePowerType(
        Power type,
        LivingEntity entity,
        @Nullable Predicate<Pair<Entity, Entity>> bientityCondition
    ) {
        super(type, entity);
        this.bientityCondition = bientityCondition;
    }

    public boolean doesApply(Entity shape) {
        return bientityCondition == null || bientityCondition.test(new Pair<>(entity, shape));
    }

    public static PowerTypeFactory getFactory() {
        PowerTypeFactory<?> factory = new PowerTypeFactory<>(
            Shappoli.identifier("prevent_shape_change"),
            new SerializableData()
                .add("bientity_condition", ApoliDataTypes.BIENTITY_CONDITION, null)
            ,
            data -> (type, player) -> new PreventShapeChangePowerType(type, player,
                data.get("bientity_condition")
            )
        ).allowCondition();

        PowerTypes.ALIASES.addPathAlias("prevent_morph", factory.getSerializerId().getPath());
        return factory;
    }
}
