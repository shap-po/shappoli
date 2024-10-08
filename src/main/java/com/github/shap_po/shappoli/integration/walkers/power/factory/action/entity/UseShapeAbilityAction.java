package com.github.shap_po.shappoli.integration.walkers.power.factory.action.entity;

import com.github.shap_po.shappoli.Shappoli;
import com.github.shap_po.shappoli.integration.walkers.power.ActionOnShapeAbilityUsePower;
import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import tocraft.walkers.ability.AbilityRegistry;
import tocraft.walkers.api.PlayerAbilities;
import tocraft.walkers.api.PlayerShape;

public class UseShapeAbilityAction {
    public static void action(SerializableData.Instance data, Entity entity) {
        if (!(entity instanceof ServerPlayerEntity player)) {
            return;
        }

        LivingEntity shape = PlayerShape.getCurrentShape(player);
        if (shape == null || !AbilityRegistry.has(shape)) {
            return;
        }

        if (PlayerAbilities.canUseAbility(player) || data.getBoolean("force")) {
            PowerHolderComponent.withPowers(player, ActionOnShapeAbilityUsePower.class, ActionOnShapeAbilityUsePower::doesApply, ActionOnShapeAbilityUsePower::apply);

            AbilityRegistry.get(shape).onUse(player, shape, player.getWorld());
            if (data.getBoolean("apply_cooldown")) {
                PlayerAbilities.setCooldown(player, AbilityRegistry.get(shape).getCooldown(shape));
                PlayerAbilities.sync(player);
            }
        }
    }

    public static ActionFactory<Entity> getFactory() {
        return new ActionFactory<>(
            Shappoli.identifier("use_shape_ability"),
            new SerializableData()
                .add("force", SerializableDataTypes.BOOLEAN, false)
                .add("apply_cooldown", SerializableDataTypes.BOOLEAN, true)
            ,
            UseShapeAbilityAction::action
        );
    }
}
