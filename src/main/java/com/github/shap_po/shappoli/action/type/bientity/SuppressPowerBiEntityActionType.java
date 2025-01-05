package com.github.shap_po.shappoli.action.type.bientity;

import com.github.shap_po.shappoli.Shappoli;
import com.github.shap_po.shappoli.access.SuppressiblePower;
import com.github.shap_po.shappoli.action.type.ShappoliBiEntityActionTypes;
import com.github.shap_po.shappoli.util.MiscUtil;
import com.google.common.collect.Streams;
import io.github.apace100.apoli.action.ActionConfiguration;
import io.github.apace100.apoli.action.BiEntityAction;
import io.github.apace100.apoli.action.type.BiEntityActionType;
import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.data.TypedDataObjectFactory;
import io.github.apace100.apoli.power.MultiplePower;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerConfiguration;
import io.github.apace100.apoli.power.PowerReference;
import io.github.apace100.apoli.power.type.PowerType;
import io.github.apace100.apoli.power.type.PowerTypes;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class SuppressPowerBiEntityActionType extends BiEntityActionType {
    public static final TypedDataObjectFactory<SuppressPowerBiEntityActionType> DATA_FACTORY = TypedDataObjectFactory.simple(
        new SerializableData()
            .add("power", ApoliDataTypes.POWER_REFERENCE, null) // power reference, example: my_namespace:my_power
            .add("powers", ApoliDataTypes.POWER_REFERENCE.list(), null)

            .add("power_type", PowerTypes.DATA_TYPE, null) // power type, example: apoli:action_on_hit
            .add("power_types", PowerTypes.DATA_TYPE.list(), null)

            .add("power_source", SerializableDataTypes.IDENTIFIER, null) // power source identifier, example: apoli:command
            .add("power_sources", SerializableDataTypes.IDENTIFIERS, null)

            .add("ignored_power", ApoliDataTypes.POWER_REFERENCE, null) // power reference to ignore
            .add("ignored_powers", ApoliDataTypes.POWER_REFERENCE.list(), null)

            .add("duration", SerializableDataTypes.POSITIVE_INT)
            .add("bientity_action", BiEntityAction.DATA_TYPE.optional(), Optional.empty())

            .add("ignore_no_condition_warning", SerializableDataTypes.BOOLEAN, false)
            .add("ignore_multiple_power_warning", SerializableDataTypes.BOOLEAN, false)

            .validate(data -> MiscUtil.checkAtLeastOneFieldExists(data, "power", "powers", "power_type", "power_types", "power_source", "power_sources")),
        data -> new SuppressPowerBiEntityActionType(
            MiscUtil.listFromData(data, "power", "powers"),
            MiscUtil.listFromData(data, "power_type", "power_types"),
            MiscUtil.listFromData(data, "power_source", "power_sources"),
            MiscUtil.listFromData(data, "ignored_power", "ignored_powers"),
            data.getInt("duration"),
            data.get("bientity_action"),
            data.getBoolean("ignore_no_condition_warning"),
            data.getBoolean("ignore_multiple_power_warning")
        ),
        (actionType, serializableData) -> serializableData.instance()
            .set("powers", actionType.powerRefs)
            .set("power_types", actionType.powerConfigurations)
            .set("power_sources", actionType.powerSources)
            .set("ignored_powers", actionType.ignoredPowers)
            .set("duration", actionType.duration)
            .set("bientity_action", actionType.biEntityAction)
            .set("ignore_no_condition_warning", actionType.ignoreNoConditionWarning)
            .set("ignore_multiple_power_warning", actionType.ignoreMultiplePowerWarning)
    );

    private final List<PowerReference> powerRefs;
    private final List<PowerConfiguration<PowerType>> powerConfigurations;
    private final List<Identifier> powerSources;
    private final List<PowerReference> ignoredPowers;
    private final int duration;
    private final Optional<BiEntityAction> biEntityAction;
    private final boolean ignoreNoConditionWarning;
    private final boolean ignoreMultiplePowerWarning;

    public SuppressPowerBiEntityActionType(
        List<PowerReference> powerRefs, List<PowerConfiguration<PowerType>> powerConfigurations, List<Identifier> powerSources,
        List<PowerReference> ignoredPowers,
        int duration,
        Optional<BiEntityAction> biEntityAction,
        boolean ignoreNoConditionWarning, boolean ignoreMultiplePowerWarning
    ) {
        this.powerRefs = powerRefs;
        this.powerConfigurations = powerConfigurations;
        this.powerSources = powerSources;
        this.ignoredPowers = ignoredPowers;
        this.duration = duration;
        this.biEntityAction = biEntityAction;
        this.ignoreNoConditionWarning = ignoreNoConditionWarning;
        this.ignoreMultiplePowerWarning = ignoreMultiplePowerWarning;
    }

    @Override
    public void execute(Entity actor, Entity target) {
        Iterator<PowerType> powers = getPowerTypes(target).iterator();

        boolean suppressed = false;
        while (powers.hasNext()) {
            boolean result = suppressPower(powers.next(), actor);
            suppressed = suppressed || result;
        }

        if (suppressed) {
            biEntityAction.ifPresent(action -> action.execute(actor, target));
        }
    }

    @Override
    public @NotNull ActionConfiguration<?> getConfig() {
        return ShappoliBiEntityActionTypes.SUPPRESS_POWER;
    }

    private Stream<PowerType> getPowerTypes(Entity target) {
        PowerHolderComponent component = PowerHolderComponent.getNullable(target);
        if (component == null) {
            return Stream.empty();
        }

        Stream<PowerType> powersFromRefs = powerRefs.stream().map(pr -> pr.getNullablePowerType(target));
        Stream<PowerType> powersFromTypes = component
            .getPowerTypes().stream()
            .filter(powerType -> powerConfigurations.stream()
                .anyMatch(powerConfiguration -> powerConfiguration.equals(powerType.getConfig()))
            );
        Stream<PowerType> powersFromSources = powerSources.stream()
            .flatMap(source -> component.getPowersFromSource(source)
                .stream()
                .filter(Predicate.not(Power::isSubPower))
            )
            .map(component::getPowerType);

        return Streams.concat(powersFromRefs, powersFromTypes, powersFromSources);
    }

    private <P extends PowerType> boolean suppressPower(@Nullable P powerType, Entity actor) {
        SuppressiblePower suppressiblePower = (SuppressiblePower) powerType;
        if (powerType == null || ignoredPowers.stream().anyMatch(ignored -> powerType.getPower().asReference().equals(ignored))) {
            return false;
        }

        if (!suppressiblePower.shappoli$canBeSuppressed()) {
            Shappoli.LOGGER.error("Tried to suppress a power that cannot be suppressed: {}", powerType.getConfig().id());
            return false;
        }

        if (powerType.getPower() instanceof MultiplePower multiplePowerType) {
            if (!ignoreMultiplePowerWarning) {
                Shappoli.LOGGER.warn(
                    "Suppressed power \"{}\" of type \"apoli:multiple\". This is generally not recommended. If you want to ignore this message, set the \"ignore_multiple_power_warning\" parameter to \"true\"",
                    powerType.getConfig().id()
                );
            }
            // loop over all sub powers and try to suppress them
            return multiplePowerType
                .getSubPowers()
                .stream()
                .map(Power::getPowerType)
                .map(subPower -> suppressPower(subPower, actor))
                .reduce(false, Boolean::logicalOr);
        }

        if (!SuppressiblePower.hasCondition(powerType) && !ignoreNoConditionWarning) {
            Shappoli.LOGGER.warn(
                "Suppressed power \"{}\" of type \"{}\" that does not support ConditionTypes. This probably would not work. If you want to ignore this message, set the \"ignore_no_condition_warning\" parameter to \"true\"",
                powerType.getConfig().id(),
                powerType.getPower().getId()
            );
        }

        return suppressiblePower.shappoli$suppressFor(duration, actor);
    }
}
