package com.github.shap_po.shappoli.power.factory.action.bientity;

import com.github.shap_po.shappoli.Shappoli;
import com.github.shap_po.shappoli.access.SuppressiblePower;
import com.github.shap_po.shappoli.data.ShappoliDataTypes;
import com.github.shap_po.shappoli.util.MiscUtil;
import com.github.shap_po.shappoli.util.PowerHolderComponentUtil;
import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.MultiplePowerType;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerTypeReference;
import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

public class SuppressPowerAction {
    public static void action(SerializableData.Instance data, Pair<Entity, Entity> actorAndTarget) {
        List<PowerTypeReference<?>> powerRefs = MiscUtil.listFromData(data, "power", "powers");
        List<Identifier> powerIds = MiscUtil.listFromData(data, "power_id", "power_ids");
        List<PowerTypeReference<?>> ignoredPowers = MiscUtil.listFromData(data, null, "ignored_powers");
        int duration = data.getInt("duration");
        Consumer<Pair<Entity, Entity>> bientity_action = data.get("bientity_action");

        PowerHolderComponent component = PowerHolderComponent.KEY.get(actorAndTarget.getRight());

        boolean suppressed = false;

        for (PowerTypeReference<?> powerRef : powerRefs) {
            boolean result = suppressPower(data, actorAndTarget, component, ignoredPowers, component.getPower(powerRef), duration);
            suppressed = suppressed || result;
        }

        for (Iterator<Power> iterator = PowerHolderComponentUtil.getPowers(component, powerIds).iterator(); iterator.hasNext(); ) {
            boolean result = suppressPower(data, actorAndTarget, component, ignoredPowers, iterator.next(), duration);
            suppressed = suppressed || result;
        }

        if (suppressed && bientity_action != null) {
            bientity_action.accept(actorAndTarget);
        }
    }

    private static <P extends Power> boolean suppressPower(
        SerializableData.Instance data, Pair<Entity, Entity> actorAndTarget, PowerHolderComponent component,
        List<PowerTypeReference<?>> ignoredPowers, @Nullable P power, int duration
    ) {
        SuppressiblePower suppressiblePower = (SuppressiblePower) power;
        if (power == null || ignoredPowers.stream().anyMatch(denied -> power.getType().equals(denied))) {
            return false;
        }

        if (!suppressiblePower.shappoli$canBeSuppressed()) {
            Shappoli.LOGGER.error("Tried to suppress a power that cannot be suppressed: {}", power.getType().getIdentifier());
            return false;
        }

        if (power.getType() instanceof MultiplePowerType<?> multiplePowerType) {
            if (!data.getBoolean("ignore_multiple_power_warning")) {
                Shappoli.LOGGER.warn(
                    "Suppressed power \"{}\" of type \"apoli:multiple\". This is generally not recommended. If you want to ignore this message, set the \"ignore_multiple_power_warning\" parameter to \"true\"",
                    power.getType().getIdentifier()
                );
            }
            // loop over all sub powers and try to suppress them
            return multiplePowerType.getSubPowers().stream()
                .<Power>map(subPowerId -> component.getPower(new PowerTypeReference<>(subPowerId)))
                .map(subPower -> suppressPower(data, actorAndTarget, component, ignoredPowers, subPower, duration))
                .reduce(false, Boolean::logicalOr);
        }

        if (!suppressiblePower.shappoli$hasConditions() && !data.getBoolean("ignore_no_condition_warning")) {
            Shappoli.LOGGER.warn(
                "Suppressed power \"{}\" of type \"{}\" that does not support conditions. This probably would not work. If you want to ignore this message, set the \"ignore_no_condition_warning\" parameter to \"true\"",
                power.getType().getIdentifier(), PowerHolderComponentUtil.getPowerId(power)
            );
        }

        return suppressiblePower.shappoli$suppressFor(duration, actorAndTarget.getLeft());
    }

    public static ActionFactory<Pair<Entity, Entity>> getFactory() {
        return new ActionFactory<>(Shappoli.identifier("suppress_power"),
            new SerializableData()
                .add("power", ApoliDataTypes.POWER_TYPE, null) // power type reference, example: my_namespace:my_power
                .add("powers", ShappoliDataTypes.POWER_TYPES, null)
                .add("power_id", SerializableDataTypes.IDENTIFIER, null) // power identifier, example: apoli:action_on_hit
                .add("power_ids", SerializableDataTypes.IDENTIFIERS, null)
                .add("ignored_powers", ShappoliDataTypes.POWER_TYPES, null) // power type references to ignore
                .add("duration", SerializableDataTypes.POSITIVE_INT)
                .add("bientity_action", ApoliDataTypes.BIENTITY_ACTION, null)

                .add("ignore_no_condition_warning", SerializableDataTypes.BOOLEAN, false)
                .add("ignore_multiple_power_warning", SerializableDataTypes.BOOLEAN, false)
            ,
            SuppressPowerAction::action
        );
    }
}
