package com.github.shap_po.shappoli.integration.origins.action.type;

import com.github.shap_po.shappoli.Shappoli;
import com.github.shap_po.shappoli.integration.origins.action.type.bientity.CopyOriginBiEntityActionType;
import io.github.apace100.apoli.action.ActionConfiguration;
import io.github.apace100.apoli.action.type.BiEntityActionTypes;

public class ShappoliOriginsBiEntityActionTypes {
    public static final ActionConfiguration<CopyOriginBiEntityActionType> COPY_ORIGIN = BiEntityActionTypes.register(ActionConfiguration.of(Shappoli.identifier("copy_origin"), CopyOriginBiEntityActionType.DATA_FACTORY));

    public static void register() {
    }
}
