package com.github.shap_po.shappoli.integration.walkers.ability.type;

import io.github.apace100.apoli.action.ActionConfiguration;
import io.github.apace100.apoli.data.TypedDataObjectFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.NotNull;
import tocraft.walkers.ability.impl.generic.ShootFireballAbility;

public class ShootFireballShapeAbilityType extends ShapeAbilityType {
    public static final TypedDataObjectFactory<ShootFireballShapeAbilityType> DATA_FACTORY = TypedDataObjectFactory.simple(
        new SerializableData()
            .add("is_large", SerializableDataTypes.BOOLEAN, false),
        data -> new ShootFireballShapeAbilityType(data.getBoolean("is_large")),
        (abilityType, serializableData) -> serializableData.instance()
            .set("is_large", abilityType.isLarge)
    );

    private final boolean isLarge;
    private final ShootFireballAbility<LivingEntity> ability;

    public ShootFireballShapeAbilityType(boolean isLarge) {
        this.isLarge = isLarge;
        this.ability = new ShootFireballAbility<>(isLarge);
    }

    @Override
    protected void execute(PlayerEntity player, LivingEntity shape) {
        ability.onUse(player, shape, player.getWorld());
    }

    @Override
    public @NotNull ActionConfiguration<?> getConfig() {
        return ShapeAbilityTypes.SHOOT_FIREBALL;
    }
}
