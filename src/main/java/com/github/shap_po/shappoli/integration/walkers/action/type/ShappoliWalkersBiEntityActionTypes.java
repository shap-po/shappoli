package com.github.shap_po.shappoli.integration.walkers.action.type;

import com.github.shap_po.shappoli.Shappoli;
import com.github.shap_po.shappoli.integration.walkers.action.type.bientity.SwitchShapeBiEntityActionType;
import io.github.apace100.apoli.action.ActionConfiguration;
import io.github.apace100.apoli.action.type.BiEntityActionTypes;

public class ShappoliWalkersBiEntityActionTypes {
    public static ActionConfiguration<SwitchShapeBiEntityActionType> SWITCH_SHAPE = BiEntityActionTypes.register(ActionConfiguration.of(Shappoli.identifier("switch_shape"), SwitchShapeBiEntityActionType.DATA_FACTORY));

    public static void register() {
        BiEntityActionTypes.ALIASES.addPathAlias("change_shape", SWITCH_SHAPE.id().getPath());
    }
}
