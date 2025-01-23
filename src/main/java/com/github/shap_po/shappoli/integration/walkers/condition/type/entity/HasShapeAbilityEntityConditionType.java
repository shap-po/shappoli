package com.github.shap_po.shappoli.integration.walkers.condition.type.entity;

import com.github.shap_po.shappoli.integration.walkers.condition.type.ShappoliWalkersEntityConditionTypes;
import com.github.shap_po.shappoli.integration.walkers.registry.ShappoliWalkersShapeAbilityClassRegistry;
import com.github.shap_po.shappoli.integration.walkers.util.WalkersUtil;
import com.github.shap_po.shappoli.util.MiscUtil;
import io.github.apace100.apoli.condition.ConditionConfiguration;
import io.github.apace100.apoli.condition.context.EntityConditionContext;
import io.github.apace100.apoli.condition.type.EntityConditionType;
import io.github.apace100.apoli.data.TypedDataObjectFactory;
import io.github.apace100.calio.data.SerializableData;
import net.minecraft.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;
import tocraft.walkers.ability.AbilityRegistry;
import tocraft.walkers.ability.ShapeAbility;

import java.util.List;

public class HasShapeAbilityEntityConditionType extends EntityConditionType {
    public static final TypedDataObjectFactory<HasShapeAbilityEntityConditionType> DATA_FACTORY = TypedDataObjectFactory.simple(
        new SerializableData()
            .add("ability", ShappoliWalkersShapeAbilityClassRegistry.DATA_TYPE, null)
            .add("abilities", ShappoliWalkersShapeAbilityClassRegistry.DATA_TYPE.list(), null),
        data -> new HasShapeAbilityEntityConditionType(MiscUtil.listFromData(data, "ability", "abilities")),
        (conditionType, serializableData) -> serializableData.instance()
            .set("abilities", conditionType.abilities)
    );

    private final List<? extends Class<? extends ShapeAbility<?>>> abilities;

    public HasShapeAbilityEntityConditionType(List<? extends Class<? extends ShapeAbility<?>>> abilities) {
        this.abilities = abilities;
    }

    @Override
    public boolean test(EntityConditionContext context) {
        if (!(context.entity() instanceof LivingEntity livingEntity)) {
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

        return abilities.stream().anyMatch(a -> a.isInstance(shapeAbility));
    }

    @Override
    public @NotNull ConditionConfiguration<?> getConfig() {
        return ShappoliWalkersEntityConditionTypes.HAS_SHAPE_ABILITY;
    }
}
