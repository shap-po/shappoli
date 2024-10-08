package com.github.shap_po.shappoli.integration.walkers.action.type.entity;

import com.github.shap_po.shappoli.Shappoli;
import com.github.shap_po.shappoli.integration.walkers.power.type.ActionOnShapeAbilityUsePowerType;
import io.github.apace100.apoli.action.factory.ActionTypeFactory;
import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import tocraft.walkers.ability.AbilityRegistry;
import tocraft.walkers.api.PlayerAbilities;
import tocraft.walkers.api.PlayerShape;

public class UseShapeAbilityActionType {
    public static void action(
        Entity entity,
        boolean force,
        boolean applyCooldown
    ) {
        if (!(entity instanceof ServerPlayerEntity player)) {
            return;
        }

        LivingEntity shape = PlayerShape.getCurrentShape(player);
        if (shape == null || !AbilityRegistry.has(shape)) {
            return;
        }

        if (PlayerAbilities.canUseAbility(player) || force) {
            PowerHolderComponent.withPowerTypes(player, ActionOnShapeAbilityUsePowerType.class, ActionOnShapeAbilityUsePowerType::doesApply, ActionOnShapeAbilityUsePowerType::apply);

            AbilityRegistry.get(shape).onUse(player, shape, player.getWorld());
            if (applyCooldown) {
                PlayerAbilities.setCooldown(player, AbilityRegistry.get(shape).getCooldown(shape));
                PlayerAbilities.sync(player);
            }
        }
    }

    public static ActionTypeFactory<Entity> getFactory() {
        return new ActionTypeFactory<>(
            Shappoli.identifier("use_shape_ability"),
            new SerializableData()
                .add("force", SerializableDataTypes.BOOLEAN, false)
                .add("apply_cooldown", SerializableDataTypes.BOOLEAN, true)
            ,
            (data, entity) -> action(
                entity,
                data.getBoolean("force"),
                data.getBoolean("apply_cooldown")
            )
        );
    }
}
