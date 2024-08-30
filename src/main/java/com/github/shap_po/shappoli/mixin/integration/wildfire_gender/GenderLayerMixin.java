package com.github.shap_po.shappoli.mixin.integration.wildfire_gender;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.wildfire.render.BreastSide;
import com.wildfire.render.GenderLayer;
import com.wildfire.render.WildfireModelRenderer;
import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.power.ModelColorPower;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

@Mixin(GenderLayer.class)
public abstract class GenderLayerMixin<T extends LivingEntity> {
    @Shadow
    protected abstract void renderBreast(T entity, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int packedLightIn, int packedOverlayIn, BreastSide side);

    @Shadow
    protected static void renderBox(WildfireModelRenderer.ModelBox model, MatrixStack matrixStack, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
    }

    @WrapOperation(
        method = "renderBreast",
        at = @At(value = "INVOKE", target = "Lcom/wildfire/render/GenderLayer;renderBox(Lcom/wildfire/render/WildfireModelRenderer$ModelBox;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;IIFFFF)V")
    )
    private void shappoli$modifyRenderBreastColor(WildfireModelRenderer.ModelBox model, MatrixStack matrixStack, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha, Operation<Void> original, LivingEntity entity) {
        List<ModelColorPower> modelColorPowers = PowerHolderComponent.KEY.get(entity).getPowers(ModelColorPower.class);
        if (!modelColorPowers.isEmpty()) {
            red = modelColorPowers.stream().map(ModelColorPower::getRed).reduce((a, b) -> a * b).orElse(red);
            green = modelColorPowers.stream().map(ModelColorPower::getGreen).reduce((a, b) -> a * b).orElse(green);
            blue = modelColorPowers.stream().map(ModelColorPower::getBlue).reduce((a, b) -> a * b).orElse(blue);
            alpha = modelColorPowers.stream().map(ModelColorPower::getAlpha).min(Float::compare).orElse(alpha);
        }
        original.call(model, matrixStack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }
}
