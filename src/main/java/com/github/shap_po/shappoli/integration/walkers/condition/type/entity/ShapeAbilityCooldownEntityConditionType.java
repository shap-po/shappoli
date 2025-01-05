package com.github.shap_po.shappoli.integration.walkers.condition.type.entity;

import com.github.shap_po.shappoli.integration.walkers.condition.type.ShappoliWalkersEntityConditionTypes;
import io.github.apace100.apoli.condition.ConditionConfiguration;
import io.github.apace100.apoli.condition.type.EntityConditionType;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.data.TypedDataObjectFactory;
import io.github.apace100.apoli.util.Comparison;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.NotNull;
import tocraft.walkers.api.PlayerAbilities;

public class ShapeAbilityCooldownEntityConditionType extends EntityConditionType {
    public static final TypedDataObjectFactory<ShapeAbilityCooldownEntityConditionType> DATA_FACTORY = TypedDataObjectFactory.simple(
        new SerializableData()
            .add("comparison", ApoliDataTypes.COMPARISON)
            .add("compare_to", SerializableDataTypes.INT),
        data -> new ShapeAbilityCooldownEntityConditionType(
            data.get("comparison"),
            data.getInt("compare_to")
        ),
        (conditionType, serializableData) -> serializableData.instance()
            .set("comparison", conditionType.comparison)
            .set("compare_to", conditionType.compareTo)
    );

    private final Comparison comparison;
    private final int compareTo;

    public ShapeAbilityCooldownEntityConditionType(Comparison comparison, int compareTo) {
        this.comparison = comparison;
        this.compareTo = compareTo;
    }

    @Override
    public boolean test(Entity entity) {
        if (!(entity instanceof PlayerEntity player)) {
            return false;
        }

        int cooldown = PlayerAbilities.getCooldown(player);

        return comparison.compare(cooldown, compareTo);
    }

    @Override
    public @NotNull ConditionConfiguration<?> getConfig() {
        return ShappoliWalkersEntityConditionTypes.SHAPE_ABILITY_COOLDOWN;
    }
}
