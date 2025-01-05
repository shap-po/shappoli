package com.github.shap_po.shappoli.action.type;

import com.github.shap_po.shappoli.Shappoli;
import com.github.shap_po.shappoli.action.type.entity.SelfBientityActionEntityActionType;
import com.github.shap_po.shappoli.action.type.entity.SendActionEntityActionType;
import com.github.shap_po.shappoli.action.type.meta.SendActionMetaActionType;
import io.github.apace100.apoli.action.ActionConfiguration;
import io.github.apace100.apoli.action.type.EntityActionTypes;


public class ShappoliEntityActionTypes {
    public static final ActionConfiguration<SendActionEntityActionType> SEND_ACTION = EntityActionTypes.register(SendActionMetaActionType.createConfiguration(SendActionEntityActionType::new));

    public static final ActionConfiguration<SelfBientityActionEntityActionType> SELF_BIENTITY_ACTION = EntityActionTypes.register(ActionConfiguration.of(Shappoli.identifier("self_bientity_action"), SelfBientityActionEntityActionType.DATA_FACTORY));

    public static void register() {
        EntityActionTypes.ALIASES.addPathAlias("bientity_action", SELF_BIENTITY_ACTION.id().getPath());
    }
}
