package com.github.shap_po.shappoli.integration.origins.action.type.bientity;

import com.github.shap_po.shappoli.Shappoli;
import com.github.shap_po.shappoli.integration.origins.action.type.ShappoliOriginsBiEntityActionTypes;
import com.github.shap_po.shappoli.integration.origins.util.OriginsUtil;
import com.github.shap_po.shappoli.util.MiscUtil;
import io.github.apace100.apoli.action.ActionConfiguration;
import io.github.apace100.apoli.action.type.BiEntityActionType;
import io.github.apace100.apoli.data.TypedDataObjectFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.apace100.origins.origin.Origin;
import io.github.apace100.origins.origin.OriginLayer;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

public class CopyOriginBiEntityActionType extends BiEntityActionType {
    public static final TypedDataObjectFactory<CopyOriginBiEntityActionType> DATA_FACTORY = TypedDataObjectFactory.simple(
        new SerializableData()
            .add("layer", SerializableDataTypes.IDENTIFIER, OriginsUtil.ORIGIN_LAYER_ID)
            .add("modify_actor", SerializableDataTypes.BOOLEAN, false)
            .add("modify_target", SerializableDataTypes.BOOLEAN, true)
            .validate(data -> MiscUtil.checkAtLeastOneFieldIsTrue(data, "modify_actor", "modify_target"))
        ,
        data -> new CopyOriginBiEntityActionType(
            data.get("layer"),
            data.getBoolean("modify_actor"),
            data.getBoolean("modify_target")
        ),
        (type, serializableData) -> serializableData.instance()
            .set("layer", type.layerId)
            .set("modify_actor", type.modifyActor)
            .set("modify_target", type.modifyTarget)
    );

    private final Identifier layerId;
    private final boolean modifyActor;
    private final boolean modifyTarget;

    public CopyOriginBiEntityActionType(Identifier layerId, boolean modifyActor, boolean modifyTarget) {
        this.layerId = layerId;
        this.modifyActor = modifyActor;
        this.modifyTarget = modifyTarget;
    }

    @Override
    public void execute(Entity actor, Entity target) {
        if (actor.getEntityWorld().isClient) {
            return;
        }

        OriginLayer layer = OriginsUtil.getLayer(layerId);

        if (layer == null) {
            Shappoli.LOGGER.warn("Tried to copy an origin to a layer that does not exist: {}", layerId);
            return;
        }

        Origin actorOrigin = OriginsUtil.getOrigin(actor, layer);
        Origin targetOrigin = OriginsUtil.getOrigin(target, layer);

        if (modifyActor) {
            OriginsUtil.setOrigin(actor, layer, targetOrigin);
        }
        if (modifyTarget) {
            OriginsUtil.setOrigin(target, layer, actorOrigin);
        }
    }

    @Override
    public @NotNull ActionConfiguration<?> getConfig() {
        return ShappoliOriginsBiEntityActionTypes.COPY_ORIGIN;
    }
}
