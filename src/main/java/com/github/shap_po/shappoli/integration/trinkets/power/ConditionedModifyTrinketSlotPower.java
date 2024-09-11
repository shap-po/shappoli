package com.github.shap_po.shappoli.integration.trinkets.power;

import com.github.shap_po.shappoli.Shappoli;
import com.github.shap_po.shappoli.integration.trinkets.data.ShappoliTrinketsDataTypes;
import com.github.shap_po.shappoli.integration.trinkets.data.SlotEntityAttributeModifier;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.factory.PowerFactories;
import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.LivingEntity;

import java.util.List;

/**
 * Based on the {@link io.github.apace100.apoli.power.ConditionedAttributePower}
 */
public class ConditionedModifyTrinketSlotPower extends ModifyTrinketSlotPower {
    private final int tickRate;

    public ConditionedModifyTrinketSlotPower(PowerType<?> type, LivingEntity entity, int tickRate) {
        super(type, entity);
        this.tickRate = tickRate;
        this.setTicking(true);
    }

    @Override
    public void onAdded() {
        if (this.isActive()) {
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

    public static PowerFactory<Power> createFactory() {
        PowerFactory<Power> factory = new PowerFactory<>(Shappoli.identifier("conditioned_modify_trinket_slots"),
            new SerializableData()
                .add("modifier", ShappoliTrinketsDataTypes.SLOT_ENTITY_ATTRIBUTE_MODIFIER, null)
                .add("modifiers", ShappoliTrinketsDataTypes.SLOT_ENTITY_ATTRIBUTE_MODIFIERS, null)
                .add("tick_rate", SerializableDataTypes.POSITIVE_INT, 20)
            ,
            data -> (type, player) -> {
                ConditionedModifyTrinketSlotPower power = new ConditionedModifyTrinketSlotPower(type, player, data.getInt("tick_rate"));
                data.ifPresent("modifier", power::addModifier);
                data.<List<SlotEntityAttributeModifier>>ifPresent("modifiers", mods -> mods.forEach(power::addModifier));
                return power;
            }
        ).allowCondition();

        PowerFactories.ALIASES.addPathAlias("conditioned_modify_trinket_slots", factory.getSerializerId().getPath());
        return factory;
    }
}
