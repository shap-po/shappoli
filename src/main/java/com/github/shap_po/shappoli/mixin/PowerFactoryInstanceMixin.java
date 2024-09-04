package com.github.shap_po.shappoli.mixin;

import com.github.shap_po.shappoli.access.SuppressiblePower;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.factory.PowerFactory;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PowerFactory.Instance.class)
public class PowerFactoryInstanceMixin<P extends Power> {
    @Shadow
    @Final
    PowerFactory<P> this$0;

    @Inject(method = "apply*", at = @At(value = "RETURN"))
    private void shappoli$apply(CallbackInfoReturnable<P> cir) {
        P power = cir.getReturnValue();
        ((SuppressiblePower) power).shappoli$setHasConditions(((PowerFactoryAccessor) this$0).shappoli$hasConditions());
    }
}
