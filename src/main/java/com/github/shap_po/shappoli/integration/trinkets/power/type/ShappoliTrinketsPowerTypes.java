package com.github.shap_po.shappoli.integration.trinkets.power.type;

import com.github.shap_po.shappoli.Shappoli;
import io.github.apace100.apoli.power.PowerConfiguration;
import io.github.apace100.apoli.power.type.PowerTypes;

public class ShappoliTrinketsPowerTypes {
    public static final PowerConfiguration<ActionOnTrinketChangePowerType> ACTION_ON_TRINKET_CHANGE = PowerTypes.register(PowerConfiguration.dataFactory(Shappoli.identifier("action_on_trinket_change"), ActionOnTrinketChangePowerType.DATA_FACTORY));
    public static final PowerConfiguration<ActionOnTrinketKeyPressPowerType> ACTION_ON_TRINKET_KEY_PRESS = PowerTypes.register(PowerConfiguration.dataFactory(Shappoli.identifier("action_on_trinket_key_press"), ActionOnTrinketKeyPressPowerType.DATA_FACTORY));
    public static final PowerConfiguration<ConditionedModifyTrinketsSlotPowerType> CONDITIONED_MODIFY_TRINKETS_SLOT = PowerTypes.register(PowerConfiguration.dataFactory(Shappoli.identifier("conditioned_modify_trinkets_slot"), ConditionedModifyTrinketsSlotPowerType.DATA_FACTORY));
    public static final PowerConfiguration<ModifyTrinketsSlotPowerType> MODIFY_TRINKETS_SLOT = PowerTypes.register(PowerConfiguration.dataFactory(Shappoli.identifier("modify_trinkets_slot"), ModifyTrinketsSlotPowerType.DATA_FACTORY));
    public static final PowerConfiguration<PreventTrinketEquipPowerType> PREVENT_TRINKET_EQUIP = PowerTypes.register(PowerConfiguration.dataFactory(Shappoli.identifier("prevent_trinket_equip"), PreventTrinketEquipPowerType.DATA_FACTORY));
    public static final PowerConfiguration<PreventTrinketUnequipPowerType> PREVENT_TRINKET_UNEQUIP = PowerTypes.register(PowerConfiguration.dataFactory(Shappoli.identifier("prevent_trinket_unequip"), PreventTrinketUnequipPowerType.DATA_FACTORY));

    public static void register() {
        PowerTypes.ALIASES.addPathAlias("conditioned_modify_trinkets_slots", CONDITIONED_MODIFY_TRINKETS_SLOT.id().getPath());
        PowerTypes.ALIASES.addPathAlias("modify_trinkets_slots", MODIFY_TRINKETS_SLOT.id().getPath());
        PowerTypes.ALIASES.addPathAlias("conditioned_modify_trinket_slots", CONDITIONED_MODIFY_TRINKETS_SLOT.id().getPath());
        PowerTypes.ALIASES.addPathAlias("modify_trinket_slots", MODIFY_TRINKETS_SLOT.id().getPath());
    }
}
