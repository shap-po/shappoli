package com.github.shap_po.shappoli.integration.origins.action.type.entity;

import com.github.shap_po.shappoli.Shappoli;
import com.github.shap_po.shappoli.integration.origins.action.type.ShappoliOriginsEntityActionTypes;
import com.github.shap_po.shappoli.integration.origins.util.OriginsUtil;
import io.github.apace100.apoli.action.ActionConfiguration;
import io.github.apace100.apoli.action.type.EntityActionType;
import io.github.apace100.apoli.data.TypedDataObjectFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.apace100.origins.origin.Origin;
import io.github.apace100.origins.origin.OriginLayer;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

public class SetOriginEntityActionType extends EntityActionType {
    public static final TypedDataObjectFactory<SetOriginEntityActionType> DATA_FACTORY = TypedDataObjectFactory.simple(
        new SerializableData()
            .add("layer", SerializableDataTypes.IDENTIFIER, OriginsUtil.ORIGIN_LAYER_ID)
            .add("origin", SerializableDataTypes.IDENTIFIER),
        data -> new SetOriginEntityActionType(
            data.get("layer"),
            data.get("origin")
        ),
        (type, serializableData) -> serializableData.instance()
            .set("layer", type.layerId)
            .set("origin", type.originId)
    );

    private final Identifier layerId;
    private final Identifier originId;

    public SetOriginEntityActionType(Identifier layerId, Identifier originId) {
        this.layerId = layerId;
        this.originId = originId;
    }

    @Override
    public void execute(Entity entity) {
        if (entity.getEntityWorld().isClient) {
            return;
        }

        OriginLayer layer = OriginsUtil.getLayer(layerId);
        Origin origin = OriginsUtil.getOrigin(originId);

        if (layer == null) {
            Shappoli.LOGGER.warn("Tried to set an origin to a layer that does not exist: {}", layerId);
            return;
        }
        if (origin == null) {
            Shappoli.LOGGER.warn("Tried to set an origin to an origin that does not exist: {}", originId);
            return;
        }

        OriginsUtil.setOrigin(entity, layer, origin);
    }

    @Override
    public @NotNull ActionConfiguration<?> getConfig() {
        return ShappoliOriginsEntityActionTypes.SET_ORIGIN;
    }
}
