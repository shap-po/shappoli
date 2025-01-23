package com.github.shap_po.shappoli.integration.trinkets.data;

import com.github.shap_po.shappoli.integration.trinkets.keybinding.TrinketKeyBinding;
import com.github.shap_po.shappoli.integration.trinkets.keybinding.TrinketKeyBindingManager;
import io.github.apace100.calio.data.SerializableDataType;
import io.github.apace100.calio.data.SerializableDataTypes;

public class ShappoliTrinketsDataTypes {
    public static final SerializableDataType<TrinketSlotFilter> TRINKET_SLOT = TrinketSlotFilter.DATA_TYPE;
    public static final SerializableDataType<SlotEntityAttributeModifier> SLOT_ENTITY_ATTRIBUTE_MODIFIER = SlotEntityAttributeModifier.DATA_TYPE;
    public static final SerializableDataType<TrinketKeyBinding> TRINKET_KEYBINDING = SerializableDataTypes.IDENTIFIER.xmap(TrinketKeyBindingManager::getNullable, TrinketKeyBinding::getId);
}
