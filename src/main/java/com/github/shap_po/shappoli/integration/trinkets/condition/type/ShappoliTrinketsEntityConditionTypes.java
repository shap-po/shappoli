package com.github.shap_po.shappoli.integration.trinkets.condition.type;

import com.github.shap_po.shappoli.Shappoli;
import com.github.shap_po.shappoli.integration.trinkets.condition.type.entity.EquippedTrinketCountEntityConditionType;
import com.github.shap_po.shappoli.integration.trinkets.condition.type.entity.TrinketSlotCountEntityConditionType;
import io.github.apace100.apoli.condition.ConditionConfiguration;
import io.github.apace100.apoli.condition.type.EntityConditionTypes;

public class ShappoliTrinketsEntityConditionTypes {
    public static final ConditionConfiguration<EquippedTrinketCountEntityConditionType> EQUIPPED_TRINKET_COUNT = EntityConditionTypes.register(ConditionConfiguration.of(Shappoli.identifier("equipped_trinket_count"), EquippedTrinketCountEntityConditionType.DATA_FACTORY));
    public static final ConditionConfiguration<TrinketSlotCountEntityConditionType> TRINKET_SLOT_COUNT = EntityConditionTypes.register(ConditionConfiguration.of(Shappoli.identifier("trinket_slot_count"), TrinketSlotCountEntityConditionType.DATA_FACTORY));

    public static void register() {
    }
}
