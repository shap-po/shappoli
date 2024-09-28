package com.github.shap_po.shappoli.integration.trinkets.power.type;

import com.github.shap_po.shappoli.Shappoli;
import com.github.shap_po.shappoli.integration.trinkets.data.ShappoliTrinketsDataTypes;
import com.github.shap_po.shappoli.integration.trinkets.data.SlotEntityAttributeModifier;
import com.github.shap_po.shappoli.util.MiscUtil;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.factory.PowerTypeFactory;
import io.github.apace100.apoli.power.factory.PowerTypes;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.LivingEntity;

import java.util.List;

/**
 * Based on the {@link io.github.apace100.apoli.power.type.ConditionedAttributePowerType}
 */
public class ConditionedModifyTrinketSlotPowerType extends ModifyTrinketSlotPowerType {
    private final int tickRate;
    private final boolean applyOnAdded;

    public ConditionedModifyTrinketSlotPowerType(Power power, LivingEntity entity, int tickRate, boolean applyOnAdded) {
        super(power, entity);
        this.tickRate = tickRate;
        this.setTicking(true);
        this.applyOnAdded = applyOnAdded;
    }

    @Override
    public void onAdded() {
        if (this.applyOnAdded && this.isActive()) {
            super.onAdded();
        }
    }

    @Override
    public void tick() {
        if (entity.age % tickRate != 0) {
            return;
        }

        if (this.isActive()) {
            this.applyTempMods();
        } else {
            this.removeTempMods();
        }
    }

    public static PowerTypeFactory getFactory() {
        PowerTypeFactory<?> factory = new PowerTypeFactory<>(
            Shappoli.identifier("conditioned_modify_trinket_slot"),
            new SerializableData()
                .add("modifier", ShappoliTrinketsDataTypes.SLOT_ENTITY_ATTRIBUTE_MODIFIER, null)
                .add("modifiers", ShappoliTrinketsDataTypes.SLOT_ENTITY_ATTRIBUTE_MODIFIERS, null)
                .add("tick_rate", SerializableDataTypes.POSITIVE_INT, 20)
                .add("apply_on_added", SerializableDataTypes.BOOLEAN, true)
                .postProcessor(data -> MiscUtil.checkHasAtLeastOneField(data, "modifier", "modifiers"))
            ,
            data -> (type, player) -> {
                ConditionedModifyTrinketSlotPowerType power = new ConditionedModifyTrinketSlotPowerType(type, player, data.getInt("tick_rate"), data.getBoolean("apply_on_added"));
                data.ifPresent("modifier", power::addModifier);
                data.<List<SlotEntityAttributeModifier>>ifPresent("modifiers", mods -> mods.forEach(power::addModifier));
                return power;
            }
        ).allowCondition();

        PowerTypes.ALIASES.addPathAlias("conditioned_modify_trinket_slots", factory.getSerializerId().getPath());
        return factory;
    }
}
