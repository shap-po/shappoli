package com.github.shap_po.shappoli.integration.trinkets.power;

import com.github.shap_po.shappoli.integration.trinkets.data.TrinketSlotData;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.type.PowerType;
import io.github.apace100.apoli.power.factory.PowerTypeFactory;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Pair;
import net.minecraft.world.World;

import java.util.List;
import java.util.function.Predicate;

public class PreventTrinketEquipPower extends BasePreventTrinketChangePower {
    public PreventTrinketEquipPower(
        Power power,
        LivingEntity entity,
        Predicate<Pair<World, ItemStack>> itemCondition,
        List<TrinketSlotData> slots,
        boolean allowCreative
    ) {
        super(power, entity, itemCondition, slots, allowCreative);
    }

    public static PowerTypeFactory createFactory() {
        return createFactory("prevent_trinket_equip", PreventTrinketEquipPower::new);
    }
}
