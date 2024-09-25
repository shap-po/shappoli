package com.github.shap_po.shappoli.integration.origins.power.factory.action.entity;

import com.github.shap_po.shappoli.Shappoli;
import com.github.shap_po.shappoli.integration.origins.util.OriginsUtil;
import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.apace100.origins.origin.Origin;
import io.github.apace100.origins.origin.OriginLayer;
import net.minecraft.entity.Entity;

public class SetOriginAction {
    public static void action(SerializableData.Instance data, Entity entity) {
        if (entity.getEntityWorld().isClient) {
            return;
        }

        OriginLayer layer = OriginsUtil.getLayer(data.get("layer"));
        Origin origin = OriginsUtil.getOrigin(data.get("origin"));

        OriginsUtil.setOrigin(entity, layer, origin);
    }

    public static ActionFactory<Entity> getFactory() {
        return new ActionFactory<>(Shappoli.identifier("set_origin"),
            new SerializableData()
                .add("layer", SerializableDataTypes.IDENTIFIER, OriginsUtil.ORIGIN_LAYER_ID)
                .add("origin", SerializableDataTypes.IDENTIFIER)
            ,
            SetOriginAction::action
        );
    }
}
