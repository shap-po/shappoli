package com.github.shap_po.shappoli.mixin.integration.wildfire_gender;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.wildfire.render.GenderLayer;
import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.power.type.ModelColorPowerType;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

@Mixin(GenderLayer.class)
public abstract class GenderLayerMixin<T extends LivingEntity> {
    @WrapOperation(
        method = "renderBreast",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/ColorHelper$Argb;getArgb(IIII)I")
    )
    private int shappoli$modifyRenderBreastColor(int alpha, int red, int green, int blue, Operation<Integer> original, T entity) {
        List<ModelColorPowerType> modelColorPowers = PowerHolderComponent.KEY.get(entity).getPowerTypes(ModelColorPowerType.class);
        if (!modelColorPowers.isEmpty()) {
            red = Math.round(
                modelColorPowers
                    .stream()
                    .map(ModelColorPowerType::getRed)
                    .reduce((a, b) -> a * b)
                    .orElse((float) red)
            );
            green = Math.round(
                modelColorPowers
                    .stream()
                    .map(ModelColorPowerType::getGreen)
                    .reduce((a, b) -> a * b)
                    .orElse((float) green)
            );
            blue = Math.round(
                modelColorPowers
                    .stream()
                    .map(ModelColorPowerType::getBlue)
                    .reduce((a, b) -> a * b)
                    .orElse((float) blue)
            );
            alpha = Math.round(
                modelColorPowers
                    .stream()
                    .map(ModelColorPowerType::getAlpha)
                    .min(Float::compare)
                    .orElse((float) alpha)
            );
        }
        original.call(alpha, red, green, blue);
        return alpha;
    }
}
