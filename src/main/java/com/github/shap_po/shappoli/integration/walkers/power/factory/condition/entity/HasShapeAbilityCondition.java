package com.github.shap_po.shappoli.integration.walkers.power.factory.condition.entity;

import com.github.shap_po.shappoli.Shappoli;
import com.github.shap_po.shappoli.integration.walkers.registry.ShappoliWalkersRegistries;
import com.github.shap_po.shappoli.integration.walkers.util.WalkersUtil;
import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import tocraft.walkers.ability.AbilityRegistry;
import tocraft.walkers.ability.ShapeAbility;

public class HasShapeAbilityCondition {
    public static boolean condition(SerializableData.Instance data, Entity entity) {
        if (!(entity instanceof LivingEntity livingEntity)) {
            return false;
        }

        LivingEntity shape = WalkersUtil.getEffectiveShape(livingEntity);

        Identifier ability = data.getId("ability");
        if (ability == null) {
            return AbilityRegistry.has(shape);
        }

        ShapeAbility<?> shapeAbility = AbilityRegistry.get(shape);
        if (shapeAbility == null) {
            return false;
        }

        Class<? extends ShapeAbility<?>> shapeAbilityClass = ShappoliWalkersRegistries.SHAPE_ABILITY_TYPE.get(ability);
        return shapeAbilityClass != null && shapeAbilityClass.isInstance(shapeAbility);
    }


    public static ConditionFactory<Entity> getFactory() {
        return new ConditionFactory<>(
            Shappoli.identifier("has_shape_ability"),
            new SerializableData()
                .add("ability", SerializableDataTypes.IDENTIFIER, null)
            ,
            HasShapeAbilityCondition::condition
        );
    }
}
