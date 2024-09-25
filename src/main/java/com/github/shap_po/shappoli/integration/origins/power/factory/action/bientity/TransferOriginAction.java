package com.github.shap_po.shappoli.integration.origins.power.factory.action.bientity;

import com.github.shap_po.shappoli.Shappoli;
import com.github.shap_po.shappoli.integration.origins.util.OriginsUtil;
import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.apace100.origins.origin.Origin;
import io.github.apace100.origins.origin.OriginLayer;
import net.minecraft.entity.Entity;
import net.minecraft.util.Pair;

public class TransferOriginAction {
    public static void action(SerializableData.Instance data, Pair<Entity, Entity> actorAndTarget) {
        Entity actor = actorAndTarget.getLeft();
        Entity target = actorAndTarget.getRight();

        if (actor.getEntityWorld().isClient) {
            return;
        }

        OriginLayer layer = OriginsUtil.getLayer(data.get("layer"));

        Origin actorOrigin = OriginsUtil.getOrigin(actor, layer);
        Origin targetOrigin = OriginsUtil.getOrigin(target, layer);

        if (data.getBoolean("modify_actor")) {
            OriginsUtil.setOrigin(actor, layer, targetOrigin);
        }
        if (data.getBoolean("modify_target")) {
            OriginsUtil.setOrigin(target, layer, actorOrigin);
        }
    }

    public static ActionFactory<Pair<Entity, Entity>> getFactory() {
        return new ActionFactory<>(Shappoli.identifier("transfer_origin"),
            new SerializableData()
                .add("layer", SerializableDataTypes.IDENTIFIER, OriginsUtil.ORIGIN_LAYER_ID)
                .add("modify_actor", SerializableDataTypes.BOOLEAN, false)
                .add("modify_target", SerializableDataTypes.BOOLEAN, true)
            ,
            TransferOriginAction::action
        );
    }
}
