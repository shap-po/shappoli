package com.github.shap_po.shappoli.action.type;

import com.github.shap_po.shappoli.Shappoli;
import com.github.shap_po.shappoli.action.type.bientity.SendActionBiEntityActionType;
import com.github.shap_po.shappoli.action.type.bientity.SuppressPowerBiEntityActionType;
import com.github.shap_po.shappoli.action.type.bientity.TeleportBiEntityActionType;
import com.github.shap_po.shappoli.action.type.meta.SendActionMetaActionType;
import io.github.apace100.apoli.action.ActionConfiguration;
import io.github.apace100.apoli.action.type.BiEntityActionTypes;

public class ShappoliBiEntityActionTypes {
    public static final ActionConfiguration<SendActionBiEntityActionType> SEND_ACTION = BiEntityActionTypes.register(SendActionMetaActionType.createConfiguration(SendActionBiEntityActionType::new));

    public static final ActionConfiguration<SuppressPowerBiEntityActionType> SUPPRESS_POWER = BiEntityActionTypes.register(ActionConfiguration.of(Shappoli.identifier("suppress_power"), SuppressPowerBiEntityActionType.DATA_FACTORY));
    public static final ActionConfiguration<TeleportBiEntityActionType> TELEPORT = BiEntityActionTypes.register(ActionConfiguration.of(Shappoli.identifier("teleport"), TeleportBiEntityActionType.DATA_FACTORY));

    public static void register() {
    }
}
