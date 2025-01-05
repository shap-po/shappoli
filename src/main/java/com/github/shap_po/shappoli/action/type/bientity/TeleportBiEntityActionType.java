package com.github.shap_po.shappoli.action.type.bientity;

import com.github.shap_po.shappoli.action.type.ShappoliBiEntityActionTypes;
import com.github.shap_po.shappoli.util.MiscUtil;
import io.github.apace100.apoli.action.ActionConfiguration;
import io.github.apace100.apoli.action.type.BiEntityActionType;
import io.github.apace100.apoli.data.TypedDataObjectFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.s2c.play.PositionFlag;
import net.minecraft.server.world.ServerWorld;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;

public class TeleportBiEntityActionType extends BiEntityActionType {
    public static final TypedDataObjectFactory<TeleportBiEntityActionType> DATA_FACTORY = TypedDataObjectFactory.simple(
        new SerializableData()
            .add("teleport_actor", SerializableDataTypes.BOOLEAN, false)
            .add("teleport_target", SerializableDataTypes.BOOLEAN, true)
            .add("rotate", SerializableDataTypes.BOOLEAN, false)
            .validate(data -> MiscUtil.checkAtLeastOneFieldIsTrue(data, "teleport_actor", "teleport_target")),
        data -> new TeleportBiEntityActionType(
            data.getBoolean("teleport_actor"),
            data.getBoolean("teleport_target"),
            data.getBoolean("rotate")
        ),
        (actionType, serializableData) -> serializableData.instance()
            .set("teleport_actor", actionType.teleportActor)
            .set("teleport_target", actionType.teleportTarget)
            .set("rotate", actionType.rotate)
    );
    private final boolean teleportActor;
    private final boolean teleportTarget;
    private final boolean rotate;

    public TeleportBiEntityActionType(boolean teleportActor, boolean teleportTarget, boolean rotate) {
        this.teleportActor = teleportActor;
        this.teleportTarget = teleportTarget;
        this.rotate = rotate;
    }

    @Override
    public void execute(Entity actor, Entity target) {
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

    @Override
    public @NotNull ActionConfiguration<?> getConfig() {
        return ShappoliBiEntityActionTypes.TELEPORT;
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
}
