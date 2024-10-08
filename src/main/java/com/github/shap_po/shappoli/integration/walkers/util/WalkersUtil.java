package com.github.shap_po.shappoli.integration.walkers.util;

import com.github.shap_po.shappoli.integration.walkers.power.ActionOnShapeChangePower;
import io.github.apace100.apoli.component.PowerHolderComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import org.jetbrains.annotations.Nullable;
import tocraft.walkers.api.PlayerShape;
import tocraft.walkers.impl.PlayerDataProvider;

public class WalkersUtil {
    /**
     * Switches the player's shape to the given entity.
     * If the entity is null, the player's shape is cleared.
     *
     * @return true if the shape was successfully switched
     */
    public static boolean switchShape(ServerPlayerEntity player, @Nullable Identifier entityId, @Nullable NbtCompound nbt) {
        if (entityId == null) {
            return switchShape(player);
        }

        if (nbt == null) {
            nbt = new NbtCompound();
        }
        nbt.putString("id", entityId.toString());

        Entity loaded = EntityType.loadEntityWithPassengers(nbt, player.getServerWorld(), e -> e);
        if (!(loaded instanceof LivingEntity entityCopy)) {
            return false;
        }

        ((PlayerDataProvider) player).walkers$updateShapes(entityCopy);
        PowerHolderComponent.withPowers(player, ActionOnShapeChangePower.class, p -> p.doesApply(entityCopy), p -> p.apply(entityCopy));
        return true;
    }

    public static boolean switchShape(ServerPlayerEntity player, LivingEntity entity) {
        NbtCompound nbt = entity.writeNbt(new NbtCompound()); // copy entity's NBT
        return switchShape(player, EntityType.getId(entity.getType()), nbt);
    }

    public static boolean switchShape(ServerPlayerEntity player, LivingEntity entity, boolean ignoreNbt) {
        if (ignoreNbt) {
            return switchShape(player, EntityType.getId(entity.getType()));
        } else {
            return switchShape(player, entity);
        }
    }

    public static boolean switchShape(ServerPlayerEntity player, Identifier entityId) {
        return switchShape(player, entityId, new NbtCompound());
    }

    public static boolean switchShape(ServerPlayerEntity player) {
        ((PlayerDataProvider) player).walkers$updateShapes(null);
        PowerHolderComponent.withPowers(player, ActionOnShapeChangePower.class, p -> p.doesApply(player), p -> p.apply(player));
        return true;
    }

    /**
     * Gets the player's shape, defaulting to the player if the player is not transformed.
     */
    public static LivingEntity getShape(PlayerEntity player) {
        LivingEntity shape = PlayerShape.getCurrentShape(player);
        return shape == null ? player : shape;
    }

    /**
     * Gets the entity to use it for shape-related actions.
     * <br>
     * If the entity is a player, the player's shape is returned, otherwise the entity itself.
     */
    public static LivingEntity getEffectiveShape(LivingEntity entity) {
        if (entity instanceof PlayerEntity player) {
            return getShape(player);
        }
        return entity;
    }

    public static Pair<ServerPlayerEntity, LivingEntity> getPlayerShapePair(ServerPlayerEntity player) {
        return new Pair<>(player, getShape(player));
    }
}
