package com.github.shap_po.shappoli.integration.walkers.condition.type.entity;

import com.github.shap_po.shappoli.Shappoli;
import com.github.shap_po.shappoli.integration.walkers.registry.ShappoliWalkersRegistries;
import com.github.shap_po.shappoli.integration.walkers.util.WalkersUtil;
import com.github.shap_po.shappoli.util.MiscUtil;
import io.github.apace100.apoli.condition.factory.ConditionTypeFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import tocraft.walkers.ability.AbilityRegistry;
import tocraft.walkers.ability.ShapeAbility;

import java.util.List;
import java.util.Objects;

public class HasShapeAbilityConditionType {
    public static boolean condition(Entity entity, List<Identifier> abilities) {
        if (!(entity instanceof LivingEntity livingEntity)) {
            return false;
        }

        LivingEntity shape = WalkersUtil.getEffectiveShape(livingEntity);

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

    public static ConditionTypeFactory<Entity> getFactory() {
        return new ConditionTypeFactory<>(
            Shappoli.identifier("has_shape_ability"),
            new SerializableData()
                .add("ability", SerializableDataTypes.IDENTIFIER, null)
                .add("abilities", SerializableDataTypes.IDENTIFIERS, null)
            ,
            (data, entity) -> condition(entity, MiscUtil.listFromData(data, "ability", "abilities"))
        );
    }
}
