package com.github.shap_po.shappoli.integration.walkers.action.type.entity;

import com.github.shap_po.shappoli.Shappoli;
import com.github.shap_po.shappoli.integration.walkers.util.WalkersUtil;
import io.github.apace100.apoli.action.factory.ActionTypeFactory;
import io.github.apace100.apoli.action.factory.EntityActions;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class SwitchShapeActionType {
    public static void action(
        Entity entity,
        Identifier shapeId, NbtCompound nbt,
        @Nullable Consumer<Entity> actionOnSuccess
    ) {
        if (!(entity instanceof ServerPlayerEntity player)) {
            return;
        }

        boolean result = WalkersUtil.switchShape(player, shapeId, nbt);

        if (result && actionOnSuccess != null) {
            actionOnSuccess.accept(entity);
        }
    }

    public static ActionTypeFactory<Entity> getFactory() {
        ActionTypeFactory<Entity> factory = new ActionTypeFactory<>(
            Shappoli.identifier("switch_shape"),
            new SerializableData()
                .add("shape", SerializableDataTypes.IDENTIFIER, null)
                .add("nbt", SerializableDataTypes.NBT_COMPOUND, null)
                .add("action_on_success", ApoliDataTypes.ENTITY_ACTION, null)
            ,
            (data, entity) -> action(
                entity,
                data.getId("shape"), data.get("nbt"),
                data.get("action_on_success")
            )
        );

        EntityActions.ALIASES.addPathAlias("change_shape", factory.getSerializerId().getPath());
        EntityActions.ALIASES.addPathAlias("morph", factory.getSerializerId().getPath());
        return factory;
    }
}
