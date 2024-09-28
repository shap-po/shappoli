package com.github.shap_po.shappoli.integration.origins.action.type.entity;

import com.github.shap_po.shappoli.Shappoli;
import com.github.shap_po.shappoli.integration.origins.util.OriginsUtil;
import io.github.apace100.apoli.action.factory.ActionTypeFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.apace100.origins.origin.Origin;
import io.github.apace100.origins.origin.OriginLayer;
import net.minecraft.entity.Entity;

public class SetOriginActionType {
    public static void action(SerializableData.Instance data, Entity entity) {
        if (entity.getEntityWorld().isClient) {
            return;
        }

        OriginLayer layer = OriginsUtil.getLayer(data.get("layer"));
        Origin origin = OriginsUtil.getOrigin(data.get("origin"));

        OriginsUtil.setOrigin(entity, layer, origin);
    }

    public static ActionTypeFactory<Entity> getFactory() {
        return new ActionTypeFactory<>(Shappoli.identifier("set_origin"),
            new SerializableData()
                .add("layer", SerializableDataTypes.IDENTIFIER, OriginsUtil.ORIGIN_LAYER_ID)
                .add("origin", SerializableDataTypes.IDENTIFIER)
            ,
            SetOriginActionType::action
        );
    }
}
