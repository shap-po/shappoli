package com.github.shap_po.shappoli.util;

import com.mojang.serialization.DataResult;
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

    public static DataResult<SerializableData.Instance> checkAtLeastOneFieldExists(SerializableData.Instance data, String... fields) {
        for (String field : fields) {
            if (data.isPresent(field)) {
                return DataResult.success(data);
            }
        }
        return DataResult.error(() -> "Any of the following fields must be defied: " + String.join(", ", fields));
    }
    public static DataResult<SerializableData.Instance> checkAtLeastOneFieldIsTrue(SerializableData.Instance data, String... fields) {
        for (String field : fields) {
            if (data.isPresent(field) && data.getBoolean(field)) {
                return DataResult.success(data);
            }
        }
        return DataResult.error(() -> "Any of the following fields must be true: " + String.join(", ", fields));
    }
}
