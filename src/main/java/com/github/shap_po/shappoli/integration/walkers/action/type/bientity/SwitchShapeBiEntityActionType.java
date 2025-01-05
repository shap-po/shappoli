package com.github.shap_po.shappoli.integration.walkers.action.type.bientity;

import com.github.shap_po.shappoli.integration.walkers.action.type.ShappoliWalkersBiEntityActionTypes;
import com.github.shap_po.shappoli.integration.walkers.util.WalkersUtil;
import io.github.apace100.apoli.action.ActionConfiguration;
import io.github.apace100.apoli.action.BiEntityAction;
import io.github.apace100.apoli.action.type.BiEntityActionType;
import io.github.apace100.apoli.data.TypedDataObjectFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class SwitchShapeBiEntityActionType extends BiEntityActionType {
    public static final TypedDataObjectFactory<SwitchShapeBiEntityActionType> DATA_FACTORY = TypedDataObjectFactory.simple(
        new SerializableData()
            .add("ignore_nbt", SerializableDataTypes.BOOLEAN, false)
            .add("action_on_success", BiEntityAction.DATA_TYPE.optional(), Optional.empty()),
        data -> new SwitchShapeBiEntityActionType(
            data.getBoolean("ignore_nbt"),
            data.get("action_on_success")
        ),
        (actionType, serializableData) -> serializableData.instance()
            .set("ignore_nbt", actionType.ignoreNbt)
            .set("action_on_success", actionType.actionOnSuccess)
    );

    private final boolean ignoreNbt;
    private final Optional<BiEntityAction> actionOnSuccess;

    public SwitchShapeBiEntityActionType(boolean ignoreNbt, Optional<BiEntityAction> actionOnSuccess) {
        this.ignoreNbt = ignoreNbt;
        this.actionOnSuccess = actionOnSuccess;
    }

    @Override
    public void execute(Entity actor, Entity target) {
        if (!(actor instanceof ServerPlayerEntity player)) {
            return;
        }
        if (!(target instanceof LivingEntity livingEntity)) {
            return;
        }

        boolean result = WalkersUtil.switchShape(player, livingEntity, ignoreNbt);
        if (result) {
            actionOnSuccess.ifPresent(action -> action.execute(actor, target));
        }
    }

    @Override
    public @NotNull ActionConfiguration<?> getConfig() {
        return ShappoliWalkersBiEntityActionTypes.SWITCH_SHAPE;
    }
}
