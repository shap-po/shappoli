package com.github.shap_po.shappoli.mixin;

import io.github.apace100.apoli.power.factory.PowerTypeFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(PowerTypeFactory.class)
public interface PowerTypeFactoryAccessor {
    @Accessor("hasConditions")
    boolean shappoli$hasConditions();
}
