package com.github.shap_po.shappoli.access;

import io.github.apace100.apoli.power.type.PowerType;
import net.minecraft.entity.Entity;
import org.jetbrains.annotations.Nullable;

public interface SuppressiblePower {
    default boolean shappoli$canBeSuppressed() {
        return true;
    }

    @SuppressWarnings({"unused"})
    @Nullable
    Entity shappoli$getSupressingEntity();

    boolean shappoli$suppressFor(int duration, Entity supressingEntity);

    boolean shappoli$isSuppressed();

    /**
     * Checks if the power type has a condition field
     *
     * @param powerType power type
     * @return true if the power type has a condition field
     */
    static boolean hasCondition(PowerType powerType) {
        return powerType.getConfig().dataFactory().getSerializableData().containsField("condition");
    }
}
