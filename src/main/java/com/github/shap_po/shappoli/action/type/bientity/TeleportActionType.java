package com.github.shap_po.shappoli.action.type.bientity;

import com.github.shap_po.shappoli.Shappoli;
import com.github.shap_po.shappoli.util.MiscUtil;
import io.github.apace100.apoli.action.factory.ActionTypeFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.s2c.play.PositionFlag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Pair;

import java.util.EnumSet;

public class TeleportActionType {
    public static void action(
        Entity actor, Entity target,
        boolean teleportActor,
        boolean teleportTarget,
        boolean rotate
    ) {
        if (actor.getEntityWorld().isClient) {
            return;
        }

        CachedPosition actorPosition = new CachedPosition(actor);
        CachedPosition targetPosition = new CachedPosition(target);

        if (teleportActor) {
            targetPosition.teleport(actor, rotate);
        }
        if (teleportTarget) {
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


    public static ActionTypeFactory<Pair<Entity, Entity>> getFactory() {
        return new ActionTypeFactory<>(
            Shappoli.identifier("teleport"),
            new SerializableData()
                .add("teleport_actor", SerializableDataTypes.BOOLEAN, false)
                .add("teleport_target", SerializableDataTypes.BOOLEAN, true)
                .add("rotate", SerializableDataTypes.BOOLEAN, false)
                .validate(MiscUtil::checkAtLeastOneFieldIsTrue)
            ,
            (data, actorAndTarget) -> action(
                actorAndTarget.getLeft(), actorAndTarget.getRight(),
                data.getBoolean("teleport_actor"),
                data.getBoolean("teleport_target"),
                data.getBoolean("rotate")
            )
        );
    }
}
