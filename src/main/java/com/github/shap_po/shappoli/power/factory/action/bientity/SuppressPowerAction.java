package com.github.shap_po.shappoli.power.factory.action.bientity;

import com.github.shap_po.shappoli.Shappoli;
import com.github.shap_po.shappoli.access.SuppressiblePower;
import com.github.shap_po.shappoli.data.ShappoliDataTypes;
import com.github.shap_po.shappoli.util.MiscUtil;
import com.github.shap_po.shappoli.util.PowerHolderComponentUtil;
import com.google.common.collect.Streams;
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
import java.util.stream.Stream;

public class SuppressPowerAction {
    public static void action(SerializableData.Instance data, Pair<Entity, Entity> actorAndTarget) {
        List<PowerTypeReference<?>> ignoredPowers = MiscUtil.listFromData(data, "ignored_power", "ignored_powers");
        PowerHolderComponent component = PowerHolderComponent.KEY.get(actorAndTarget.getRight());

        Iterator<Power> powers = getPowers(data, component).iterator();

        boolean suppressed = false;
        while (powers.hasNext()) {
            boolean result = suppressPower(
                data, actorAndTarget, component,
                ignoredPowers, powers.next()
            );
            suppressed = suppressed || result;
        }

        Consumer<Pair<Entity, Entity>> bientity_action = data.get("bientity_action");
        if (suppressed && bientity_action != null) {
            bientity_action.accept(actorAndTarget);
        }
    }

    private static Stream<Power> getPowers(SerializableData.Instance data, PowerHolderComponent component) {
        List<PowerTypeReference<?>> powerRefs = MiscUtil.listFromData(data, "power", "powers");
        Stream<Power> powersFromRefs = powerRefs.stream().map(component::getPower);

        List<Identifier> powerIds = MiscUtil.listFromData(data, "power_type", "power_types");
        Stream<Power> powersFromIds = PowerHolderComponentUtil.getPowers(component, powerIds);

        List<Identifier> powerSources = MiscUtil.listFromData(data, "power_source", "power_sources");
        Stream<Power> powersFromSources = powerSources.stream()
            .flatMap(source -> component.getPowersFromSource(source)
                .stream()
            )
            .map(component::getPower);

        return Streams.concat(powersFromRefs, powersFromIds, powersFromSources);
    }

    private static <P extends Power> boolean suppressPower(
        SerializableData.Instance data, Pair<Entity, Entity> actorAndTarget, PowerHolderComponent component,
        List<PowerTypeReference<?>> ignoredPowers, @Nullable P power
    ) {
        SuppressiblePower suppressiblePower = (SuppressiblePower) power;
        if (power == null || ignoredPowers.stream().anyMatch(ignored -> power.getType().equals(ignored))) {
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
            return multiplePowerType
                .getSubPowers()
                .stream()
                .<Power>map(subPowerId -> component.getPower(new PowerTypeReference<>(subPowerId)))
                .map(subPower -> suppressPower(data, actorAndTarget, component, ignoredPowers, subPower))
                .reduce(false, Boolean::logicalOr);
        }

        if (!suppressiblePower.shappoli$hasConditions() && !data.getBoolean("ignore_no_condition_warning")) {
            Shappoli.LOGGER.warn(
                "Suppressed power \"{}\" of type \"{}\" that does not support conditions. This probably would not work. If you want to ignore this message, set the \"ignore_no_condition_warning\" parameter to \"true\"",
                power.getType().getIdentifier(), PowerHolderComponentUtil.getPowerId(power)
            );
        }

        return suppressiblePower.shappoli$suppressFor(data.getInt("duration"), actorAndTarget.getLeft());
    }

    public static ActionFactory<Pair<Entity, Entity>> getFactory() {
        return new ActionFactory<>(
            Shappoli.identifier("suppress_power"),
            new SerializableData()
                .add("power", ApoliDataTypes.POWER_TYPE, null) // power reference, example: my_namespace:my_power
                .add("powers", ShappoliDataTypes.POWER_TYPES, null)

                .add("power_id", SerializableDataTypes.IDENTIFIER, null) // power type, example: apoli:action_on_hit
                .add("power_ids", SerializableDataTypes.IDENTIFIERS, null)
                .addFunctionedDefault("power_type", SerializableDataTypes.IDENTIFIER, data -> data.get("power_id"))
                .addFunctionedDefault("power_types", SerializableDataTypes.IDENTIFIERS, data -> data.get("power_ids"))

                .add("power_source", SerializableDataTypes.IDENTIFIER, null) // power source identifier, example: apoli:command
                .add("power_sources", SerializableDataTypes.IDENTIFIERS, null)

                .add("ignored_power", ApoliDataTypes.POWER_TYPE, null) // power reference to ignore
                .add("ignored_powers", ShappoliDataTypes.POWER_TYPES, null)

                .add("duration", SerializableDataTypes.INT)
                .add("bientity_action", ApoliDataTypes.BIENTITY_ACTION, null)

                .add("ignore_no_condition_warning", SerializableDataTypes.BOOLEAN, false)
                .add("ignore_multiple_power_warning", SerializableDataTypes.BOOLEAN, false)
            ,
            SuppressPowerAction::action
        );
    }
}
