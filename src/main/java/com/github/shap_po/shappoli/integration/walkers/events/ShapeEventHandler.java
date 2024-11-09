package com.github.shap_po.shappoli.integration.walkers.events;

import com.github.shap_po.shappoli.integration.walkers.power.type.ActionOnShapeAbilityUsePowerType;
import com.github.shap_po.shappoli.integration.walkers.power.type.ActionOnShapeChangePowerType;
import com.github.shap_po.shappoli.integration.walkers.power.type.PreventShapeAbilityUsePowerType;
import com.github.shap_po.shappoli.integration.walkers.power.type.PreventShapeChangePowerType;
import io.github.apace100.apoli.component.PowerHolderComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ActionResult;
import tocraft.walkers.api.events.ShapeEvents;

public class ShapeEventHandler {
    public static void register() {
        ShapeEvents.SWAP_SHAPE.register((player, to) -> {
            LivingEntity shape = to == null ? player : to;
            if (PowerHolderComponent.hasPowerType(player, PreventShapeChangePowerType.class, p -> p.doesApply(shape))) {
                return ActionResult.FAIL;
            }

            PowerHolderComponent.withPowerTypes(player, ActionOnShapeChangePowerType.class, p -> p.doesApply(shape), p -> p.apply(shape));

            return ActionResult.PASS;
        });

        ShapeEvents.USE_SHAPE_ABILITY.register((player, ability) -> {
            if (PowerHolderComponent.hasPowerType(player, PreventShapeAbilityUsePowerType.class, PreventShapeAbilityUsePowerType::doesApply)) {
                return ActionResult.FAIL;
            }

            PowerHolderComponent.withPowerTypes(player, ActionOnShapeAbilityUsePowerType.class, ActionOnShapeAbilityUsePowerType::doesApply, ActionOnShapeAbilityUsePowerType::apply);

            return ActionResult.PASS;
        });
    }
}
