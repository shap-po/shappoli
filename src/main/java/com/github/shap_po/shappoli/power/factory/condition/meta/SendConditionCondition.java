package com.github.shap_po.shappoli.power.factory.condition.meta;

import com.github.shap_po.shappoli.Shappoli;
import com.github.shap_po.shappoli.power.ReceiveConditionPower;
import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.calio.data.SerializableData;
import net.minecraft.entity.Entity;

import java.util.function.BiPredicate;
import java.util.function.Function;

public class SendConditionCondition {
    public static <T> boolean condition(
        SerializableData.Instance data, T condition,
        Function<T, Entity> conditionToEntityFunction,
        BiPredicate<ReceiveConditionPower, T> sendFunction
    ) {
        Entity entity = conditionToEntityFunction.apply(condition);
        if (entity == null) {
            Shappoli.LOGGER.warn("Tried to send a condition to a null entity. Probably something went wrong on Shappoli's side. Please report this to the mod author.");
            return false;
        }

        PowerType<?> powerType = data.get("receiver");
        Power power = PowerHolderComponent.KEY.get(entity).getPower(powerType);
        if (power instanceof ReceiveConditionPower listener) {
            return listener.isActive() && sendFunction.test(listener, condition);
        }
        Shappoli.LOGGER.warn("Tried to send a condition to a power that does not exist or is not a condition receiver: {}", powerType.getIdentifier());
        return false;
    }

    public static <T> ConditionFactory<T> getFactory(
        Function<T, Entity> conditionToEntityFunction,
        BiPredicate<ReceiveConditionPower, T> sendFunction
    ) {
        return new ConditionFactory<>(
            Shappoli.identifier("send_condition"),
            new SerializableData()
                .add("receiver", ApoliDataTypes.POWER_TYPE)
            ,
            (data, condition) -> condition(
                data,
                condition,
                conditionToEntityFunction,
                sendFunction
            )
        );
    }
}
