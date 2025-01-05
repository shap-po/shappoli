package com.github.shap_po.shappoli.integration.origins.action.type;

import com.github.shap_po.shappoli.Shappoli;
import com.github.shap_po.shappoli.integration.origins.action.type.entity.SetOriginEntityActionType;
import io.github.apace100.apoli.action.ActionConfiguration;
import io.github.apace100.apoli.action.type.EntityActionTypes;

public class ShappoliOriginsEntityActionTypes {
    public static final ActionConfiguration<SetOriginEntityActionType> SET_ORIGIN = EntityActionTypes.register(ActionConfiguration.of(Shappoli.identifier("set_origin"), SetOriginEntityActionType.DATA_FACTORY));

    public static void register() {
    }
}
