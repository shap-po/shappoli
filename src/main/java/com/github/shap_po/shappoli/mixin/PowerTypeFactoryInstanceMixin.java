package com.github.shap_po.shappoli.mixin;

import com.github.shap_po.shappoli.access.SuppressiblePower;
import io.github.apace100.apoli.power.factory.PowerTypeFactory;
import io.github.apace100.apoli.power.type.PowerType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PowerTypeFactory.Instance.class)
public class PowerTypeFactoryInstanceMixin<P extends PowerType> {
    @Shadow
    @Final
    PowerTypeFactory<P> this$0;

    @Inject(method = "apply*", at = @At(value = "RETURN"))
    private void shappoli$apply(CallbackInfoReturnable<P> cir) {
        P power = cir.getReturnValue();
        ((SuppressiblePower) power).shappoli$setHasConditions(((PowerTypeFactoryAccessor) this$0).shappoli$hasConditions());
    }
}
