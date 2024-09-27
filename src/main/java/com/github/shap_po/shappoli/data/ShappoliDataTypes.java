package com.github.shap_po.shappoli.data;

import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.PowerReference;
import io.github.apace100.calio.data.SerializableDataType;

import java.util.List;

public class ShappoliDataTypes {
    public static final SerializableDataType<List<PowerReference>> POWER_REFERENCES = SerializableDataType.list(ApoliDataTypes.POWER_REFERENCE);
}
