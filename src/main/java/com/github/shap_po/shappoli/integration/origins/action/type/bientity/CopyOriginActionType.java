package com.github.shap_po.shappoli.integration.origins.action.type.bientity;

import com.github.shap_po.shappoli.Shappoli;
import com.github.shap_po.shappoli.integration.origins.util.OriginsUtil;
import com.github.shap_po.shappoli.util.MiscUtil;
import io.github.apace100.apoli.action.factory.ActionTypeFactory;
import io.github.apace100.apoli.action.type.BiEntityActionTypes;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.apace100.origins.origin.Origin;
import io.github.apace100.origins.origin.OriginLayer;
import net.minecraft.entity.Entity;
import net.minecraft.util.Pair;

public class CopyOriginActionType {
    public static void action(
        Entity actor, Entity target,
        OriginLayer layer,
        boolean modifyActor, boolean modifyTarget
    ) {
        if (actor.getEntityWorld().isClient) {
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

    public static ActionTypeFactory<Pair<Entity, Entity>> getFactory() {
        ActionTypeFactory<Pair<Entity, Entity>> factory = new ActionTypeFactory<>(
            Shappoli.identifier("copy_origin"),
            new SerializableData()
                .add("layer", SerializableDataTypes.IDENTIFIER, OriginsUtil.ORIGIN_LAYER_ID)
                .add("modify_actor", SerializableDataTypes.BOOLEAN, false)
                .add("modify_target", SerializableDataTypes.BOOLEAN, true)
                .validate(MiscUtil::checkAtLeastOneFieldIsTrue)
            ,
            (data, actorAndTarget) -> action(
                actorAndTarget.getLeft(), actorAndTarget.getRight(),
                OriginsUtil.getLayer(data.get("layer")),
                data.getBoolean("modify_actor"), data.getBoolean("modify_target")
            )
        );

        BiEntityActionTypes.ALIASES.addPathAlias("transfer_origin", factory.getSerializerId().getPath());
        return factory;
    }
}
