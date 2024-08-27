package com.github.shap_po.shappoli.integration.trinkets.data;

import io.github.apace100.calio.data.SerializableDataType;

import java.util.List;

public class ShappoliTrinketsDataTypes {
    public static final SerializableDataType<TrinketSlotData> TRINKET_SLOT = SerializableDataType.compound(TrinketSlotData.class, TrinketSlotData.DATA, TrinketSlotData::fromData, TrinketSlotData::toData);
    public static final SerializableDataType<List<TrinketSlotData>> TRINKET_SLOTS = SerializableDataType.list(TRINKET_SLOT);
}
