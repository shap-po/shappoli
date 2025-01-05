package com.github.shap_po.shappoli.integration.trinkets.data;

import com.github.shap_po.shappoli.mixin.integration.trinkets.SlotEntityAttributeAccessor;
import dev.emi.trinkets.api.SlotAttributes;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataType;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.attribute.EntityAttributeModifier;

public class SlotEntityAttributeModifier {
    public static final SerializableDataType<SlotEntityAttributeModifier> DATA_TYPE = SerializableDataType.compound(
        new SerializableData()
            .add("slot", SerializableDataTypes.STRING)
            .add("id", SerializableDataTypes.IDENTIFIER)
            .add("amount", SerializableDataTypes.DOUBLE)
            .add("operation", SerializableDataTypes.MODIFIER_OPERATION),
        data -> new SlotEntityAttributeModifier(
            SlotEntityAttributeAccessor.init(data.get("slot")),
            new EntityAttributeModifier(
                data.getId("id"),
                data.getDouble("amount"),
                data.get("operation")
            )),
        (modifier, serializableData) -> serializableData.instance()
            .set("slot", modifier.attribute.slot)
            .set("id", modifier.getModifier().id())
            .set("amount", modifier.getModifier().value())
            .set("operation", modifier.getModifier().operation())
    );
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
}
