package com.github.shap_po.shappoli.action.type;

import com.github.shap_po.shappoli.action.type.item.SendActionItemActionType;
import com.github.shap_po.shappoli.action.type.meta.SendActionMetaActionType;
import io.github.apace100.apoli.action.ActionConfiguration;
import io.github.apace100.apoli.action.type.ItemActionTypes;

public class ShappoliItemActionTypes {
    public static final ActionConfiguration<SendActionItemActionType> SEND_ACTION = ItemActionTypes.register(SendActionMetaActionType.createConfiguration(SendActionItemActionType::new));

    public static void register() {
    }
}
