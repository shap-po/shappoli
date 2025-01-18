package com.github.shap_po.shappoli.integration.trinkets.condition.type.item;

import com.github.shap_po.shappoli.integration.trinkets.component.item.ShappoliTrinketsDataComponentTypes;
import com.github.shap_po.shappoli.integration.trinkets.component.item.TrinketItemPowersComponent;
import com.github.shap_po.shappoli.integration.trinkets.condition.type.ShappoliTrinketsItemConditionTypes;
import io.github.apace100.apoli.condition.ConditionConfiguration;
import io.github.apace100.apoli.condition.type.ItemConditionType;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.data.TypedDataObjectFactory;
import io.github.apace100.apoli.power.PowerReference;
import io.github.apace100.calio.data.SerializableData;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class HasTrinketPowerItemConditionType extends ItemConditionType {
    public static final TypedDataObjectFactory<HasTrinketPowerItemConditionType> DATA_FACTORY = TypedDataObjectFactory.simple(
        new SerializableData()
            .add("power", ApoliDataTypes.POWER_REFERENCE),
        data -> new HasTrinketPowerItemConditionType(data.get("power")),
        (conditionType, serializableData) -> serializableData.instance()
            .set("power", conditionType.power)
    );

    private final PowerReference power;

    public HasTrinketPowerItemConditionType(PowerReference power) {
        this.power = power;
    }

    @Override
    public boolean test(World world, ItemStack stack) {
        return stack.getOrDefault(ShappoliTrinketsDataComponentTypes.TRINKET_POWERS, TrinketItemPowersComponent.DEFAULT)
            .stream()
            .map(TrinketItemPowersComponent.Entry::powerId)
            .anyMatch(power.id()::equals);
    }

    @Override
    public @NotNull ConditionConfiguration<?> getConfig() {
        return ShappoliTrinketsItemConditionTypes.HAS_TRINKET_POWER;
    }
}
