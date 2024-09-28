package com.github.shap_po.shappoli.condition.type.meta;

import com.github.shap_po.shappoli.Shappoli;
import com.github.shap_po.shappoli.power.type.ReceiveConditionPowerType;
import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.condition.factory.ConditionTypeFactory;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.type.PowerType;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.util.IdentifierAlias;
import net.minecraft.entity.Entity;

import java.util.function.BiPredicate;
import java.util.function.Function;

public class SendConditionConditionType {
    public static <T> boolean condition(
        SerializableData.Instance data, T condition,
        Function<T, Entity> conditionToEntityFunction,
        BiPredicate<ReceiveConditionPowerType, T> sendFunction
    ) {
        Entity entity = conditionToEntityFunction.apply(condition);
        if (entity == null) {
            Shappoli.LOGGER.warn("Tried to send a condition to a null entity. Probably something went wrong on Shappoli's side. Please report this to the mod author.");
            return false;
        }

        Power receiver = data.get("receiver");
        PowerType power = PowerHolderComponent.KEY.get(entity).getPowerType(receiver);
        if (power instanceof ReceiveConditionPowerType listener) {
            return listener.isActive() && sendFunction.test(listener, condition);
        }
        Shappoli.LOGGER.warn("Tried to send a condition to a power that does not exist or is not a condition receiver: {}", receiver.getId());
        return false;
    }

    public static <T> ConditionTypeFactory<T> getFactory(
        Function<T, Entity> conditionToEntityFunction,
        BiPredicate<ReceiveConditionPowerType, T> sendFunction,
        IdentifierAlias aliasProvider
    ) {
        ConditionTypeFactory<T> factory = new ConditionTypeFactory<>(
            Shappoli.identifier("send_condition"),
            new SerializableData()
                .add("receiver", ApoliDataTypes.POWER_REFERENCE)
            ,
            (data, condition) -> condition(
                data,
                condition,
                conditionToEntityFunction,
                sendFunction
            )
        );

        aliasProvider.addPathAlias("request_condition", factory.getSerializerId().getPath());
        return factory;
    }
}
