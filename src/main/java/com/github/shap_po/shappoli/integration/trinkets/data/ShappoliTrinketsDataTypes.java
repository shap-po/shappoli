package com.github.shap_po.shappoli.integration.trinkets.data;

import com.github.shap_po.shappoli.integration.trinkets.slk.SlotLinkedKey;
import com.github.shap_po.shappoli.integration.trinkets.slk.SlotLinkedKeyManager;
import io.github.apace100.calio.data.SerializableDataType;
import io.github.apace100.calio.data.SerializableDataTypes;

import java.util.List;

public class ShappoliTrinketsDataTypes {
    public static final SerializableDataType<TrinketSlotData> TRINKET_SLOT = SerializableDataType.compound(TrinketSlotData.DATA, TrinketSlotData::fromData, TrinketSlotData::toData);
    public static final SerializableDataType<List<TrinketSlotData>> TRINKET_SLOTS = SerializableDataType.list(TRINKET_SLOT);

    public static final SerializableDataType<SlotEntityAttributeModifier> SLOT_ENTITY_ATTRIBUTE_MODIFIER = SerializableDataType.compound(SlotEntityAttributeModifier.DATA, SlotEntityAttributeModifier::fromData, SlotEntityAttributeModifier::toData);
    public static final SerializableDataType<List<SlotEntityAttributeModifier>> SLOT_ENTITY_ATTRIBUTE_MODIFIERS = SerializableDataType.list(SLOT_ENTITY_ATTRIBUTE_MODIFIER);

    public static final SerializableDataType<SlotLinkedKey> SLOT_LINKED_KEYBINDING = SerializableDataTypes.IDENTIFIER.xmap(SlotLinkedKeyManager::getNullable, SlotLinkedKey::getId);
    public static final SerializableDataType<List<SlotLinkedKey>> SLOT_LINKED_KEYBINDINGS = SLOT_LINKED_KEYBINDING.list();
}
