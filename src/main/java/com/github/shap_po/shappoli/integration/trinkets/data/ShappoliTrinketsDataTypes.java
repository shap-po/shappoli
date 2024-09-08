package com.github.shap_po.shappoli.integration.trinkets.data;

import io.github.apace100.calio.data.SerializableDataType;

import java.util.List;

public class ShappoliTrinketsDataTypes {
    public static final SerializableDataType<TrinketSlotData> TRINKET_SLOT = SerializableDataType.compound(TrinketSlotData.class, TrinketSlotData.DATA, TrinketSlotData::fromData, TrinketSlotData::toData);
    public static final SerializableDataType<List<TrinketSlotData>> TRINKET_SLOTS = SerializableDataType.list(TRINKET_SLOT);

    public static final SerializableDataType<SlotEntityAttributeModifier> SLOT_ENTITY_ATTRIBUTE_MODIFIER = SerializableDataType.compound(SlotEntityAttributeModifier.class, SlotEntityAttributeModifier.DATA, SlotEntityAttributeModifier::fromData, SlotEntityAttributeModifier::toData);
    public static final SerializableDataType<List<SlotEntityAttributeModifier>> SLOT_ENTITY_ATTRIBUTE_MODIFIERS = SerializableDataType.list(SLOT_ENTITY_ATTRIBUTE_MODIFIER);
}
