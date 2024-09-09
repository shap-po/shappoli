package com.github.shap_po.shappoli.util;

import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.factory.PowerFactories;
import net.minecraft.util.Identifier;

import java.util.stream.Stream;

public class PowerHolderComponentUtil {
    @SuppressWarnings({"unchecked"})
    public static <T extends Power> Stream<T> getPowers(PowerHolderComponent component, Identifier identifier) {
        return component
            .getPowers()
            .stream()
            .filter(power -> {
                    Identifier powerId = power.getType().getFactory().getFactory().getSerializerId();
                    return powerId.equals(identifier) || powerId.equals(PowerFactories.ALIASES.resolveAlias(powerId, identifier::equals));
                }
            )
            .map(power -> (T) power);
    }
}
