package com.github.shap_po.shappoli.power.factory.action.bientity;

import com.github.shap_po.shappoli.Shappoli;
import com.github.shap_po.shappoli.access.SuppressiblePower;
import com.github.shap_po.shappoli.data.ShappoliDataTypes;
import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.PowerTypeReference;
import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class SuppressPowerAction {
    public static void action(SerializableData.Instance data, Pair<Entity, Entity> actorAndTarget) {
        List<PowerTypeReference<?>> powers = new ArrayList<>();
        data.<PowerTypeReference<?>>ifPresent("power", powers::add);
        data.<List<PowerTypeReference<?>>>ifPresent("powers", powers::addAll);
        int duration = data.getInt("duration");
        Consumer<Pair<Entity, Entity>> bientity_action = data.get("bientity_action");

        PowerHolderComponent component = PowerHolderComponent.KEY.get(actorAndTarget.getRight());

        boolean suppressed = false;

        for (var powerType : powers) {
            SuppressiblePower power = (SuppressiblePower) component.getPower(powerType);
            if (power == null) {
                continue;
            }

            if (!power.shappoli$canBeSuppressed()) {
                Shappoli.LOGGER.error("Tried to suppress a power that cannot be suppressed: {}", powerType.getIdentifier());
                continue;
            }
            if (!power.shappoli$hasConditions() && !data.getBoolean("ignore_warning")) {
                Shappoli.LOGGER.warn("Suppressed power that does not support conditions: {}. If you want to ignore this message, set the \"ignore_warning\" parameter to true", powerType.getIdentifier());
            }

            boolean result = power.shappoli$suppressFor(duration, actorAndTarget.getLeft());
            suppressed = suppressed || result;
        }

        if (suppressed && bientity_action != null) {
            bientity_action.accept(actorAndTarget);
        }
    }

    public static ActionFactory<Pair<Entity, Entity>> getFactory() {
        return new ActionFactory<>(Shappoli.identifier("suppress_power"),
            new SerializableData()
                .add("power", ApoliDataTypes.POWER_TYPE, null)
                .add("powers", ShappoliDataTypes.POWER_TYPES, null)
                .add("duration", SerializableDataTypes.POSITIVE_INT)
                .add("bientity_action", ApoliDataTypes.BIENTITY_ACTION, null)
                .add("ignore_warning", SerializableDataTypes.BOOLEAN, false)
            ,
            SuppressPowerAction::action
        );
    }
}
