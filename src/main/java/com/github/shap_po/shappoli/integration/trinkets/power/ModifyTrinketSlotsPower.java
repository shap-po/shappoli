package com.github.shap_po.shappoli.integration.trinkets.power;

import com.github.shap_po.shappoli.Shappoli;
import com.github.shap_po.shappoli.integration.trinkets.data.ShappoliTrinketsDataTypes;
import com.github.shap_po.shappoli.integration.trinkets.data.TrinketSlotData;
import dev.emi.trinkets.api.TrinketsApi;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.ValueModifyingPower;
import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.apoli.util.modifier.Modifier;
import io.github.apace100.calio.data.SerializableData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;

import java.util.LinkedList;
import java.util.List;

public class ModifyTrinketSlotsPower extends ValueModifyingPower {
    private final List<TrinketSlotData.SlotId> slots;
    private final List<EntityAttributeModifier> slotModifiers = new LinkedList<>();

    public ModifyTrinketSlotsPower(
        PowerType<?> type,
        LivingEntity entity,
        List<TrinketSlotData.SlotId> slots
    ) {
        super(type, entity);
        this.slots = slots;
    }

    @Override
    public void onAdded() {
        TrinketsApi.getTrinketComponent(entity).ifPresent(trinketComponent -> {
//            var g = trinketComponent.getInventory().getOrDefault(slots.group, null);
//            if (g == null) {
//                Shappoli.LOGGER.info("ModifyTrinketSlots.onAdded: group == null");
//                return;
//            }
//            var s = g.getOrDefault(slots.name, null);
//            if (s == null) {
//                Shappoli.LOGGER.info("ModifyTrinketSlots.onAdded: slot == null");
//                return;
//            }
//            s.addModifier(slotModifier);
//            Shappoli.LOGGER.info("ModifyTrinketSlots.onAdded: added slotModifier");
        });
    }

    @Override
    public void onRemoved() {
        Shappoli.LOGGER.info("ModifyTrinketSlots.onRemoved");
        TrinketsApi.getTrinketComponent(entity).ifPresent(trinketComponent -> {
//            trinketComponent.getInventory().get(slots.group).get(slots.name).removeModifier(slotModifier.getId());
        });
    }

    public static PowerFactory<Power> createFactory() {
        return new PowerFactory<>(Shappoli.identifier("modify_trinket_slots"),
            new SerializableData()
                .add("slot", ShappoliTrinketsDataTypes.TRINKET_SLOT_ID)
                .add("slots", ShappoliTrinketsDataTypes.TRINKET_SLOT_IDS)
                .add("slotModifier", Modifier.DATA_TYPE, null)
                .add("modifiers", Modifier.LIST_TYPE, null)
            ,
            data -> (type, player) -> {
                ModifyTrinketSlotsPower power = new ModifyTrinketSlotsPower(type, player, TrinketSlotData.SlotId.getSlotIds(data));
                data.ifPresent("slotModifier", power::addModifier);
                data.<List<Modifier>>ifPresent("modifiers", mods -> mods.forEach(power::addModifier));
                return power;
            }
        );
    }
}
