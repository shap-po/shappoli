package com.github.shap_po.shappoli.integration.trinkets.data;

import com.github.shap_po.shappoli.mixin.integration.trinkets.SlotEntityAttributeAccessor;
import dev.emi.trinkets.api.SlotAttributes;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.apace100.calio.mixin.EntityAttributeModifierAccessor;
import net.minecraft.entity.attribute.EntityAttributeModifier;

public class SlotEntityAttributeModifier {
    public static final SerializableData DATA = new SerializableData()
        .add("slot", SerializableDataTypes.STRING)
        .add("operation", SerializableDataTypes.MODIFIER_OPERATION)
        .add("value", SerializableDataTypes.DOUBLE)
        .add("name", SerializableDataTypes.STRING, "Unnamed SlotEntityAttributeModifier");

    private final SlotAttributes.SlotEntityAttribute attribute;
    private final EntityAttributeModifier modifier;

    public SlotEntityAttributeModifier(SlotAttributes.SlotEntityAttribute attribute, EntityAttributeModifier modifier) {
        this.attribute = attribute;
        this.modifier = modifier;
    }

    public EntityAttributeModifier getModifier() {
        return modifier;
    }

    public SlotAttributes.SlotEntityAttribute getAttribute() {
        return attribute;
    }

    public static SlotEntityAttributeModifier fromData(SerializableData.Instance data) {
        SlotAttributes.SlotEntityAttribute attribute = SlotEntityAttributeAccessor.init(data.get("slot"));
        EntityAttributeModifier modifier = new EntityAttributeModifier(
            data.get("name"),
            data.get("value"),
            data.get("operation")
        );
        return new SlotEntityAttributeModifier(attribute, modifier);
    }

    public static SerializableData.Instance toData(SerializableData data, SlotEntityAttributeModifier instance) {
        SerializableData.Instance dataInstance = data.new Instance();
        dataInstance.set("slot", instance.attribute.slot);
        dataInstance.set("operation", instance.getModifier().getOperation());
        dataInstance.set("value", instance.getModifier().getValue());
        dataInstance.set("name", ((EntityAttributeModifierAccessor) instance.getModifier()).getName());
        return dataInstance;
    }
}
