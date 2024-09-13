package com.github.shap_po.shappoli.power.factory.action.bientity;

import com.github.shap_po.shappoli.Shappoli;
import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.s2c.play.PositionFlag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Pair;

import java.util.EnumSet;

public class TeleportAction {
    public static void action(SerializableData.Instance data, Pair<Entity, Entity> actorAndTarget) {
        Entity actor = actorAndTarget.getLeft();
        Entity target = actorAndTarget.getRight();
        if (actor.getEntityWorld().isClient) {
            return;
        }

        CachedPosition actorPosition = new CachedPosition(actor);
        CachedPosition targetPosition = new CachedPosition(target);
        boolean rotate = data.getBoolean("rotate");

        if (data.getBoolean("teleport_actor")) {
            targetPosition.teleport(actor, rotate);
        }
        if (data.getBoolean("teleport_target")) {
            actorPosition.teleport(target, rotate);
        }
    }

    private static class CachedPosition {
        private final ServerWorld world;
        private final double x;
        private final double y;
        private final double z;
        private final float yaw;
        private final float pitch;

        public CachedPosition(Entity entity) {
            this.world = (ServerWorld) entity.getEntityWorld();
            this.x = entity.getX();
            this.y = entity.getY();
            this.z = entity.getZ();
            this.yaw = entity.getYaw();
            this.pitch = entity.getPitch();
        }

        public void teleport(Entity entity, boolean rotate) {
            float yaw = rotate ? this.yaw : entity.getYaw();
            float pitch = rotate ? this.pitch : entity.getPitch();
            entity.teleport(this.world, this.x, this.y, this.z, EnumSet.noneOf(PositionFlag.class), yaw, pitch);
        }
    }


    public static ActionFactory<Pair<Entity, Entity>> getFactory() {
        return new ActionFactory<>(Shappoli.identifier("teleport"),
            new SerializableData()
                .add("teleport_actor", SerializableDataTypes.BOOLEAN, false)
                .add("teleport_target", SerializableDataTypes.BOOLEAN, true)
                .add("rotate", SerializableDataTypes.BOOLEAN, false)
            ,
            TeleportAction::action
        );
    }
}
