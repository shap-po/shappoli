package com.github.shap_po.shappoli.integration.trinkets.data;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.SlotType;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataType;
import io.github.apace100.calio.data.SerializableDataTypes;
import org.jetbrains.annotations.Nullable;

public record TrinketSlotFilter(@Nullable String name, @Nullable String group, @Nullable Integer index) {
    public static final SerializableDataType<TrinketSlotFilter> DATA_TYPE = SerializableDataType.compound(
        new SerializableData()
            .add("name", SerializableDataTypes.STRING, null)
            .add("group", SerializableDataTypes.STRING, null)
            .add("index", SerializableDataTypes.INT, null),
        (data) -> new TrinketSlotFilter(
            data.get("name"),
            data.get("group"),
            data.isPresent("index") ? data.getInt("index") : null
        ),
        (slot, serializableData) -> serializableData.instance()
            .set("name", slot.name)
            .set("group", slot.group)
            .set("index", slot.index)
    );

    public boolean test(SlotReference slotReference) {
        SlotType slotType = slotReference.inventory().getSlotType();
        return (group == null || group.equals(slotType.getGroup())) &&
            (name == null || name.equals(slotType.getName())) &&
            (index == null || index == slotReference.index());
    }

    /**
     * @return The id of the slot in the format "name/group".
     */
    public String getId() {
        return name + "/" + group;
    }
}
