package com.github.shap_po.shappoli.integration.trinkets.data;

import com.github.shap_po.shappoli.util.MiscUtil;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.SlotType;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TrinketSlotData {
    public static final SerializableData DATA = new SerializableData()
        .add("name", SerializableDataTypes.STRING, null)
        .add("group", SerializableDataTypes.STRING, null)
        .add("index", SerializableDataTypes.INT, null);

    public final @Nullable String name;
    public final @Nullable String group;
    public final @Nullable Integer index;

    public TrinketSlotData(@Nullable String name, @Nullable String group, @Nullable Integer index) {
        this.name = name;
        this.group = group;
        this.index = index;
    }

    public boolean test(SlotReference slotReference) {
        SlotType slotType = slotReference.inventory().getSlotType();
        return (group == null || group.equals(slotType.getGroup())) &&
            (name == null || name.equals(slotType.getName())) &&
            (index == null || index == slotReference.index());
    }

    /**
     * @param data The data to get the slots from. Data can contain a single slot or/and a list of slots.
     * @return A list of trinket slot data from the given data.
     */
    public static List<TrinketSlotData> getSlots(SerializableData.Instance data) {
        return MiscUtil.listFromData(data, "slot", "slots");
    }

    /**
     * @return The id of the slot in the format "name/group".
     */
    public String getId() {
        return name + "/" + group;
    }

    public static TrinketSlotData fromData(SerializableData.Instance dataInstance) {
        return new TrinketSlotData(
            dataInstance.get("name"),
            dataInstance.get("group"),
            dataInstance.isPresent("index") ? dataInstance.getInt("index") : null
        );
    }

    public static SerializableData.Instance toData(SerializableData data, TrinketSlotData slot) {
        SerializableData.Instance dataInstance = data.new Instance();
        dataInstance.set("name", slot.name);
        dataInstance.set("group", slot.group);
        dataInstance.set("index", slot.index);
        return dataInstance;
    }
}
