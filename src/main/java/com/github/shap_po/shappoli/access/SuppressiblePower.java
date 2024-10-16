package com.github.shap_po.shappoli.access;

import net.minecraft.entity.Entity;
import org.jetbrains.annotations.Nullable;

public interface SuppressiblePower {
    default boolean shappoli$canBeSuppressed() {
        return true;
    }

    boolean shappoli$hasConditions();

    void shappoli$setHasConditions(boolean hasConditionTypes);

    @SuppressWarnings({"unused"})
    @Nullable
    Entity shappoli$getSupressingEntity();

    boolean shappoli$suppressFor(int duration, Entity supressingEntity);

    boolean shappoli$isSuppressed();
}
