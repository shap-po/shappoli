package com.github.shap_po.shappoli.integration.walkers.events;

import com.github.shap_po.shappoli.integration.walkers.power.ActionOnShapeChangePower;
import com.github.shap_po.shappoli.integration.walkers.power.PreventShapeChangePower;
import io.github.apace100.apoli.component.PowerHolderComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ActionResult;
import tocraft.walkers.api.events.ShapeEvents;

public class ShapeEventHandler {
    public static void register() {
        ShapeEvents.SWAP_SHAPE.register((player, to) -> {
            LivingEntity shape = to == null ? player : to;
            if (PowerHolderComponent.hasPower(player, PreventShapeChangePower.class, p -> p.doesApply(shape))) {
                return ActionResult.FAIL;
            }

            PowerHolderComponent.withPowers(player, ActionOnShapeChangePower.class, p -> p.doesApply(shape), p -> p.apply(shape));

            return ActionResult.PASS;
        });
    }
}
