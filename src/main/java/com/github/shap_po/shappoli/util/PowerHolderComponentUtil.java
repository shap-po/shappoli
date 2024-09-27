package com.github.shap_po.shappoli.util;

import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.power.factory.PowerTypes;
import io.github.apace100.apoli.power.type.PowerType;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.stream.Stream;

public class PowerHolderComponentUtil {
    /**
     * Get all powers in the component with any of the specified types/ids.
     *
     * @param component     the component to get the powers from
     * @param ofIdentifiers list of filter identifiers (e.g. "apoli:active_self")
     * @return a stream of powers
     */
    public static Stream<PowerType> getPowerTypes(PowerHolderComponent component, List<Identifier> ofIdentifiers) {
        List<Identifier> resolvedIdentifiers = ofIdentifiers.stream()
            .map(identifier -> PowerTypes.ALIASES.resolveNamespaceAlias(identifier, false))
            .toList();

        return component
            .getPowerTypes()
            .stream()
            .filter(power -> resolvedIdentifiers.contains(PowerTypes.ALIASES.resolveAlias(getPowerId(power), resolvedIdentifiers::contains)));
    }

    public static Identifier getPowerId(PowerType powerType) {
        return powerType.getPowerId();
    }
}
