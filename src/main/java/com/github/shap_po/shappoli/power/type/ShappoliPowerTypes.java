package com.github.shap_po.shappoli.power.type;

import com.github.shap_po.shappoli.Shappoli;
import io.github.apace100.apoli.power.PowerConfiguration;
import io.github.apace100.apoli.power.type.PowerTypes;

public class ShappoliPowerTypes {
    public static final PowerConfiguration<ActionOnEntityCollisionPowerType> ACTION_ON_ENTITY_COLLISION = PowerTypes.register(PowerConfiguration.dataFactory(Shappoli.identifier("action_on_entity_collision"), ActionOnEntityCollisionPowerType.DATA_FACTORY));
    public static final PowerConfiguration<ActionOnKeyPressPowerType> ACTION_ON_KEY_PRESS = PowerTypes.register(PowerConfiguration.dataFactory(Shappoli.identifier("action_on_key_press"), ActionOnKeyPressPowerType.DATA_FACTORY));
    public static final PowerConfiguration<ModifyVillagerReputationPowerType> MODIFY_VILLAGER_REPUTATION = PowerTypes.register(PowerConfiguration.dataFactory(Shappoli.identifier("modify_villager_reputation"), ModifyVillagerReputationPowerType.DATA_FACTORY));
    public static final PowerConfiguration<ReceiveActionPowerType> RECEIVE_ACTION = PowerTypes.register(PowerConfiguration.dataFactory(Shappoli.identifier("receive_action"), ReceiveActionPowerType.DATA_FACTORY));
    public static final PowerConfiguration<ReceiveConditionPowerType> RECEIVE_CONDITION = PowerTypes.register(PowerConfiguration.dataFactory(Shappoli.identifier("receive_condition"), ReceiveConditionPowerType.DATA_FACTORY));
    public static final PowerConfiguration<SuppressPowerPowerType> SUPPRESS_POWER = PowerTypes.register(PowerConfiguration.dataFactory(Shappoli.identifier("suppress_power"), SuppressPowerPowerType.DATA_FACTORY));

    public static void register() {
    }
}
