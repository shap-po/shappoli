package com.github.shap_po.shappoli.mixin.integration.trinkets;

import com.github.shap_po.shappoli.integration.trinkets.power.ActionOnTrinketChangePower;
import com.github.shap_po.shappoli.util.InventoryUtil;
import dev.emi.trinkets.api.SlotType;
import dev.emi.trinkets.api.TrinketInventory;
import dev.emi.trinkets.api.TrinketsApi;
import io.github.apace100.apoli.component.PowerHolderComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;

// TODO: Replace with events after Trinkets v3.10.0

/**
 * Trinket equip/unequip calls
 * Inspired by Emi's {@link dev.emi.trinkets.mixin.LivingEntityMixin}
 */
@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Unique
    private final Map<String, ItemStack> lastEquippedTrinkets = new HashMap<>();

    @Inject(at = @At("TAIL"), method = "tick")
    private void tick(CallbackInfo info) {
        LivingEntity entity = (LivingEntity) (Object) this;
        if (entity.isRemoved()) {
            return;
        }
        TrinketsApi.getTrinketComponent(entity).ifPresent(trinkets -> {
            Map<String, ItemStack> newlyEquippedTrinkets = new HashMap<>();

            trinkets.forEach((ref, stack) -> {
                TrinketInventory inventory = ref.inventory();

                SlotType slotType = inventory.getSlotType();
                int index = ref.index();

                ItemStack oldStack = getOldStack(slotType, index);
                ItemStack newStack = inventory.getStack(index);
                ItemStack newStackCopy = newStack.copy(); // old stack is removed when unequipped

                InventoryUtil.setHolder(newStack, entity); // link the stack to the player, so it can be used in powers

                if (!ItemStack.areEqual(newStack, oldStack)) {
                    // Call unequip powers on old trinket
                    PowerHolderComponent.withPower(entity, ActionOnTrinketChangePower.class,
                        p -> p.doesApply(entity, ref, oldStack),
                        p -> p.apply(entity, ref, false)
                    );
                    // Call equip powers on new trinket
                    PowerHolderComponent.withPower(entity, ActionOnTrinketChangePower.class,
                        p -> p.doesApply(entity, ref, newStack),
                        p -> p.apply(entity, ref, true)
                    );
                }

                String newRef = getSlotName(slotType, index);

                ItemStack tickedStack = inventory.getStack(index);
                // Avoid calling equip/unequip on stacks that mutate themselves
                if (tickedStack.getItem() == newStackCopy.getItem()) {
                    newlyEquippedTrinkets.put(newRef, tickedStack.copy());
                } else {
                    newlyEquippedTrinkets.put(newRef, newStackCopy);
                }
            });

            lastEquippedTrinkets.clear();
            lastEquippedTrinkets.putAll(newlyEquippedTrinkets);
        });
    }

    @Unique
    String getSlotName(SlotType slotType, int index) {
        return slotType.getGroup() + "/" + slotType.getName() + "/" + index;
    }

    @Unique
    private ItemStack getOldStack(SlotType type, int index) {
        return lastEquippedTrinkets.getOrDefault(getSlotName(type, index), ItemStack.EMPTY);
    }
}
