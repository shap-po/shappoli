package com.github.shap_po.shappoli.integration.trinkets.condition.type.item;

import com.github.shap_po.shappoli.integration.trinkets.condition.type.ShappoliTrinketsItemConditionTypes;
import dev.emi.trinkets.api.TrinketsApi;
import io.github.apace100.apoli.condition.ConditionConfiguration;
import io.github.apace100.apoli.condition.context.ItemConditionContext;
import io.github.apace100.apoli.condition.type.ItemConditionType;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class TrinketItemConditionType extends ItemConditionType {
    @Override
    public boolean test(ItemConditionContext context) {
        return TrinketsApi.getTrinket(context.stack().getItem()) != TrinketsApi.getDefaultTrinket();
    }

    @Override
    public @NotNull ConditionConfiguration<?> getConfig() {
        return ShappoliTrinketsItemConditionTypes.TRINKET;
    }
}
