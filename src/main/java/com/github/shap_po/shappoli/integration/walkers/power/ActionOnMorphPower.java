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

import java.util.function.Consumer;

public class ActionOnMorphPower extends Power {
    private final Consumer<Entity> entityAction;

    public ActionOnMorphPower(
        PowerType<?> type,
        LivingEntity entity,
        Consumer<Entity> entityAction
    ) {
        super(type, entity);
        this.entityAction = entityAction;
    }

    public void apply() {
        entityAction.accept(entity);
    }

    public static PowerFactory createFactory() {
        PowerFactory<Power> factory = new PowerFactory<>(
            Shappoli.identifier("action_on_morph"),
            new SerializableData()
                .add("entity_action", ApoliDataTypes.ENTITY_ACTION)
            ,
            data -> (type, player) -> new ActionOnMorphPower(
                type,
                player,
                data.get("entity_action")
            )
        ).allowCondition();

        PowerFactories.ALIASES.addPathAlias("action_on_shape_change", factory.getSerializerId().getPath());
        return factory;
    }
}
