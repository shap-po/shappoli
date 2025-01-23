package com.github.shap_po.shappoli.condition.type.item;

import com.github.shap_po.shappoli.condition.type.ShappoliItemConditionTypes;
import com.github.shap_po.shappoli.util.InventoryUtil;
import io.github.apace100.apoli.condition.ConditionConfiguration;
import io.github.apace100.apoli.condition.EntityCondition;
import io.github.apace100.apoli.condition.context.ItemConditionContext;
import io.github.apace100.apoli.condition.type.ItemConditionType;
import io.github.apace100.apoli.data.TypedDataObjectFactory;
import io.github.apace100.calio.data.SerializableData;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class HolderItemConditionType extends ItemConditionType {
    public static final TypedDataObjectFactory<HolderItemConditionType> DATA_FACTORY = TypedDataObjectFactory.simple(
        new SerializableData()
            .add("condition", EntityCondition.DATA_TYPE),
        data -> new HolderItemConditionType(data.get("condition")),
        (conditionType, serializableData) -> serializableData.instance()
            .set("condition", conditionType.condition)
    );
    private final EntityCondition condition;

    public HolderItemConditionType(EntityCondition condition) {
        this.condition = condition;
    }

    @Override
    public boolean test(ItemConditionContext context) {
        Entity holder = InventoryUtil.getHolder(context.stack());
        return holder != null && condition.test(holder);
    }

    @Override
    public @NotNull ConditionConfiguration<?> getConfig() {
        return ShappoliItemConditionTypes.HOLDER;
    }
}
