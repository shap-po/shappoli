package com.github.shap_po.shappoli.integration.trinkets.slk;

import com.github.shap_po.shappoli.integration.trinkets.data.ShappoliTrinketsDataTypes;
import com.github.shap_po.shappoli.integration.trinkets.data.TrinketSlotData;
import com.github.shap_po.shappoli.util.MiscUtil;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.type.Active;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataType;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.util.Identifier;

import java.util.List;

public class SlotLinkedKey {
    public static final SerializableDataType<SlotLinkedKey> DATA_TYPE = SerializableDataType.compound(
        new SerializableData()
            .add("id", SerializableDataTypes.IDENTIFIER)
            .add("values", Entry.DATA_TYPE.list())
            .add("replace", SerializableDataTypes.BOOLEAN, false)
        ,
        data -> new SlotLinkedKey(
            data.getId("id"),
            data.get("values"),
            data.getBoolean("replace")
        ),
        (slk, serializableData) -> serializableData.instance()
            .set("id", slk.id)
            .set("values", slk.values)
            .set("replace", slk.replace)
    );

    private final Identifier id;
    private final List<Entry> values;
    private final boolean replace;

    public SlotLinkedKey(
        Identifier id,
        List<Entry> values,
        boolean replace
    ) {
        this.id = id;
        this.values = values;
        this.replace = replace;
    }

    public Identifier getId() {
        return id;
    }

    public List<Entry> getValues() {
        return values;
    }

    public List<Active.Key> getKeys() {
        return values.stream().flatMap(entry -> entry.keys.stream()).toList();
    }

    public boolean shouldReplace() {
        return replace;
    }

    public static SlotLinkedKey merge(SlotLinkedKey oldSlotLinkedKey, SlotLinkedKey newSlotLinkedKey) {
        if (newSlotLinkedKey.shouldReplace()) {
            return newSlotLinkedKey;
        }
        oldSlotLinkedKey.values.addAll(newSlotLinkedKey.values);
        return newSlotLinkedKey;
    }

    public static class Entry {
        public static final SerializableDataType<Entry> DATA_TYPE = SerializableDataType.compound(
            new SerializableData()
                .add("slot", ShappoliTrinketsDataTypes.TRINKET_SLOT, null)
                .add("slots", ShappoliTrinketsDataTypes.TRINKET_SLOTS, null)
                .add("key", ApoliDataTypes.BACKWARDS_COMPATIBLE_KEY, null)
                .add("keys", ApoliDataTypes.BACKWARDS_COMPATIBLE_KEY.list(), null)
            ,
            data -> new Entry(
                MiscUtil.listFromData(data, "slot", "slots"),
                MiscUtil.listFromData(data, "key", "keys")
            ),
            (slkEntry, serializableData) -> serializableData.instance()
                .set("slots", slkEntry.slots)
                .set("keys", slkEntry.keys)
        );

        public final List<TrinketSlotData> slots;
        public final List<Active.Key> keys;

        public Entry(List<TrinketSlotData> slots, List<Active.Key> keys) {
            this.slots = slots;
            this.keys = keys;
        }
    }
}
