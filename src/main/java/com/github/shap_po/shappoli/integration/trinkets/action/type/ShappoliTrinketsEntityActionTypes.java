package com.github.shap_po.shappoli.integration.trinkets.action.type;

import com.github.shap_po.shappoli.Shappoli;
import com.github.shap_po.shappoli.integration.trinkets.action.type.entity.ModifyTrinketsInventoryEntityActionType;
import io.github.apace100.apoli.action.ActionConfiguration;
import io.github.apace100.apoli.action.type.EntityActionTypes;

public class ShappoliTrinketsEntityActionTypes {
    public static final ActionConfiguration<ModifyTrinketsInventoryEntityActionType> MODIFY_TRINKETS_INVENTORY = EntityActionTypes.register(ActionConfiguration.of(Shappoli.identifier("modify_trinkets_inventory"), ModifyTrinketsInventoryEntityActionType.DATA_FACTORY));

    public static void register() {
        EntityActionTypes.ALIASES.addPathAlias("modify_trinket_inventory", MODIFY_TRINKETS_INVENTORY.id().getPath());
        EntityActionTypes.ALIASES.addPathAlias("modify_trinkets", MODIFY_TRINKETS_INVENTORY.id().getPath());
    }
}
