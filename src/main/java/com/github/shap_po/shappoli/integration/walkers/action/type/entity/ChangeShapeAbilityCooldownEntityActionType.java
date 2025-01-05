package com.github.shap_po.shappoli.integration.walkers.action.type.entity;

import com.github.shap_po.shappoli.integration.walkers.action.type.ShappoliWalkersEntityActionTypes;
import io.github.apace100.apoli.action.ActionConfiguration;
import io.github.apace100.apoli.action.type.EntityActionType;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.data.TypedDataObjectFactory;
import io.github.apace100.apoli.util.ResourceOperation;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;
import tocraft.walkers.api.PlayerAbilities;

public class ChangeShapeAbilityCooldownEntityActionType extends EntityActionType {
    public static final TypedDataObjectFactory<ChangeShapeAbilityCooldownEntityActionType> DATA_FACTORY = TypedDataObjectFactory.simple(
        new SerializableData()
            .add("operation", ApoliDataTypes.RESOURCE_OPERATION, ResourceOperation.ADD)
            .add("change", SerializableDataTypes.INT),
        data -> new ChangeShapeAbilityCooldownEntityActionType(
            data.get("operation"),
            data.getInt("change")
        ),
        (actionType, serializableData) -> serializableData.instance()
            .set("operation", actionType.operation)
            .set("change", actionType.change)
    );

    private final ResourceOperation operation;
    private final int change;

    public ChangeShapeAbilityCooldownEntityActionType(ResourceOperation operation, int change) {
        this.operation = operation;
        this.change = change;
    }

    @Override
    public void execute(Entity entity) {
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

    @Override
    public @NotNull ActionConfiguration<?> getConfig() {
        return ShappoliWalkersEntityActionTypes.CHANGE_SHAPE_ABILITY_COOLDOWN;
    }
}
