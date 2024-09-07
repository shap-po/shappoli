package com.github.shap_po.shappoli.integration.walkers.power.factory.action.entity;

import com.github.shap_po.shappoli.Shappoli;
import com.github.shap_po.shappoli.integration.walkers.util.WalkersUtil;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.apoli.power.factory.action.EntityActions;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

public class MorphAction {
    public static void action(SerializableData.Instance data, Entity entity) {
        if (!(entity instanceof ServerPlayerEntity player)) {
            return;
        }

        Identifier shapeId = data.getId("shape");
        NbtCompound nbt = data.get("nbt");

        boolean result = WalkersUtil.switchShape(player, shapeId, nbt);

        Consumer<Entity> action = data.get("action_on_success");
        if (result && action != null) {
            action.accept(entity);
        }
    }

    public static ActionFactory<Entity> getFactory() {
        ActionFactory<Entity> factory = new ActionFactory<>(Shappoli.identifier("morph"),
            new SerializableData()
                .add("shape", SerializableDataTypes.IDENTIFIER, null)
                .add("nbt", SerializableDataTypes.NBT, null)
                .add("action_on_success", ApoliDataTypes.ENTITY_ACTION, null)
            ,
            MorphAction::action
        );

        EntityActions.ALIASES.addPathAlias("switch_shape", factory.getSerializerId().getPath());
        return factory;
    }
}
