package com.github.shap_po.shappoli.integration.trinkets.power.type;

import com.github.shap_po.shappoli.integration.trinkets.data.ShappoliTrinketsDataTypes;
import com.github.shap_po.shappoli.integration.trinkets.data.SlotEntityAttributeModifier;
import com.github.shap_po.shappoli.integration.trinkets.util.TrinketsUtil;
import com.github.shap_po.shappoli.util.MiscUtil;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import dev.emi.trinkets.api.TrinketsApi;
import io.github.apace100.apoli.condition.EntityCondition;
import io.github.apace100.apoli.data.TypedDataObjectFactory;
import io.github.apace100.apoli.power.PowerConfiguration;
import io.github.apace100.apoli.power.type.PowerType;
import io.github.apace100.calio.data.SerializableData;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

/**
 * Based on the {@link io.github.apace100.apoli.power.type.AttributePowerType}
 */
public class ModifyTrinketsSlotPowerType extends PowerType {
    public static final TypedDataObjectFactory<ModifyTrinketsSlotPowerType> DATA_FACTORY = TypedDataObjectFactory.simple(
        new SerializableData()
            .add("modifier", ShappoliTrinketsDataTypes.SLOT_ENTITY_ATTRIBUTE_MODIFIER, null)
            .add("modifiers", ShappoliTrinketsDataTypes.SLOT_ENTITY_ATTRIBUTE_MODIFIER.list(), null)
            .validate(data -> MiscUtil.checkAtLeastOneFieldExists(data, "modifier", "modifiers")),
        data -> new ModifyTrinketsSlotPowerType(MiscUtil.listFromData(data, "modifier", "modifiers")),
        (powerType, serializableData) -> serializableData.instance()
            .set("modifiers", powerType.modifiers)
    );

    protected final List<SlotEntityAttributeModifier> modifiers;
    protected final Multimap<String, EntityAttributeModifier> modifiersMap;
    private boolean applied = false;

    protected ModifyTrinketsSlotPowerType(List<SlotEntityAttributeModifier> modifiers, Optional<EntityCondition> condition) {
        super(condition);
        this.modifiers = modifiers;
        modifiersMap = HashMultimap.create();
        modifiers.forEach(mod -> modifiersMap.put(mod.getAttribute().slot, mod.getModifier()));
    }

    public ModifyTrinketsSlotPowerType(List<SlotEntityAttributeModifier> modifiers) {
        this(modifiers, Optional.empty());
    }

    @Override
    public void onAdded() {
        this.applyTempModifiers(true);
    }

    @Override
    public void onRemoved() {
        this.removeTempModifiers(true);
    }

    protected void applyTempModifiers(boolean force) {
        if (getHolder().getWorld().isClient || (applied && !force)) {
            return;
        }
        applied = true;

        TrinketsApi.getTrinketComponent(getHolder()).ifPresent(trinket -> {
            trinket.addTemporaryModifiers(modifiersMap);
            TrinketsUtil.updateInventories(trinket);
        });
    }

    protected void applyTempModifiers() {
        applyTempModifiers(false);
    }

    protected void removeTempModifiers(boolean force) {
        if (getHolder().getWorld().isClient || (!applied && !force)) {
            return;
        }
        applied = false;

        TrinketsApi.getTrinketComponent(getHolder()).ifPresent(trinket -> {
            trinket.removeModifiers(modifiersMap);
            TrinketsUtil.updateInventories(trinket);
        });
    }

    protected void removeTempModifiers() {
        removeTempModifiers(false);
    }

    @Override
    public @NotNull PowerConfiguration<?> getConfig() {
        return ShappoliTrinketsPowerTypes.MODIFY_TRINKETS_SLOT;
    }
}
