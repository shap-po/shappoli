package com.github.shap_po.shappoli.integration.walkers.condition.type.entity;

import com.github.shap_po.shappoli.integration.walkers.condition.type.ShappoliWalkersEntityConditionTypes;
import com.github.shap_po.shappoli.integration.walkers.util.WalkersUtil;
import io.github.apace100.apoli.condition.BiEntityCondition;
import io.github.apace100.apoli.condition.ConditionConfiguration;
import io.github.apace100.apoli.condition.context.BiEntityConditionContext;
import io.github.apace100.apoli.condition.type.EntityConditionType;
import io.github.apace100.apoli.data.TypedDataObjectFactory;
import io.github.apace100.calio.data.SerializableData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.NotNull;
import tocraft.walkers.api.PlayerShape;

import java.util.Optional;

public class ShapedEntityConditionType extends EntityConditionType {
    @Override
    public boolean test(Entity entity) {
        if (!(entity instanceof PlayerEntity player)) {
            return false;
        }

        return PlayerShape.getCurrentShape(player) != null;
    }

    @Override
    public @NotNull ConditionConfiguration<?> getConfig() {
        return ShappoliWalkersEntityConditionTypes.SHAPED;
    }
}
