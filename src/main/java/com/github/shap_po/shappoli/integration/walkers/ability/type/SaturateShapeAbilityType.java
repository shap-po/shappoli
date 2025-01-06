package com.github.shap_po.shappoli.integration.walkers.ability.type;

import io.github.apace100.apoli.action.ActionConfiguration;
import io.github.apace100.apoli.data.TypedDataObjectFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.NotNull;
import tocraft.walkers.ability.impl.generic.SaturateAbility;

public class SaturateShapeAbilityType extends ShapeAbilityType {
    public static final TypedDataObjectFactory<SaturateShapeAbilityType> DATA_FACTORY = TypedDataObjectFactory.simple(
        new SerializableData()
            .add("food_level", SerializableDataTypes.POSITIVE_INT, 6)
            .add("saturation_level", SerializableDataTypes.POSITIVE_FLOAT, 0.1f),
        data -> new SaturateShapeAbilityType(
            data.getInt("food_level"),
            data.getFloat("saturation_level")
        ),
        (abilityType, serializableData) -> serializableData.instance()
            .set("food_level", abilityType.foodLevel)
            .set("saturation_level", abilityType.saturationLevel)
    );

    private final int foodLevel;
    private final float saturationLevel;
    private final SaturateAbility<LivingEntity> ability;

    public SaturateShapeAbilityType(int foodLevel, float saturationLevel) {
        this.foodLevel = foodLevel;
        this.saturationLevel = saturationLevel;
        this.ability = new SaturateAbility<>(foodLevel, saturationLevel);
    }

    @Override
    protected void execute(PlayerEntity player, LivingEntity shape) {
        ability.onUse(player, shape, player.getWorld());
    }

    @Override
    public @NotNull ActionConfiguration<?> getConfig() {
        return ShapeAbilityTypes.SATURATE;
    }
}
