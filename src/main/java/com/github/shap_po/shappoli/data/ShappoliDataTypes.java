package com.github.shap_po.shappoli.data;

import com.github.shap_po.shappoli.power.type.ActiveAny;
import io.github.apace100.calio.data.SerializableDataType;

public class ShappoliDataTypes {
    public static final SerializableDataType<ActiveAny.Key> ACTIVE_ANY_KEY = SerializableDataType.compound(ActiveAny.Key.DATA, ActiveAny.Key::fromData, ActiveAny.Key::toData);
}
