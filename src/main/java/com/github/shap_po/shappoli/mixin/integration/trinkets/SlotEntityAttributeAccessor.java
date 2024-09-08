package com.github.shap_po.shappoli.mixin.integration.trinkets;

import dev.emi.trinkets.api.SlotAttributes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(SlotAttributes.SlotEntityAttribute.class)
public interface SlotEntityAttributeAccessor {
    @Invoker("<init>")
    static SlotAttributes.SlotEntityAttribute init(String slot) {
        throw new AssertionError();
    }
}
