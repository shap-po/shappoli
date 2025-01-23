package com.github.shap_po.shappoli.action.type.meta;

import com.github.shap_po.shappoli.Shappoli;
import com.github.shap_po.shappoli.power.type.ReceiveActionPowerType;
import com.github.shap_po.shappoli.util.AbstractSender;
import io.github.apace100.apoli.action.AbstractAction;
import io.github.apace100.apoli.action.ActionConfiguration;
import io.github.apace100.apoli.action.type.AbstractActionType;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.PowerReference;
import io.github.apace100.apoli.power.type.PowerType;
import io.github.apace100.apoli.util.context.ActionContext;
import io.github.apace100.calio.data.SerializableData;
import net.minecraft.entity.Entity;

import java.util.function.BiConsumer;
import java.util.function.Function;

public interface SendActionMetaActionType<T extends ActionContext<?>, A extends AbstractAction<T, ? extends AbstractActionType<T, A>>> extends AbstractSender {
    static <T extends ActionContext<?>> void send(
        Entity entity,
        T actionContext,
        PowerReference receiver,
        BiConsumer<ReceiveActionPowerType, T> sendFunction
    ) {
        if (entity == null) {
            Shappoli.LOGGER.warn("Tried to send an action to a null entity. Probably something went wrong on Shappoli's side. Please report this to the mod author.");
            return;
        }

        PowerType power = receiver.getNullablePowerType(entity);
        if (power == null) {
            Shappoli.LOGGER.warn("Tried to send an action to a power that does not exist: {}", receiver.id());
            return;
        }

        if (power instanceof ReceiveActionPowerType listener) {
            sendFunction.accept(listener, actionContext);
        } else {
            Shappoli.LOGGER.warn("Tried to send an action to a power that does not exist or is not an action receiver: {}", receiver.id());
        }
    }

    static <T extends ActionContext<?>, A extends AbstractAction<T, AT>, AT extends AbstractActionType<T, A>, M extends AbstractActionType<T, A> & SendActionMetaActionType<T, A>> ActionConfiguration<M> createConfiguration(Function<PowerReference, M> constructor) {
        return ActionConfiguration.of(
            Shappoli.identifier("send_action"),
            new SerializableData()
                .add("receiver", ApoliDataTypes.POWER_REFERENCE),
            data -> constructor.apply(data.get("receiver")),
            (m, serializableData) -> serializableData.instance()
                .set("receiver", m.getReceiver())
        );
    }
}
