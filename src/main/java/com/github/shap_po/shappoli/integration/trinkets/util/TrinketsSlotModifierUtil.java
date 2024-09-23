package com.github.shap_po.shappoli.integration.trinkets.util;

import dev.emi.trinkets.api.TrinketInventory;
import net.minecraft.entity.attribute.EntityAttributeModifier;

import java.util.UUID;

public class TrinketsSlotModifierUtil {
    public static final String MODIFIER_PREFIX = "shappoli/slot_count_modifier/";
    public static final String MODIFIER_NAME = "Shappoli slot count modifier";

    /**
     * Get the UUID for the slot count modifier for the given inventory.
     * <br>
     * The id is unique to the slot type and persistent across restarts.
     */
    public static UUID getModifierId(TrinketInventory inventory) {
        return UUID.nameUUIDFromBytes((MODIFIER_PREFIX + TrinketsUtil.getSlotId(inventory.getSlotType())).getBytes());
    }

    public static void modifySlotCount(TrinketInventory inventory, int amount) {
        int oldAmount = getSlotModifierValue(inventory);
        setSlotCountModifier(inventory, amount + oldAmount);
    }

    public static void setSlotCountModifier(TrinketInventory inventory, int count) {
        UUID modifierId = getModifierId(inventory);

        // refresh the modifier if it's already there
        inventory.removeModifier(modifierId);

        inventory.addPersistentModifier(
            new EntityAttributeModifier(
                modifierId,
                MODIFIER_NAME,
                count,
                EntityAttributeModifier.Operation.ADDITION
            )
        );
        TrinketsUtil.updateInventories(inventory.getComponent());
    }

    public static int getSlotModifierValue(TrinketInventory inventory) {
        UUID modifierId = getModifierId(inventory);
        EntityAttributeModifier modifier = inventory.getModifiers().get(modifierId);
        return modifier == null ? 0 : (int) modifier.getValue();
    }

    public static void resetSlotCount(TrinketInventory inventory) {
        UUID modifierId = getModifierId(inventory);

        inventory.removeModifier(modifierId);
        TrinketsUtil.updateInventories(inventory.getComponent());
    }
}
