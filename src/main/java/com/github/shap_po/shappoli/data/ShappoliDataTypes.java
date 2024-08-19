package com.github.shap_po.shappoli.data;

import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataType;
import io.github.apace100.calio.data.SerializableDataTypes;

public class ShappoliDataTypes {
    public static final SerializableDataType<TrinketSlotData> TRINKET_SLOT = SerializableDataType.compound(
        TrinketSlotData.class,
        new SerializableData()
            .add("name", SerializableDataTypes.STRING, null)
            .add("group", SerializableDataTypes.STRING, null)
            .add("index", SerializableDataTypes.INT, null),
        (data) -> new TrinketSlotData(data.getString("name"), data.getString("group"), data.getInt("index")),
        ((serializableData, trinketSlotData) -> {
            SerializableData.Instance data = serializableData.new Instance();
            data.set("name", trinketSlotData.name);
            data.set("group", trinketSlotData.group);
            data.set("index", trinketSlotData.index);
            return data;
        })
    );
}
