package com.github.shap_po.shappoli.integration.trinkets.power;

import com.github.shap_po.shappoli.integration.trinkets.data.TrinketSlotData;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.factory.PowerFactory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Pair;
import net.minecraft.world.World;

import java.util.List;
import java.util.function.Predicate;

public class PreventTrinketEquipPower extends BasePreventTrinketChangePower {
    public PreventTrinketEquipPower(
        PowerType<?> type,
        LivingEntity entity,
        Predicate<Entity> entityCondition,
        Predicate<Pair<World, ItemStack>> itemCondition,
        List<TrinketSlotData> slots,
        boolean allowCreative
    ) {
        super(type, entity, entityCondition, itemCondition, slots, allowCreative);
    }

    public static PowerFactory createFactory() {
        return createFactory("prevent_trinket_equip", PreventTrinketEquipPower::new);
    }

}
