package com.github.shap_po.shappoli.action.type.meta;

import com.github.shap_po.shappoli.Shappoli;
import com.github.shap_po.shappoli.power.type.ReceiveActionPowerType;
import io.github.apace100.apoli.action.factory.ActionTypeFactory;
import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.type.PowerType;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.util.IdentifierAlias;
import net.minecraft.entity.Entity;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class SendActionActionType {
    public static <T> void action(
        T actionData,
        Entity entity,
        Power receiver,
        BiConsumer<ReceiveActionPowerType, T> sendFunction
    ) {
        if (entity == null) {
            Shappoli.LOGGER.warn("Tried to send an action to a null entity. Probably something went wrong on Shappoli's side. Please report this to the mod author.");
            return;
        }

        PowerType power = PowerHolderComponent.KEY.get(entity).getPowerType(receiver);

        if (power instanceof ReceiveActionPowerType listener) {
            sendFunction.accept(listener, actionData);
        } else {
            Shappoli.LOGGER.warn("Tried to send an action to a power that does not exist or is not an action receiver: {}", receiver.getId());
        }
    }

    public static <T> ActionTypeFactory<T> getFactory(
        Function<T, Entity> actionToEntityFunction,
        BiConsumer<ReceiveActionPowerType, T> sendFunction,
        IdentifierAlias aliasProvider
    ) {
        return new ActionTypeFactory<>(
            Shappoli.identifier("send_action"),
            new SerializableData()
                .add("receiver", ApoliDataTypes.POWER_REFERENCE)
            ,
            (data, actionData) -> action(
                actionData,
                actionToEntityFunction.apply(actionData),
                data.get("receiver"),
                sendFunction
            )
        );
    }
}
