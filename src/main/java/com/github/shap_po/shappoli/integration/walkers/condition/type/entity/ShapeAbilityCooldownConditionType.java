package com.github.shap_po.shappoli.integration.walkers.condition.type.entity;

import com.github.shap_po.shappoli.Shappoli;
import io.github.apace100.apoli.condition.factory.ConditionTypeFactory;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.util.Comparison;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import tocraft.walkers.api.PlayerAbilities;

public class ShapeAbilityCooldownConditionType {
    public static boolean condition(
        Entity entity,
        Comparison comparison, int compareTo
    ) {
        if (!(entity instanceof PlayerEntity player)) {
            return false;
        }

        int cooldown = PlayerAbilities.getCooldown(player);

        return comparison.compare(cooldown, compareTo);
    }

    public static ConditionTypeFactory<Entity> getFactory() {
        return new ConditionTypeFactory<>(
            Shappoli.identifier("shape_ability_cooldown"),
            new SerializableData()
                .add("comparison", ApoliDataTypes.COMPARISON)
                .add("compare_to", SerializableDataTypes.INT)
            ,
            (data, entity) -> condition(
                entity,
                data.get("comparison"), data.getInt("compare_to")
            )
        );
    }
}
