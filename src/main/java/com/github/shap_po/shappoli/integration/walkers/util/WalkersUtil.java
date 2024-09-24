package com.github.shap_po.shappoli.integration.walkers.util;

import com.github.shap_po.shappoli.integration.walkers.power.ActionOnMorphPower;
import io.github.apace100.apoli.component.PowerHolderComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
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
        PowerHolderComponent.withPowers(player, ActionOnMorphPower.class, p -> p.doesApply(entityCopy), p -> p.apply(entityCopy));
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
        PowerHolderComponent.withPowers(player, ActionOnMorphPower.class, p -> p.doesApply(player), p -> p.apply(player));
        return true;
    }
}
