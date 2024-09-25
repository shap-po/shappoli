package com.github.shap_po.shappoli.integration.walkers.power.factory.condition.entity;

import com.github.shap_po.shappoli.Shappoli;
import com.github.shap_po.shappoli.integration.walkers.util.WalkersUtil;
import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.calio.data.SerializableData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import tocraft.walkers.ability.AbilityRegistry;

public class HasShapeAbilityCondition {
    public static boolean condition(SerializableData.Instance data, Entity entity) {
        if (!(entity instanceof LivingEntity livingEntity)) {
            return false;
        }
        if (entity instanceof PlayerEntity player) {
            LivingEntity shape = WalkersUtil.getShape(player);
            return AbilityRegistry.has(shape);
        }
        return AbilityRegistry.has(livingEntity);
    }

    public static ConditionFactory<Entity> getFactory() {
        return new ConditionFactory<>(
            Shappoli.identifier("has_shape_ability"),
            new SerializableData(),
            HasShapeAbilityCondition::condition
        );
    }
}
