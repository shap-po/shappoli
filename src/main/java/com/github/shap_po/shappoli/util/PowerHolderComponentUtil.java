package com.github.shap_po.shappoli.util;

import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.factory.PowerFactories;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.stream.Stream;

public class PowerHolderComponentUtil {
    /**
     * Get all powers in the component with the specified type/id.
     *
     * @param component    the component to get the powers from
     * @param ofIdentifier filter identifier (e.g. "apoli:active_self")
     * @param <T>          the type of power
     * @return a stream of powers
     */
    @SuppressWarnings({"unchecked", "unused"})
    public static <T extends Power> Stream<T> getPowers(PowerHolderComponent component, Identifier ofIdentifier) {
        // make sure identifier doesn't use a namespace alias
        Identifier resolvedIdentifier = PowerFactories.ALIASES.resolveNamespaceAlias(ofIdentifier, false);

        return component
            .getPowers()
            .stream()
            .filter(power -> resolvedIdentifier.equals(PowerFactories.ALIASES.resolveAlias(getPowerId(power), resolvedIdentifier::equals)))
            .map(power -> (T) power);
    }

    /**
     * Get all powers in the component with any of the specified types/ids.
     *
     * @param component     the component to get the powers from
     * @param ofIdentifiers list of filter identifiers (e.g. "apoli:active_self")
     * @return a stream of powers
     */
    public static Stream<Power> getPowers(PowerHolderComponent component, List<Identifier> ofIdentifiers) {
        List<Identifier> resolvedIdentifiers = ofIdentifiers.stream()
            .map(identifier -> PowerFactories.ALIASES.resolveNamespaceAlias(identifier, false))
            .toList();

        return component
            .getPowers()
            .stream()
            .filter(power -> resolvedIdentifiers.contains(PowerFactories.ALIASES.resolveAlias(getPowerId(power), resolvedIdentifiers::contains)));
    }

    public static Identifier getPowerId(PowerType<?> powerType) {
        return powerType.getFactory().getFactory().getSerializerId();
    }

    public static Identifier getPowerId(Power power) {
        return getPowerId(power.getType());
    }
}
