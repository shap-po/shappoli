package com.github.shap_po.shappoli.integration.walkers.power.factory.condition.entity;

import com.github.shap_po.shappoli.Shappoli;
import com.github.shap_po.shappoli.integration.walkers.registry.ShappoliWalkersRegistries;
import com.github.shap_po.shappoli.integration.walkers.util.WalkersUtil;
import com.github.shap_po.shappoli.util.MiscUtil;
import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import tocraft.walkers.ability.AbilityRegistry;
import tocraft.walkers.ability.ShapeAbility;

import java.util.List;
import java.util.Objects;

public class HasShapeAbilityCondition {
    public static boolean condition(SerializableData.Instance data, Entity entity) {
        if (!(entity instanceof LivingEntity livingEntity)) {
            return false;
        }

        LivingEntity shape = WalkersUtil.getEffectiveShape(livingEntity);

        List<Identifier> abilities = MiscUtil.listFromData(data, "ability", "abilities");
        if (abilities.isEmpty()) {
            return AbilityRegistry.has(shape);
        }

        ShapeAbility<?> shapeAbility = AbilityRegistry.get(shape);
        if (shapeAbility == null) {
            return false;
        }

        return abilities
            .stream()
            .map(ShappoliWalkersRegistries.SHAPE_ABILITY_TYPE::get)
            .filter(Objects::nonNull)
            .anyMatch(a -> a.isInstance(shapeAbility));
    }


    public static ConditionFactory<Entity> getFactory() {
        return new ConditionFactory<>(
            Shappoli.identifier("has_shape_ability"),
            new SerializableData()
                .add("ability", SerializableDataTypes.IDENTIFIER, null)
                .add("abilities", SerializableDataTypes.IDENTIFIERS, null)
            ,
            HasShapeAbilityCondition::condition
        );
    }
}
