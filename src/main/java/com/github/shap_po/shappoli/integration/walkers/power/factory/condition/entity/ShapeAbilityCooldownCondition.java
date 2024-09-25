package com.github.shap_po.shappoli.integration.walkers.power.factory.condition.entity;

import com.github.shap_po.shappoli.Shappoli;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.apoli.util.Comparison;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import tocraft.walkers.api.PlayerAbilities;

public class ShapeAbilityCooldownCondition {
    public static boolean condition(SerializableData.Instance data, Entity entity) {
        if (!(entity instanceof PlayerEntity player)) {
            return false;
        }

        Comparison comparison = data.get("comparison");
        int compareTo = data.get("compare_to");
        int cooldown = PlayerAbilities.getCooldown(player);

        return comparison.compare(cooldown, compareTo);
    }

    public static ConditionFactory<Entity> getFactory() {
        return new ConditionFactory<>(
            Shappoli.identifier("shape_ability_cooldown"),
            new SerializableData()
                .add("comparison", ApoliDataTypes.COMPARISON)
                .add("compare_to", SerializableDataTypes.INT)
            ,
            ShapeAbilityCooldownCondition::condition
        );
    }
}
