package com.github.shap_po.shappoli.integration.walkers.condition.type;

import com.github.shap_po.shappoli.Shappoli;
import com.github.shap_po.shappoli.integration.walkers.condition.type.entity.*;
import io.github.apace100.apoli.condition.ConditionConfiguration;
import io.github.apace100.apoli.condition.type.EntityConditionTypes;

public class ShappoliWalkersEntityConditionTypes {
    public static final ConditionConfiguration<CanUseShapeAbilityEntityConditionType> CAN_USE_SHAPE_ABILITY = EntityConditionTypes.register(ConditionConfiguration.simple(Shappoli.identifier("can_use_shape_ability"), CanUseShapeAbilityEntityConditionType::new));
    public static final ConditionConfiguration<HasShapeAbilityEntityConditionType> HAS_SHAPE_ABILITY = EntityConditionTypes.register(ConditionConfiguration.of(Shappoli.identifier("has_shape_ability"), HasShapeAbilityEntityConditionType.DATA_FACTORY));
    public static final ConditionConfiguration<ShapeAbilityCooldownEntityConditionType> SHAPE_ABILITY_COOLDOWN = EntityConditionTypes.register(ConditionConfiguration.of(Shappoli.identifier("shape_ability_cooldown"), ShapeAbilityCooldownEntityConditionType.DATA_FACTORY));
    public static final ConditionConfiguration<ShapedEntityConditionType> SHAPED = EntityConditionTypes.register(ConditionConfiguration.simple(Shappoli.identifier("shaped"), ShapedEntityConditionType::new));
    public static final ConditionConfiguration<ShapeEntityConditionType> SHAPE = EntityConditionTypes.register(ConditionConfiguration.of(Shappoli.identifier("shape"), ShapeEntityConditionType.DATA_FACTORY));

    public static void register() {
        EntityConditionTypes.ALIASES.addPathAlias("shape_condition", SHAPE.id().getPath());
    }
}
