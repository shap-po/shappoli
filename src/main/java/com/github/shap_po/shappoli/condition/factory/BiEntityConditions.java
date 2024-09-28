package com.github.shap_po.shappoli.condition.factory;

import com.github.shap_po.shappoli.power.type.ReceiveConditionPowerType;
import com.github.shap_po.shappoli.condition.type.meta.SendConditionConditionType;
import io.github.apace100.apoli.condition.factory.ConditionTypeFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import net.minecraft.entity.Entity;
import net.minecraft.registry.Registry;
import net.minecraft.util.Pair;

public class BiEntityConditions {
    public static void register() {
        register(SendConditionConditionType.getFactory(
            Pair::getLeft,
            ReceiveConditionPowerType::receiveBientity,
            io.github.apace100.apoli.condition.factory.BiEntityConditions.ALIASES
        ));
    }

    private static void register(ConditionTypeFactory<Pair<Entity, Entity>> ConditionTypeFactory) {
        Registry.register(ApoliRegistries.BIENTITY_CONDITION, ConditionTypeFactory.getSerializerId(), ConditionTypeFactory);
    }
}
