package com.github.shap_po.shappoli.integration.trinkets.condition.type;

import com.github.shap_po.shappoli.Shappoli;
import com.github.shap_po.shappoli.integration.trinkets.condition.type.item.EquippableTrinketItemConditionType;
import com.github.shap_po.shappoli.integration.trinkets.condition.type.item.EquippedTrinketCountItemConditionType;
import com.github.shap_po.shappoli.integration.trinkets.condition.type.item.EquippedTrinketItemConditionType;
import com.github.shap_po.shappoli.integration.trinkets.condition.type.item.TrinketItemConditionType;
import io.github.apace100.apoli.condition.ConditionConfiguration;
import io.github.apace100.apoli.condition.type.ItemConditionTypes;

public class ShappoliTrinketsItemConditionTypes {
    public static final ConditionConfiguration<EquippableTrinketItemConditionType> EQUIPPABLE_TRINKET = ItemConditionTypes.register(ConditionConfiguration.of(Shappoli.identifier("equippable_trinket"), EquippableTrinketItemConditionType.DATA_FACTORY));
    public static final ConditionConfiguration<EquippedTrinketCountItemConditionType> EQUIPPED_TRINKET_COUNT = ItemConditionTypes.register(ConditionConfiguration.of(Shappoli.identifier("equipped_trinket_count"), EquippedTrinketCountItemConditionType.DATA_FACTORY));
    public static final ConditionConfiguration<EquippedTrinketItemConditionType> EQUIPPED_TRINKET = ItemConditionTypes.register(ConditionConfiguration.of(Shappoli.identifier("equipped_trinket"), EquippedTrinketItemConditionType.DATA_FACTORY));
    public static final ConditionConfiguration<TrinketItemConditionType> TRINKET = ItemConditionTypes.register(ConditionConfiguration.simple(Shappoli.identifier("trinket"), TrinketItemConditionType::new));

    public static void register() {
        ItemConditionTypes.ALIASES.addPathAlias("is_trinket", TRINKET.id().getPath());
    }
}
