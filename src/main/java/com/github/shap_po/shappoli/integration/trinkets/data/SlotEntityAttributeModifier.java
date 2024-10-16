package com.github.shap_po.shappoli.integration.trinkets.data;

import com.github.shap_po.shappoli.mixin.integration.trinkets.SlotEntityAttributeAccessor;
import dev.emi.trinkets.api.SlotAttributes;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.attribute.EntityAttributeModifier;

public class SlotEntityAttributeModifier {
    public static final SerializableData DATA = new SerializableData()
        .add("slot", SerializableDataTypes.STRING)
        .add("id", SerializableDataTypes.IDENTIFIER)
        .add("amount", SerializableDataTypes.DOUBLE)
        .add("operation", SerializableDataTypes.MODIFIER_OPERATION);

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
            data.getId("id"),
            data.getDouble("amount"),
            data.get("operation")
        );
        return new SlotEntityAttributeModifier(attribute, modifier);
    }

    public static SerializableData.Instance toData(SlotEntityAttributeModifier instance,SerializableData data) {
        SerializableData.Instance dataInstance = data.new Instance();
        dataInstance.set("slot", instance.attribute.slot);
        dataInstance.set("id", instance.getModifier().id());
        dataInstance.set("amount", instance.getModifier().value());
        dataInstance.set("operation", instance.getModifier().operation());
        return dataInstance;
    }
}
