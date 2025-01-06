package com.github.shap_po.shappoli.integration.walkers.action.type;

import com.github.shap_po.shappoli.Shappoli;
import com.github.shap_po.shappoli.integration.walkers.action.type.entity.*;
import io.github.apace100.apoli.action.ActionConfiguration;
import io.github.apace100.apoli.action.type.EntityActionTypes;

public class ShappoliWalkersEntityActionTypes {
    public static final ActionConfiguration<ChangeShapeAbilityCooldownEntityActionType> CHANGE_SHAPE_ABILITY_COOLDOWN = EntityActionTypes.register(ActionConfiguration.of(Shappoli.identifier("change_shape_ability_cooldown"), ChangeShapeAbilityCooldownEntityActionType.DATA_FACTORY));
    public static final ActionConfiguration<ExecuteShapeAbilityEntityActionType> EXECUTE_SHAPE_ABILITY = EntityActionTypes.register(ActionConfiguration.of(Shappoli.identifier("execute_shape_ability"), ExecuteShapeAbilityEntityActionType.DATA_FACTORY));
    public static final ActionConfiguration<ShapeActionEntityActionType> SHAPE_ACTION = EntityActionTypes.register(ActionConfiguration.of(Shappoli.identifier("shape_action"), ShapeActionEntityActionType.DATA_FACTORY));
    public static final ActionConfiguration<SwitchShapeEntityActionType> SWITCH_SHAPE = EntityActionTypes.register(ActionConfiguration.of(Shappoli.identifier("switch_shape"), SwitchShapeEntityActionType.DATA_FACTORY));
    public static final ActionConfiguration<UseShapeAbilityEntityActionType> USE_SHAPE_ABILITY = EntityActionTypes.register(ActionConfiguration.of(Shappoli.identifier("use_shape_ability"), UseShapeAbilityEntityActionType.DATA_FACTORY));

    public static void register() {
        EntityActionTypes.ALIASES.addPathAlias("action_on_shape", SHAPE_ACTION.id().getPath());
        EntityActionTypes.ALIASES.addPathAlias("change_shape", SWITCH_SHAPE.id().getPath());
    }
}
