package com.github.shap_po.shappoli.integration.walkers.action.type.entity;

import com.github.shap_po.shappoli.Shappoli;
import io.github.apace100.apoli.action.factory.ActionTypeFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import tocraft.walkers.ability.AbilityRegistry;
import tocraft.walkers.ability.ShapeAbility;
import tocraft.walkers.api.PlayerAbilities;
import tocraft.walkers.api.PlayerShape;
import tocraft.walkers.api.events.ShapeEvents;

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

        // Check cooldown
        if (PlayerAbilities.canUseAbility(player) || force) {
            ShapeAbility<LivingEntity> ability = AbilityRegistry.get(shape);
            if (ability == null) {
                return;
            }

            ActionResult result = ShapeEvents.USE_SHAPE_ABILITY.invoke().use(player, ability);
            // check if ability was canceled
            if (result == ActionResult.FAIL && !force) {
                return;
            }

            ability.onUse(player, shape, shape.getWorld());
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
