package com.github.shap_po.shappoli.data;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.SlotType;
import org.jetbrains.annotations.Nullable;

public class TrinketSlotData {
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
}
