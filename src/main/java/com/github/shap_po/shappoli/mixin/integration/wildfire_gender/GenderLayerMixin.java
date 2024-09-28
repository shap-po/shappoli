package com.github.shap_po.shappoli.mixin.integration.wildfire_gender;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.wildfire.render.GenderLayer;
import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.power.type.ModelColorPowerType;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;
import java.util.function.BinaryOperator;
import java.util.function.Function;

@Mixin(GenderLayer.class)
public abstract class GenderLayerMixin<T extends LivingEntity> {
    @WrapOperation(
        method = "renderBreast",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/ColorHelper$Argb;getArgb(IIII)I")
    )
    private int shappoli$modifyRenderBreastColor(int alpha, int red, int green, int blue, Operation<Integer> original, T entity) {
        List<ModelColorPowerType> modelColorPowers = PowerHolderComponent.KEY.get(entity).getPowerTypes(ModelColorPowerType.class);

        if (!modelColorPowers.isEmpty()) {
            red = shappoli$getNewColor(red, modelColorPowers, ModelColorPowerType::getRed, (a, b) -> a * b);
            green = shappoli$getNewColor(green, modelColorPowers, ModelColorPowerType::getGreen, (a, b) -> a * b);
            blue = shappoli$getNewColor(blue, modelColorPowers, ModelColorPowerType::getBlue, (a, b) -> a * b);
            alpha = shappoli$getNewColor(alpha, modelColorPowers, ModelColorPowerType::getAlpha, Math::min);
        }

        return original.call(alpha, red, green, blue);
    }

    @Unique
    private int shappoli$getNewColor(
        int original, List<ModelColorPowerType> modelColorPowers,
        Function<ModelColorPowerType, Float> colorGetter,
        BinaryOperator<Float> reducer
    ) {
        return Math.round(
            modelColorPowers.stream()
                .map(colorGetter)
                .reduce(reducer)
                .map(c -> c * 255)
                .orElse((float) original)
        );
    }
}
