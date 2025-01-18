package com.github.shap_po.shappoli.integration.trinkets.component.item;

import com.github.shap_po.shappoli.Shappoli;
import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ShappoliTrinketsDataComponentTypes {
    public static final ComponentType<TrinketItemPowersComponent> TRINKET_POWERS = ComponentType.<TrinketItemPowersComponent>builder()
        .codec(TrinketItemPowersComponent.CODEC)
        .packetCodec(TrinketItemPowersComponent.PACKET_CODEC)
        .build();

    public static void register() {
        Registry.register(Registries.DATA_COMPONENT_TYPE, Shappoli.identifier("trinket_powers"), TRINKET_POWERS);
    }
}
