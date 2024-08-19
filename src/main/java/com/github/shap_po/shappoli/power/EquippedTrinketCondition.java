package com.github.shap_po.shappoli.power;

import com.github.shap_po.shappoli.Shappoli;
import com.github.shap_po.shappoli.data.ShappoliDataTypes;
import com.github.shap_po.shappoli.data.TrinketSlotData;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketsApi;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.calio.data.SerializableData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Pair;
import net.minecraft.world.World;

import java.util.function.Predicate;

public class EquippedTrinketCondition {
    public static boolean condition(SerializableData.Instance data, Entity entity) {
        if (!(entity instanceof LivingEntity livingEntity)) {
            return false;
        }
        var component = TrinketsApi.getTrinketComponent(livingEntity);
        if (component.isEmpty()) {
            return false;
        }

        Predicate<Pair<World, ItemStack>> itemCondition = data.get("item_condition");
        TrinketSlotData slot = data.get("slot");

        boolean isEquipped = false;
        for (Pair<SlotReference, ItemStack> trinket : component.get().getEquipped((i) -> true)) {
            if ((slot == null || slot.test(trinket.getLeft())) &&
                (itemCondition == null || itemCondition.test(new Pair<>(entity.getWorld(), trinket.getRight())))
            ) {
                isEquipped = true;
                break;
            }
        }

        return isEquipped;
    }

    public static ConditionFactory<Entity> getFactory() {
        return new ConditionFactory<>(
            Shappoli.identifier("equipped_trinket"),
            new SerializableData()
                .add("item_condition", ApoliDataTypes.ITEM_CONDITION, null)
                .add("slot", ShappoliDataTypes.TRINKET_SLOT, null),
            EquippedTrinketCondition::condition
        );
    }
}
