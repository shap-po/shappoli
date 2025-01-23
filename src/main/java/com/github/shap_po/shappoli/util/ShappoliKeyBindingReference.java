package com.github.shap_po.shappoli.util;

import io.github.apace100.apoli.data.TypedDataObjectFactory;
import io.github.apace100.apoli.util.keybinding.KeyBindingReference;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.client.option.KeyBinding;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class ShappoliKeyBindingReference {
    public static final TypedDataObjectFactory<ShappoliKeyBindingReference> DATA_FACTORY = TypedDataObjectFactory.simple(new SerializableData()
            .add("key", SerializableDataTypes.STRING, null)
            .add("category", SerializableDataTypes.STRING, null)
            .add("continuous", SerializableDataTypes.BOOLEAN, false),
        data -> new ShappoliKeyBindingReference(
            data.getString("key"),
            data.getString("category"),
            data.getBoolean("continuous")
        ),
        (keyBindingReference, serializableData) -> serializableData.instance()
            .set("key", keyBindingReference.key)
            .set("category", keyBindingReference.category)
            .set("continuous", keyBindingReference.continuous)
    );

    public @Nullable String key;
    public @Nullable String category;
    public boolean continuous;

    public ShappoliKeyBindingReference(@Nullable String key, @Nullable String category, boolean continuous) {
        this.key = key;
        this.category = category;
        this.continuous = continuous;
    }

    public static ShappoliKeyBindingReference fromKeyBinding(KeyBinding keyBinding, boolean continuous) {
        return new ShappoliKeyBindingReference(keyBinding.getTranslationKey(), keyBinding.getCategory(), continuous);
    }

    public KeyBindingReference asKeyBindingReference() {
        return new KeyBindingReference(key, continuous);
    }

    @Override
    public String toString() {
        return "Key{" +
            "key='" + key + '\'' +
            ", category='" + category + '\'' +
            ", continuous=" + continuous +
            '}';
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof ShappoliKeyBindingReference otherKey)) {
            return false;
        }

        return Objects.equals(this.key, otherKey.key)
            && Objects.equals(this.category, otherKey.category)
            && this.continuous == otherKey.continuous;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.key, this.category, this.continuous);
    }
}
