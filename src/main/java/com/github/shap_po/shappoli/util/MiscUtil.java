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

    /**
     * @throws IllegalArgumentException If none of the fields are present
     */
    public static void checkHasAtLeastOneField(SerializableData.Instance data, String... fields) {
        for (String field : fields) {
            if (data.isPresent(field)) {
                return;
            }
        }
        throw new IllegalArgumentException("Any of the following fields must be defied: " + String.join(", ", fields));
    }
}
