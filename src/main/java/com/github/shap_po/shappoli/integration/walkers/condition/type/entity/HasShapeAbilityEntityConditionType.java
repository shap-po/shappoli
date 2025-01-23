package com.github.shap_po.shappoli.integration.walkers.condition.type.entity;

import com.github.shap_po.shappoli.integration.walkers.condition.type.ShappoliWalkersEntityConditionTypes;
import com.github.shap_po.shappoli.integration.walkers.registry.ShappoliWalkersRegistries;
import com.github.shap_po.shappoli.integration.walkers.util.WalkersUtil;
import com.github.shap_po.shappoli.util.MiscUtil;
import io.github.apace100.apoli.condition.ConditionConfiguration;
import io.github.apace100.apoli.condition.context.EntityConditionContext;
import io.github.apace100.apoli.condition.type.EntityConditionType;
import io.github.apace100.apoli.data.TypedDataObjectFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import tocraft.walkers.ability.AbilityRegistry;
import tocraft.walkers.ability.ShapeAbility;

import java.util.List;
import java.util.Objects;

public class HasShapeAbilityEntityConditionType extends EntityConditionType {
    public static final TypedDataObjectFactory<HasShapeAbilityEntityConditionType> DATA_FACTORY = TypedDataObjectFactory.simple(
        new SerializableData()
            .add("ability", SerializableDataTypes.IDENTIFIER, null)
            .add("abilities", SerializableDataTypes.IDENTIFIERS, null),
        data -> new HasShapeAbilityEntityConditionType(MiscUtil.listFromData(data, "ability", "abilities")),
        (conditionType, serializableData) -> serializableData.instance()
            .set("abilities", conditionType.abilities)
    );

    private final List<Identifier> abilities;
    private final List<? extends Class<? extends ShapeAbility<?>>> abilityClasses;

    public HasShapeAbilityEntityConditionType(List<Identifier> abilities) {
        this.abilities = abilities;
        this.abilityClasses = abilities
            .stream()
            .map(ShappoliWalkersRegistries.SHAPE_ABILITY_CLASS::get)
            .filter(Objects::nonNull)
            .toList();
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

        return abilityClasses.stream().anyMatch(a -> a.isInstance(shapeAbility));
    }

    @Override
    public @NotNull ConditionConfiguration<?> getConfig() {
        return ShappoliWalkersEntityConditionTypes.HAS_SHAPE_ABILITY;
    }
}
