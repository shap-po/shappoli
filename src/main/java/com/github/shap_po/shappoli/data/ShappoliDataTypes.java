package com.github.shap_po.shappoli.data;

import io.github.apace100.calio.data.SerializableDataType;

import java.util.List;

public class ShappoliDataTypes {
    public static final SerializableDataType<TrinketSlotData> TRINKET_SLOT = SerializableDataType.compound(TrinketSlotData.class, TrinketSlotData.DATA, TrinketSlotData::fromData, TrinketSlotData::toData);
    public static final SerializableDataType<List<TrinketSlotData>> TRINKET_SLOTS = SerializableDataType.list(TRINKET_SLOT);
}
