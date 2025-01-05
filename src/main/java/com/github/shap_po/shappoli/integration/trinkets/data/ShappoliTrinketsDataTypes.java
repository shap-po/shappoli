package com.github.shap_po.shappoli.integration.trinkets.data;

import com.github.shap_po.shappoli.integration.trinkets.slk.SlotLinkedKey;
import com.github.shap_po.shappoli.integration.trinkets.slk.SlotLinkedKeyManager;
import io.github.apace100.calio.data.SerializableDataType;
import io.github.apace100.calio.data.SerializableDataTypes;

public class ShappoliTrinketsDataTypes {
    public static final SerializableDataType<TrinketSlotData> TRINKET_SLOT = TrinketSlotData.DATA_TYPE;
    public static final SerializableDataType<SlotEntityAttributeModifier> SLOT_ENTITY_ATTRIBUTE_MODIFIER = SlotEntityAttributeModifier.DATA_TYPE;
    public static final SerializableDataType<SlotLinkedKey> SLOT_LINKED_KEYBINDING = SerializableDataTypes.IDENTIFIER.xmap(SlotLinkedKeyManager::getNullable, SlotLinkedKey::getId);
}
