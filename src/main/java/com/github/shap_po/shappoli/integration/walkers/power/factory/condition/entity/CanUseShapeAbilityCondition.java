package com.github.shap_po.shappoli.integration.walkers.power.factory.condition.entity;

import com.github.shap_po.shappoli.Shappoli;
import io.github.apace100.apoli.condition.factory.ConditionTypeFactory;
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

    public static ConditionTypeFactory<Entity> getFactory() {
        return new ConditionTypeFactory<>(
            Shappoli.identifier("can_use_shape_ability"),
            new SerializableData()
            ,
            CanUseShapeAbilityCondition::condition
        );
    }
}
