package com.github.shap_po.shappoli.mixin.integration.walkers;

import com.github.shap_po.shappoli.integration.walkers.power.ActionOnShapeAbilityUsePower;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.apace100.apoli.component.PowerHolderComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import tocraft.craftedcore.network.ModernNetworking;
import tocraft.walkers.ability.AbilityRegistry;
import tocraft.walkers.api.PlayerAbilities;
import tocraft.walkers.api.PlayerShape;
import tocraft.walkers.network.ServerNetworking;

import java.util.Objects;

@Mixin(ServerNetworking.class)
public class ServerNetworkingMixin {
    @WrapOperation(
        method = "registerUseAbilityPacketHandler",
        at = @At(
            value = "INVOKE",
            target = "Ltocraft/craftedcore/network/ModernNetworking;registerReceiver(Ltocraft/craftedcore/network/ModernNetworking$Side;Lnet/minecraft/util/Identifier;Ltocraft/craftedcore/network/ModernNetworking$Receiver;)V"
        )
    )
    private static void shappoli$onAbilityUse(ModernNetworking.Side side, Identifier id, ModernNetworking.Receiver receiver, Operation<Void> original) {
        ModernNetworking.Receiver newReceiver = (context, packet) -> {
            PlayerEntity player = context.getPlayer();
            Objects.requireNonNull(context.getPlayer().getServer()).execute(() -> {
                LivingEntity shape = PlayerShape.getCurrentShape(player);
                if (shape != null && AbilityRegistry.has(shape) && PlayerAbilities.canUseAbility(player)) {
                    PowerHolderComponent.withPowerTypes(player, ActionOnShapeAbilityUsePower.class, ActionOnShapeAbilityUsePower::doesApply, ActionOnShapeAbilityUsePower::apply);
                }
            });
            receiver.receive(context, packet);
        };
        original.call(side, id, newReceiver);
    }
}
