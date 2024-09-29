package com.github.shap_po.shappoli.util;

import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
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
    public static Stream<Power> getPowers(PowerHolderComponent component, List<Identifier> ofIdentifiers) {
        return component
            .getPowers()
            .stream()
            .filter(power -> ofIdentifiers.contains(getPowerId(power)));
    }

    public static Identifier getPowerId(PowerType<?> powerType) {
        return powerType.getFactory().getFactory().getSerializerId();
    }

    public static Identifier getPowerId(Power power) {
        return getPowerId(power.getType());
    }
}
