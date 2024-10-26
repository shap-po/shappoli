package com.github.shap_po.shappoli.integration.trinkets.event;

import com.github.shap_po.shappoli.integration.trinkets.component.item.TrinketItemPowersComponent;
import dev.emi.trinkets.api.event.TrinketEquipCallback;
import dev.emi.trinkets.api.event.TrinketUnequipCallback;

public class TrinketsEventListener {
    public static void register() {
        TrinketEquipCallback.EVENT.register(TrinketItemPowersComponent::onEquip);
        TrinketUnequipCallback.EVENT.register(TrinketItemPowersComponent::onUnequip);
    }
}
