package com.github.shap_po.shappoli.power.factory.action.meta;

import com.github.shap_po.shappoli.Shappoli;
import com.github.shap_po.shappoli.power.ActionOnEventReceivePower;
import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.util.IdentifierAlias;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class SendEventAction {
    private static final Identifier ID = Shappoli.identifier("send_event");

    public static <T> void action(
        SerializableData.Instance data,
        T t,
        Function<T, Entity> actionToEntityFunction,
        BiConsumer<ActionOnEventReceivePower, T> sendFunction
    ) {
        Entity entity = actionToEntityFunction.apply(t);
        if (entity == null) {
            Shappoli.LOGGER.warn("Tried to send an event to a null entity");
            return;
        }

        Optional<PowerHolderComponent> maybeComponent = PowerHolderComponent.KEY.maybeGet(entity);
        if (maybeComponent.isEmpty()) {
            Shappoli.LOGGER.warn("Tried to send an event to an entity without a power holder component: {}", entity);
            return;
        }
        PowerHolderComponent component = maybeComponent.get();

        PowerType<?> powerType = data.get("receiver");
        Power power = component.getPower(powerType);

        if (power instanceof ActionOnEventReceivePower listener) {
            sendFunction.accept(listener, t);
        } else {
            Shappoli.LOGGER.warn("Tried to send an event to a power that is not a receiver: {}", powerType.getIdentifier());
        }
    }

    public static void addAlias(IdentifierAlias aliases) {
        aliases.addPathAlias("emit_event", ID.getPath());
    }

    public static <T> ActionFactory<T> getFactory(
        Function<T, Entity> actionToEntityFunction,
        BiConsumer<ActionOnEventReceivePower, T> sendFunction
    ) {
        return new ActionFactory<>(ID,
            new SerializableData()
                .add("listener", ApoliDataTypes.POWER_TYPE, null) // alias
                .addFunctionedDefault("receiver", ApoliDataTypes.POWER_TYPE, data -> data.get("listener"))
            ,
            (instance, pair) -> action(
                instance,
                pair,
                actionToEntityFunction,
                sendFunction
            )
        );
    }
}
