package com.github.shap_po.shappoli.integration.walkers.action.type.entity;

import com.github.shap_po.shappoli.Shappoli;
import io.github.apace100.apoli.action.factory.ActionTypeFactory;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.util.ResourceOperation;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import tocraft.walkers.api.PlayerAbilities;

public class ChangeShapeAbilityCooldownActionType {
    public static void action(
        Entity entity,
        ResourceOperation operation, int change
    ) {
        if (!(entity instanceof ServerPlayerEntity player)) {
            return;
        }

        int oldValue = PlayerAbilities.getCooldown(player);
        int newValue = processValue(operation, oldValue, change);

        if (oldValue != newValue) {
            PlayerAbilities.setCooldown(player, newValue);
            PlayerAbilities.sync(player);
        }
    }

    private static int processValue(ResourceOperation operation, int oldValue, int newValue) {
        return switch (operation) {
            case ADD -> oldValue + newValue;
            case SET -> newValue;
        };
    }

    public static ActionTypeFactory<Entity> getFactory() {
        return new ActionTypeFactory<>(
            Shappoli.identifier("change_shape_ability_cooldown"),
            new SerializableData()
                .add("operation", ApoliDataTypes.RESOURCE_OPERATION, ResourceOperation.ADD)
                .add("change", SerializableDataTypes.INT)
            ,
            (data, entity) -> action(
                entity,
                data.get("operation"), data.getInt("change")
            )
        );
    }
}
