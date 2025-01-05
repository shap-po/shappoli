package com.github.shap_po.shappoli.integration.trinkets.power.type;

import com.github.shap_po.shappoli.integration.trinkets.data.ShappoliTrinketsDataTypes;
import com.github.shap_po.shappoli.integration.trinkets.data.SlotEntityAttributeModifier;
import com.github.shap_po.shappoli.util.MiscUtil;
import io.github.apace100.apoli.condition.EntityCondition;
import io.github.apace100.apoli.data.TypedDataObjectFactory;
import io.github.apace100.apoli.power.PowerConfiguration;
import io.github.apace100.apoli.power.type.PowerType;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

/**
 * Based on the {@link io.github.apace100.apoli.power.type.ConditionedAttributePowerType}
 */
public class ConditionedModifyTrinketsSlotPowerType extends ModifyTrinketsSlotPowerType {
    public static final TypedDataObjectFactory<ConditionedModifyTrinketsSlotPowerType> DATA_FACTORY = PowerType.createConditionedDataFactory(
        new SerializableData()
            .add("modifier", ShappoliTrinketsDataTypes.SLOT_ENTITY_ATTRIBUTE_MODIFIER, null)
            .add("modifiers", ShappoliTrinketsDataTypes.SLOT_ENTITY_ATTRIBUTE_MODIFIER.list(), null)
            .add("tick_rate", SerializableDataTypes.INT, 20)
            .add("apply_on_added", SerializableDataTypes.BOOLEAN, false)
            .validate(data -> MiscUtil.checkAtLeastOneFieldExists(data, "modifier", "modifiers")),
        (data, condition) -> new ConditionedModifyTrinketsSlotPowerType(
            MiscUtil.listFromData(data, "modifier", "modifiers"),
            data.get("tick_rate"),
            data.get("apply_on_added"),
            condition
        ),
        (powerType, serializableData) -> serializableData.instance()
            .set("modifiers", powerType.modifiers)
            .set("tick_rate", powerType.tickRate)
            .set("apply_on_added", powerType.applyOnAdded)
    );

    private final int tickRate;
    private final boolean applyOnAdded;

    private Integer startTicks = null;
    private Integer endTicks = null;
    private boolean wasActive = false;

    public ConditionedModifyTrinketsSlotPowerType(List<SlotEntityAttributeModifier> modifiers, int tickRate, boolean applyOnAdded, Optional<EntityCondition> condition) {
        super(modifiers, condition);
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
    public void serverTick() {
        if (isActive()) {
            if (startTicks == null) {
                startTicks = getHolder().age % tickRate;
                endTicks = null;
            } else if (!wasActive && getHolder().age % tickRate == startTicks) {
                applyTempModifiers();
                this.wasActive = true;
            }
        } else if (wasActive) {
            if (endTicks == null) {
                startTicks = null;
                endTicks = getHolder().age % tickRate;
            } else if (getHolder().age % tickRate == endTicks) {
                removeTempModifiers();
                this.wasActive = false;
            }
        }
    }

    @Override
    public @NotNull PowerConfiguration<?> getConfig() {
        return ShappoliTrinketsPowerTypes.CONDITIONED_MODIFY_TRINKETS_SLOT;
    }
}
