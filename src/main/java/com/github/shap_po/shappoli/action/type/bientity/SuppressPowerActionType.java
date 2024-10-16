package com.github.shap_po.shappoli.action.type.bientity;

import com.github.shap_po.shappoli.Shappoli;
import com.github.shap_po.shappoli.access.SuppressiblePower;
import com.github.shap_po.shappoli.util.MiscUtil;
import com.google.common.collect.Streams;
import io.github.apace100.apoli.action.factory.ActionTypeFactory;
import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.MultiplePower;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerReference;
import io.github.apace100.apoli.power.factory.PowerTypeFactory;
import io.github.apace100.apoli.power.type.PowerType;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class SuppressPowerActionType {
    public static void action(
        Entity actor, Entity target,
        List<PowerReference> powerRefs, List<PowerTypeFactory> powerTypeFactories, List<Identifier> powerSources,
        List<PowerReference> ignoredPowers,
        int duration,
        @Nullable Consumer<Pair<Entity, Entity>> bientityAction,
        boolean ignoreNoConditionWarning, boolean ignoreMultiplePowerWarning

    ) {
        PowerHolderComponent component = PowerHolderComponent.KEY.get(target);
        Iterator<PowerType> powers = getPowerTypes(component, powerRefs, powerTypeFactories, powerSources).iterator();

        boolean suppressed = false;
        while (powers.hasNext()) {
            boolean result = suppressPower(
                powers.next(),
                component,
                actor,
                ignoredPowers,
                duration,
                ignoreNoConditionWarning, ignoreMultiplePowerWarning
            );
            suppressed = suppressed || result;
        }

        if (suppressed && bientityAction != null) {
            bientityAction.accept(new Pair<>(actor, target));
        }
    }

    private static Stream<PowerType> getPowerTypes(
        PowerHolderComponent component,
        List<PowerReference> powerRefs, List<PowerTypeFactory> powerTypeFactories, List<Identifier> powerSources
    ) {
        Stream<PowerType> powersFromRefs = powerRefs.stream().map(component::getPowerType);
        Stream<PowerType> powersFromTypes = component
            .getPowerTypes().stream()
            .filter(powerType -> powerTypeFactories.stream()
                .anyMatch(powerTypeFactory -> powerTypeFactory.equals(
                    powerType.getPower()
                        .getFactoryInstance()
                        .getFactory()
                ))
            );
        Stream<PowerType> powersFromSources = powerSources.stream()
            .flatMap(source -> component.getPowersFromSource(source)
                .stream()
                .filter(Predicate.not(Power::isSubPower))
            )
            .map(component::getPowerType);

        return Streams.concat(powersFromRefs, powersFromTypes, powersFromSources);
    }

    private static <P extends PowerType> boolean suppressPower(
        @Nullable P powerType,
        PowerHolderComponent component,
        Entity actor,
        List<PowerReference> ignoredPowers,
        int duration,
        boolean ignoreNoConditionWarning, boolean ignoreMultiplePowerWarning
    ) {
        SuppressiblePower suppressiblePower = (SuppressiblePower) powerType;
        if (powerType == null || ignoredPowers.stream().anyMatch(ignored -> powerType.getPower().equals(ignored))) {
            return false;
        }

        if (!suppressiblePower.shappoli$canBeSuppressed()) {
            Shappoli.LOGGER.error("Tried to suppress a power that cannot be suppressed: {}", powerType.getPowerId());
            return false;
        }

        if (powerType.getPower() instanceof MultiplePower multiplePowerType) {
            if (!ignoreMultiplePowerWarning) {
                Shappoli.LOGGER.warn(
                    "Suppressed power \"{}\" of type \"apoli:multiple\". This is generally not recommended. If you want to ignore this message, set the \"ignore_multiple_power_warning\" parameter to \"true\"",
                    powerType.getPowerId()
                );
            }
            // loop over all sub powers and try to suppress them
            return multiplePowerType
                .getSubPowers()
                .stream()
                .map(subPowerId -> component.getPowerType(new PowerReference(subPowerId.getId())))
                .map(subPower -> suppressPower(subPower, component, actor, ignoredPowers, duration, ignoreNoConditionWarning, ignoreMultiplePowerWarning))
                .reduce(false, Boolean::logicalOr);
        }

        if (!suppressiblePower.shappoli$hasConditions() && !ignoreNoConditionWarning) {
            Shappoli.LOGGER.warn(
                "Suppressed power \"{}\" of type \"{}\" that does not support ConditionTypes. This probably would not work. If you want to ignore this message, set the \"ignore_no_condition_warning\" parameter to \"true\"",
                powerType.getPowerId(),
                powerType.getPower().getFactoryInstance().getSerializerId()
            );
        }

        return suppressiblePower.shappoli$suppressFor(duration, actor);
    }

    public static ActionTypeFactory<Pair<Entity, Entity>> getFactory() {
        return new ActionTypeFactory<>(
            Shappoli.identifier("suppress_power"),
            new SerializableData()
                .add("power", ApoliDataTypes.POWER_REFERENCE, null) // power reference, example: my_namespace:my_power
                .add("powers", ApoliDataTypes.POWER_REFERENCE.list(), null)

                .add("power_type", ApoliDataTypes.POWER_TYPE_FACTORY, null) // power type, example: apoli:action_on_hit
                .add("power_types", ApoliDataTypes.POWER_TYPE_FACTORY.list(), null)

                .add("power_source", SerializableDataTypes.IDENTIFIER, null) // power source identifier, example: apoli:command
                .add("power_sources", SerializableDataTypes.IDENTIFIERS, null)

                .add("ignored_power", ApoliDataTypes.POWER_REFERENCE, null) // power reference to ignore
                .add("ignored_powers", ApoliDataTypes.POWER_REFERENCE.list(), null)

                .add("duration", SerializableDataTypes.POSITIVE_INT)
                .add("bientity_action", ApoliDataTypes.BIENTITY_ACTION, null)

                .add("ignore_no_condition_warning", SerializableDataTypes.BOOLEAN, false)
                .add("ignore_multiple_power_warning", SerializableDataTypes.BOOLEAN, false)

                .validate(data -> MiscUtil.checkAtLeastOneFieldExists(data, "power", "powers", "power_type", "power_types", "power_source", "power_sources"))
            ,
            (data, actorAndTarget) -> action(
                actorAndTarget.getLeft(), actorAndTarget.getRight(),
                MiscUtil.listFromData(data, "power", "powers"),
                MiscUtil.listFromData(data, "power_type", "power_types"),
                MiscUtil.listFromData(data, "power_source", "power_sources"),
                MiscUtil.listFromData(data, "ignored_power", "ignored_powers"),
                data.getInt("duration"),
                data.get("bientity_action"),
                data.getBoolean("ignore_no_condition_warning"),
                data.getBoolean("ignore_multiple_power_warning")
            )
        );
    }
}
