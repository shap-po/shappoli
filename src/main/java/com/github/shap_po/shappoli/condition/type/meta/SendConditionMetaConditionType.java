package com.github.shap_po.shappoli.condition.type.meta;

import com.github.shap_po.shappoli.Shappoli;
import com.github.shap_po.shappoli.power.type.ReceiveConditionPowerType;
import com.github.shap_po.shappoli.util.AbstractSender;
import io.github.apace100.apoli.condition.AbstractCondition;
import io.github.apace100.apoli.condition.ConditionConfiguration;
import io.github.apace100.apoli.condition.type.AbstractConditionType;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.PowerReference;
import io.github.apace100.apoli.power.type.PowerType;
import io.github.apace100.apoli.util.context.TypeConditionContext;
import io.github.apace100.calio.data.SerializableData;
import net.minecraft.entity.Entity;

import java.util.function.BiPredicate;
import java.util.function.Function;

public interface SendConditionMetaConditionType<T extends TypeConditionContext, C extends AbstractCondition<T, ? extends AbstractConditionType<T, C>>> extends AbstractSender {
    static <T extends TypeConditionContext> boolean send(
        Entity entity,
        T conditionContext,
        PowerReference receiver,
        BiPredicate<ReceiveConditionPowerType, T> sendFunction
    ) {
        if (entity == null) {
            Shappoli.LOGGER.warn("Tried to send a condition to a null entity. Probably something went wrong on Shappoli's side. Please report this to the mod author.");
            return false;
        }

        PowerType power = receiver.getNullablePowerType(entity);
        if (power == null) {
            Shappoli.LOGGER.warn("Tried to send a condition to a power that does not exist: {}", receiver.id());
            return false;
        }

        if (power instanceof ReceiveConditionPowerType listener) {
            return listener.isActive() && sendFunction.test(listener, conditionContext);
        }
        Shappoli.LOGGER.warn("Tried to send a condition to a power that does not exist or is not a condition receiver: {}", receiver.id());
        return false;
    }

    static <T extends TypeConditionContext, C extends AbstractCondition<T, CT>, CT extends AbstractConditionType<T, C>, M extends AbstractConditionType<T, C> & SendConditionMetaConditionType<T, C>> ConditionConfiguration<M> createConfiguration(Function<PowerReference, M> constructor) {
        return ConditionConfiguration.of(
            Shappoli.identifier("send_condition"),
            new SerializableData()
                .add("receiver", ApoliDataTypes.POWER_REFERENCE),
            data -> constructor.apply(
                data.get("receiver")
            ),
            (m, serializableData) -> serializableData.instance()
                .set("receiver", m.getReceiver())
        );
    }
}
