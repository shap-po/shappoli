package com.github.shap_po.shappoli.power.factory.action.bientity;

import com.github.shap_po.shappoli.Shappoli;
import com.github.shap_po.shappoli.access.SuppressiblePower;
import com.github.shap_po.shappoli.data.ShappoliDataTypes;
import com.github.shap_po.shappoli.util.MiscUtil;
import com.github.shap_po.shappoli.util.PowerHolderComponentUtil;
import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.data.ApoliDataTypes;
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
            boolean result = suppressPower(data, actorAndTarget, ignoredPowers, component.getPower(powerRef), duration);
            suppressed = suppressed || result;
        }

        for (Identifier powerId : powerIds) {
            for (Iterator<Power> iterator = PowerHolderComponentUtil.getPowers(component, powerId).iterator(); iterator.hasNext(); ) {
                boolean result = suppressPower(data, actorAndTarget, ignoredPowers, iterator.next(), duration);
                suppressed = suppressed || result;
            }
        }

        if (suppressed && bientity_action != null) {
            bientity_action.accept(actorAndTarget);
        }
    }

    private static boolean suppressPower(
        SerializableData.Instance data, Pair<Entity, Entity> actorAndTarget, List<PowerTypeReference<?>> ignoredPowers,
        @Nullable Power power, int duration
    ) {
        SuppressiblePower suppressiblePower = (SuppressiblePower) power;
        if (power == null || ignoredPowers.stream().anyMatch(denied -> power.getType().equals(denied))) {
            return false;
        }


        if (!suppressiblePower.shappoli$canBeSuppressed()) {
            Shappoli.LOGGER.error("Tried to suppress a power that cannot be suppressed: {}", power.getType().getIdentifier());
            return false;
        }
        if (!suppressiblePower.shappoli$hasConditions() && !data.getBoolean("ignore_warning")) {
            Shappoli.LOGGER.warn("Suppressed power that does not support conditions: {}. If you want to ignore this message, set the \"ignore_warning\" parameter to true", power.getType().getIdentifier());
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
                .add("ignore_warning", SerializableDataTypes.BOOLEAN, false)
            ,
            SuppressPowerAction::action
        );
    }
}
