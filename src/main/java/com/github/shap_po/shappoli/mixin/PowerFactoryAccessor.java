package com.github.shap_po.shappoli.mixin;

import io.github.apace100.apoli.power.factory.PowerFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(PowerFactory.class)
public interface PowerFactoryAccessor {
    @Accessor("hasConditions")
    boolean shappoli$hasConditions();
}
