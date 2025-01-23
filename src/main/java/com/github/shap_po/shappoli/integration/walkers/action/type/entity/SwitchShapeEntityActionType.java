package com.github.shap_po.shappoli.integration.walkers.action.type.entity;

import com.github.shap_po.shappoli.integration.walkers.action.type.ShappoliWalkersEntityActionTypes;
import com.github.shap_po.shappoli.integration.walkers.util.WalkersUtil;
import io.github.apace100.apoli.action.ActionConfiguration;
import io.github.apace100.apoli.action.EntityAction;
import io.github.apace100.apoli.action.context.EntityActionContext;
import io.github.apace100.apoli.action.type.EntityActionType;
import io.github.apace100.apoli.data.TypedDataObjectFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class SwitchShapeEntityActionType extends EntityActionType {
    public static final TypedDataObjectFactory<SwitchShapeEntityActionType> DATA_FACTORY = TypedDataObjectFactory.simple(
        new SerializableData()
            .add("shape", SerializableDataTypes.IDENTIFIER, null)
            .add("tag", SerializableDataTypes.NBT_COMPOUND, null)
            .add("action_on_success", EntityAction.DATA_TYPE.optional(), Optional.empty()),
        data -> new SwitchShapeEntityActionType(
            data.getId("shape"),
            data.get("tag"),
            data.get("action_on_success")
        ),
        (actionType, serializableData) -> serializableData.instance()
            .set("shape", actionType.shape)
            .set("tag", actionType.tag)
            .set("action_on_success", actionType.actionOnSuccess)
    );

    private final Identifier shape;
    private final NbtCompound tag;
    private final Optional<EntityAction> actionOnSuccess;

    public SwitchShapeEntityActionType(Identifier shape, NbtCompound tag, Optional<EntityAction> actionOnSuccess) {
        this.shape = shape;
        this.tag = tag;
        this.actionOnSuccess = actionOnSuccess;
    }

    @Override
    public void accept(EntityActionContext context) {
        if (!(context.entity() instanceof ServerPlayerEntity player)) {
            return;
        }

        boolean result = WalkersUtil.switchShape(player, shape, tag);

        if (result) {
            actionOnSuccess.ifPresent(action -> action.accept(context));
        }
    }

    @Override
    public @NotNull ActionConfiguration<?> getConfig() {
        return ShappoliWalkersEntityActionTypes.SWITCH_SHAPE;
    }
}
