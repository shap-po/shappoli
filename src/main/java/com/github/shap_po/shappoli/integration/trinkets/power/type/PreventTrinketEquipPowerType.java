package com.github.shap_po.shappoli.integration.trinkets.power.type;

import com.github.shap_po.shappoli.integration.trinkets.data.TrinketSlotData;
import io.github.apace100.apoli.condition.EntityCondition;
import io.github.apace100.apoli.condition.ItemCondition;
import io.github.apace100.apoli.data.TypedDataObjectFactory;
import io.github.apace100.apoli.power.PowerConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class PreventTrinketEquipPowerType extends AbstractPreventTrinketChangePowerType {
    public static final TypedDataObjectFactory<PreventTrinketEquipPowerType> DATA_FACTORY = AbstractPreventTrinketChangePowerType.createDataFactory(PreventTrinketEquipPowerType::new);

    public PreventTrinketEquipPowerType(
        Optional<ItemCondition> itemCondition,
        List<TrinketSlotData> slots,
        boolean allowInCreative,
        Optional<EntityCondition> condition
    ) {
        super(itemCondition, slots, allowInCreative, condition);
    }

    @Override
    public @NotNull PowerConfiguration<?> getConfig() {
        return ShappoliTrinketsPowerTypes.PREVENT_TRINKET_EQUIP;
    }
}
