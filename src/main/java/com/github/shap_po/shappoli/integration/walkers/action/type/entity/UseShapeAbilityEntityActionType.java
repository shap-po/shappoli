package com.github.shap_po.shappoli.integration.walkers.action.type.entity;

import com.github.shap_po.shappoli.integration.walkers.action.type.ShappoliWalkersEntityActionTypes;
import io.github.apace100.apoli.action.ActionConfiguration;
import io.github.apace100.apoli.action.type.EntityActionType;
import io.github.apace100.apoli.data.TypedDataObjectFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import org.jetbrains.annotations.NotNull;
import tocraft.walkers.ability.AbilityRegistry;
import tocraft.walkers.ability.ShapeAbility;
import tocraft.walkers.api.PlayerAbilities;
import tocraft.walkers.api.PlayerShape;
import tocraft.walkers.api.events.ShapeEvents;

public class UseShapeAbilityEntityActionType extends EntityActionType {
    public static final TypedDataObjectFactory<UseShapeAbilityEntityActionType> DATA_FACTORY = TypedDataObjectFactory.simple(
        new SerializableData()
            .add("force", SerializableDataTypes.BOOLEAN, false)
            .add("apply_cooldown", SerializableDataTypes.BOOLEAN, true),
        data -> new UseShapeAbilityEntityActionType(
            data.getBoolean("force"),
            data.getBoolean("apply_cooldown")
        ),
        (actionType, serializableData) -> serializableData.instance()
            .set("force", actionType.force)
            .set("apply_cooldown", actionType.applyCooldown)
    );

    private final boolean force;
    private final boolean applyCooldown;

    public UseShapeAbilityEntityActionType(boolean force, boolean applyCooldown) {
        this.force = force;
        this.applyCooldown = applyCooldown;
    }

    @Override
    public void execute(Entity entity) {
        if (!(entity instanceof ServerPlayerEntity player)) {
            return;
        }

        LivingEntity shape = PlayerShape.getCurrentShape(player);
        if (shape == null || !AbilityRegistry.has(shape)) {
            return;
        }

        // Check cooldown
        if (PlayerAbilities.canUseAbility(player) || force) {
            ShapeAbility<LivingEntity> ability = AbilityRegistry.get(shape);
            if (ability == null) {
                return;
            }

            ActionResult result = ShapeEvents.USE_SHAPE_ABILITY.invoke().use(player, ability);
            // check if ability was canceled
            if (result == ActionResult.FAIL && !force) {
                return;
            }

            ability.onUse(player, shape, shape.getWorld());
            if (applyCooldown) {
                PlayerAbilities.setCooldown(player, AbilityRegistry.get(shape).getCooldown(shape));
                PlayerAbilities.sync(player);
            }
        }
    }

    @Override
    public @NotNull ActionConfiguration<?> getConfig() {
        return ShappoliWalkersEntityActionTypes.USE_SHAPE_ABILITY;
    }
}
