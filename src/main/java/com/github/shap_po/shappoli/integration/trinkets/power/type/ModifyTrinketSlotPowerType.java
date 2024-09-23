package com.github.shap_po.shappoli.integration.trinkets.power.type;

import com.github.shap_po.shappoli.Shappoli;
import com.github.shap_po.shappoli.integration.trinkets.data.ShappoliTrinketsDataTypes;
import com.github.shap_po.shappoli.integration.trinkets.data.SlotEntityAttributeModifier;
import com.github.shap_po.shappoli.integration.trinkets.util.TrinketsUtil;
import com.github.shap_po.shappoli.util.MiscUtil;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import dev.emi.trinkets.api.TrinketsApi;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.factory.PowerTypeFactory;
import io.github.apace100.apoli.power.factory.PowerTypes;
import io.github.apace100.apoli.power.type.PowerType;
import io.github.apace100.calio.data.SerializableData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;

import java.util.LinkedList;
import java.util.List;

/**
 * Based on the {@link io.github.apace100.apoli.power.type.AttributePowerType}
 */
public class ModifyTrinketSlotPowerType extends PowerType {
    protected final List<SlotEntityAttributeModifier> modifiers = new LinkedList<>();
    private boolean applied = false;

    public ModifyTrinketSlotPowerType(Power power, LivingEntity entity) {
        super(power, entity);
    }

    @SuppressWarnings({"UnusedReturnValue"})
    public ModifyTrinketSlotPowerType addModifier(SlotEntityAttributeModifier modifier) {
        this.modifiers.add(modifier);
        return this;
    }

    @Override
    public void onAdded() {
        this.applyTempMods(true);
    }

    @Override
    public void onRemoved() {
        this.removeTempMods(true);
    }

    protected void applyTempMods(boolean force) {
        if (entity.getWorld().isClient || (applied && !force)) {
            return;
        }
        applied = true;

        TrinketsApi.getTrinketComponent(entity).ifPresent(trinket -> {
            trinket.addTemporaryModifiers(getModifiersMap());
            TrinketsUtil.updateInventories(trinket);
        });
    }

    protected void applyTempMods() {
        applyTempMods(false);
    }

    protected void removeTempMods(boolean force) {
        if (entity.getWorld().isClient || (!applied && !force)) {
            return;
        }
        applied = false;

        TrinketsApi.getTrinketComponent(entity).ifPresent(trinket -> {
            trinket.removeModifiers(getModifiersMap());
            TrinketsUtil.updateInventories(trinket);
        });
    }

    protected void removeTempMods() {
        removeTempMods(false);
    }


    private Multimap<String, EntityAttributeModifier> getModifiersMap() {
        Multimap<String, EntityAttributeModifier> modifiersMap = HashMultimap.create();
        modifiers.forEach(mod -> modifiersMap.put(mod.getAttribute().slot, mod.getModifier()));
        return modifiersMap;
    }

    public static PowerTypeFactory getFactory() {
        PowerTypeFactory<?> factory = new PowerTypeFactory<>(
            Shappoli.identifier("modify_trinket_slot"),
            new SerializableData()
                .add("modifier", ShappoliTrinketsDataTypes.SLOT_ENTITY_ATTRIBUTE_MODIFIER, null)
                .add("modifiers", ShappoliTrinketsDataTypes.SLOT_ENTITY_ATTRIBUTE_MODIFIERS, null)
                .postProcessor(data -> MiscUtil.checkHasAtLeastOneField(data, "modifier", "modifiers"))
            ,
            data -> (type, player) -> {
                ModifyTrinketSlotPowerType power = new ModifyTrinketSlotPowerType(type, player);
                data.ifPresent("modifier", power::addModifier);
                data.<List<SlotEntityAttributeModifier>>ifPresent("modifiers", mods -> mods.forEach(power::addModifier));
                return power;
            }
        );

        PowerTypes.ALIASES.addPathAlias("modify_trinket_slots", factory.getSerializerId().getPath());
        return factory;
    }
}
