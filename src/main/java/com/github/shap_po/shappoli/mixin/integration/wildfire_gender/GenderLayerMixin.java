package com.github.shap_po.shappoli.mixin.integration.wildfire_gender;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.wildfire.render.GenderLayer;
import com.wildfire.render.WildfireModelRenderer;
import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.power.ModelColorPower;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
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
        at = @At(value = "INVOKE", target = "Lcom/wildfire/render/GenderLayer;renderBox(Lcom/wildfire/render/WildfireModelRenderer$ModelBox;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;IIFFFF)V")
    )
    private void shappoli$modifyRenderBreastColor(
        WildfireModelRenderer.ModelBox model, MatrixStack matrixStack, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn,
        float red, float green, float blue, float alpha, Operation<Void> original,
        T entity
    ) {
        List<ModelColorPower> modelColorPowers = PowerHolderComponent.KEY.get(entity).getPowers(ModelColorPower.class);
        if (!modelColorPowers.isEmpty()) {
            red = shappoli$getNewColor(red, modelColorPowers, ModelColorPower::getRed, (a, b) -> a * b);
            green = shappoli$getNewColor(green, modelColorPowers, ModelColorPower::getGreen, (a, b) -> a * b);
            blue = shappoli$getNewColor(blue, modelColorPowers, ModelColorPower::getBlue, (a, b) -> a * b);
            alpha = shappoli$getNewColor(alpha, modelColorPowers, ModelColorPower::getAlpha, Math::min);
        }
        original.call(model, matrixStack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }

    @Unique
    private float shappoli$getNewColor(
        float original, List<ModelColorPower> modelColorPowers,
        Function<ModelColorPower, Float> colorGetter,
        BinaryOperator<Float> reducer
    ) {
        return modelColorPowers.stream()
            .map(colorGetter)
            .reduce(reducer)
            .orElse(original);
    }
}
