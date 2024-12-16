package com.github.shap_po.shappoli.integration.trinkets.util;

import dev.emi.trinkets.api.TrinketInventory;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

/**
 * Utility class that allows easily adding/removing trinket slots
 */
public class TrinketsSlotModifierUtil {
    /**
     * Add or remove trinket slots
     *
     * @param inventory  trinket inventory
     * @param modifierId attribute modifier id
     * @param value      value to add
     */
    public static void modifySlotCount(TrinketInventory inventory, Identifier modifierId, int value) {
        int oldAmount = getSlotCountModifierValue(inventory, modifierId);
        setSlotCountModifierValue(inventory, modifierId, value + oldAmount);
    }

    /**
     * Set the amount of trinket slots the modifier adds/removes
     *
     * @param inventory  trinket inventory
     * @param modifierId attribute modifier id
     * @param value      value to set
     * @throws IllegalStateException if the modifier operation is not ADD_VALUE
     */
    public static void setSlotCountModifierValue(TrinketInventory inventory, Identifier modifierId, int value) {
        EntityAttributeModifier modifier = getSlotAttributeModifier(inventory, modifierId);
        if (modifier != null && modifier.operation() != EntityAttributeModifier.Operation.ADD_VALUE) {
            throw new IllegalStateException("Cannot set modifier with operation " + modifier.operation());
        }

        // refresh the modifier if it's already there
        inventory.removeModifier(modifierId);

        inventory.addPersistentModifier(
            new EntityAttributeModifier(
                modifierId,
                value,
                EntityAttributeModifier.Operation.ADD_VALUE
            )
        );
        TrinketsUtil.updateInventories(inventory.getComponent());
    }

    /**
     * Get the amount of trinket slots the modifier adds/removes
     *
     * @param inventory  trinket inventory
     * @param modifierId attribute modifier id
     * @return amount. Defaults to 0 if the modifier doesn't exist
     */
    public static int getSlotCountModifierValue(TrinketInventory inventory, Identifier modifierId) {
        EntityAttributeModifier modifier = getSlotAttributeModifier(inventory, modifierId);
        return modifier == null ? 0 : (int) modifier.value();
    }

    public static @Nullable EntityAttributeModifier getSlotAttributeModifier(TrinketInventory inventory, Identifier modifierId) {
        return inventory.getModifiers().get(modifierId);
    }

    /**
     * Remove the modifier that changes the amount of trinket slots
     *
     * @param inventory  trinket inventory
     * @param modifierId attribute modifier id
     */
    public static void removeSlotCountModifier(TrinketInventory inventory, Identifier modifierId) {
        inventory.removeModifier(modifierId);
        TrinketsUtil.updateInventories(inventory.getComponent());
    }
}
