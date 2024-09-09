package com.github.shap_po.shappoli.util;

import io.github.apace100.calio.data.SerializableData;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MiscUtil {
    public static <T> List<T> listFromData(SerializableData.Instance data, @Nullable String singleKey, @Nullable String multiKey) {
        List<T> list = new ArrayList<>();
        if (singleKey != null) {
            data.<T>ifPresent(singleKey, list::add);
        }
        if (multiKey != null) {
            data.<List<T>>ifPresent(multiKey, list::addAll);
        }
        return list;
    }
}
