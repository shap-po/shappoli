package com.github.shap_po.shappoli.integration.trinkets.util;

import com.github.shap_po.shappoli.Shappoli;
import dev.emi.trinkets.api.TrinketInventory;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.util.Identifier;

public class TrinketsSlotModifierUtil {
    public static final String MODIFIER_PREFIX = "slot_count_modifier/";

    /**
     * Get the Identifier for the slot count modifier for the given inventory.
     */
    public static Identifier getModifierId(TrinketInventory inventory) {
        return Shappoli.identifier(MODIFIER_PREFIX + TrinketsUtil.getSlotId(inventory.getSlotType()));
    }

    public static void modifySlotCount(TrinketInventory inventory, int amount) {
        int oldAmount = getSlotModifierValue(inventory);
        setSlotCountModifier(inventory, amount + oldAmount);
    }

    public static void setSlotCountModifier(TrinketInventory inventory, int count) {
        Identifier modifierId = getModifierId(inventory);

        // refresh the modifier if it's already there
        inventory.removeModifier(modifierId);

        inventory.addPersistentModifier(
            new EntityAttributeModifier(
                modifierId,
                count,
                EntityAttributeModifier.Operation.ADD_VALUE
            )
        );
        TrinketsUtil.updateInventories(inventory.getComponent());
    }

    public static int getSlotModifierValue(TrinketInventory inventory) {
        Identifier modifierId = getModifierId(inventory);
        EntityAttributeModifier modifier = inventory.getModifiers().get(modifierId);
        return modifier == null ? 0 : (int) modifier.value();
    }

    public static void resetSlotCount(TrinketInventory inventory) {
        Identifier modifierId = getModifierId(inventory);

        inventory.removeModifier(modifierId);
        TrinketsUtil.updateInventories(inventory.getComponent());
    }
}
