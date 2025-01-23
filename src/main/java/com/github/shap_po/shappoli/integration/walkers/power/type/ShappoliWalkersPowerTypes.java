package com.github.shap_po.shappoli.integration.walkers.power.type;

import com.github.shap_po.shappoli.Shappoli;
import io.github.apace100.apoli.power.PowerConfiguration;
import io.github.apace100.apoli.power.type.PowerTypes;

public class ShappoliWalkersPowerTypes {
    public static final PowerConfiguration<ActionOnShapeAbilityUsePowerType> ACTION_ON_SHAPE_ABILITY_USE = PowerTypes.register(PowerConfiguration.of(Shappoli.identifier("action_on_shape_ability_use"), ActionOnShapeAbilityUsePowerType.DATA_FACTORY));
    public static final PowerConfiguration<ActionOnShapeChangePowerType> ACTION_ON_SHAPE_CHANGE = PowerTypes.register(PowerConfiguration.of(Shappoli.identifier("action_on_shape_change"), ActionOnShapeChangePowerType.DATA_FACTORY));
    public static final PowerConfiguration<PreventShapeAbilityUsePowerType> PREVENT_SHAPE_ABILITY_USE = PowerTypes.register(PowerConfiguration.of(Shappoli.identifier("prevent_shape_ability_use"), PreventShapeAbilityUsePowerType.DATA_FACTORY));
    public static final PowerConfiguration<PreventShapeChangePowerType> PREVENT_SHAPE_CHANGE = PowerTypes.register(PowerConfiguration.of(Shappoli.identifier("prevent_shape_change"), PreventShapeChangePowerType.DATA_FACTORY));

    public static void register() {
    }
}
