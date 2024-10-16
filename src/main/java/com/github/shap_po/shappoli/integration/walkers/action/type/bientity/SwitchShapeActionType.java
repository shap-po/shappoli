package com.github.shap_po.shappoli.integration.walkers.action.type.bientity;

import com.github.shap_po.shappoli.Shappoli;
import com.github.shap_po.shappoli.integration.walkers.util.WalkersUtil;
import io.github.apace100.apoli.action.factory.ActionTypeFactory;
import io.github.apace100.apoli.action.type.BiEntityActionTypes;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Pair;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class SwitchShapeActionType {
    public static void action(
        Entity actor, Entity target,
        boolean ignoreNbt,
        @Nullable Consumer<Pair<Entity, Entity>> actionOnSuccess
    ) {
        if (!(actor instanceof ServerPlayerEntity player)) {
            return;
        }
        if (!(target instanceof LivingEntity livingEntity)) {
            return;
        }

        boolean result = WalkersUtil.switchShape(player, livingEntity, ignoreNbt);

        if (result && actionOnSuccess != null) {
            actionOnSuccess.accept(new Pair<>(actor, target));
        }
    }

    public static ActionTypeFactory<Pair<Entity, Entity>> getFactory() {
        ActionTypeFactory<Pair<Entity, Entity>> factory = new ActionTypeFactory<>(
            Shappoli.identifier("switch_shape"),
            new SerializableData()
                .add("ignore_nbt", SerializableDataTypes.BOOLEAN, false)
                .add("action_on_success", ApoliDataTypes.BIENTITY_ACTION, null)
            ,
            (data, actorAndTarget) -> action(
                actorAndTarget.getLeft(), actorAndTarget.getRight(),
                data.getBoolean("ignore_nbt"),
                data.get("action_on_success")
            )
        );

        BiEntityActionTypes.ALIASES.addPathAlias("change_shape", factory.getSerializerId().getPath());
        BiEntityActionTypes.ALIASES.addPathAlias("morph", factory.getSerializerId().getPath());
        return factory;
    }
}
