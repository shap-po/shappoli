package com.github.shap_po.shappoli.integration.walkers.power.factory.condition.entity;

import com.github.shap_po.shappoli.Shappoli;
import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.calio.data.SerializableData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import tocraft.walkers.api.PlayerAbilities;

public class CanUseShapeAbilityCondition {
    public static boolean condition(SerializableData.Instance data, Entity entity) {
        if (!(entity instanceof PlayerEntity player)) {
            return false;
        }
        return PlayerAbilities.canUseAbility(player);
    }

    public static ConditionFactory<Entity> getFactory() {
        return new ConditionFactory<>(
            Shappoli.identifier("can_use_shape_ability"),
            new SerializableData()
            ,
            CanUseShapeAbilityCondition::condition
        );
    }
}
