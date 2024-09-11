package com.github.shap_po.shappoli.integration.trinkets.power;

import com.github.shap_po.shappoli.Shappoli;
import com.github.shap_po.shappoli.integration.trinkets.data.ShappoliTrinketsDataTypes;
import com.github.shap_po.shappoli.integration.trinkets.data.SlotEntityAttributeModifier;
import com.github.shap_po.shappoli.integration.trinkets.util.TrinketsUtil;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import dev.emi.trinkets.TrinketPlayerScreenHandler;
import dev.emi.trinkets.TrinketsNetwork;
import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketInventory;
import dev.emi.trinkets.api.TrinketsApi;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.calio.data.SerializableData;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Based on the {@link io.github.apace100.apoli.power.AttributePower}
 */
public class ModifyTrinketSlotsPower extends Power {
    protected final List<SlotEntityAttributeModifier> modifiers = new LinkedList<>();
    private boolean applied = false;

    public ModifyTrinketSlotsPower(PowerType<?> type, LivingEntity entity) {
        super(type, entity);
    }

    @SuppressWarnings({"UnusedReturnValue"})
    public ModifyTrinketSlotsPower addModifier(SlotEntityAttributeModifier modifier) {
        this.modifiers.add(modifier);
        return this;
    }

    @Override
    public void onAdded() {
        this.applyTempMods(true);
    }

    @Override
    public void onRemoved() {
        this.removeTempMods(true);
    }

    protected void applyTempMods(boolean force) {
        if (entity.getWorld().isClient || (applied && !force)) {
            return;
        }
        applied = true;

        TrinketsApi.getTrinketComponent(entity).ifPresent(trinket -> {
            trinket.addTemporaryModifiers(getModifiersMap());
            updateInventories(trinket);
        });
    }

    protected void applyTempMods() {
        applyTempMods(false);
    }

    protected void removeTempMods(boolean force) {
        if (entity.getWorld().isClient || (!applied && !force)) {
            return;
        }
        applied = false;

        TrinketsApi.getTrinketComponent(entity).ifPresent(trinket -> {
            trinket.removeModifiers(getModifiersMap());
            updateInventories(trinket);
        });
    }

    protected void removeTempMods() {
        removeTempMods(false);
    }

    /**
     * Based on the ${@link dev.emi.trinkets.mixin.LivingEntityMixin#tick()} method.
     */
    private void updateInventories(TrinketComponent trinket) {
        Set<TrinketInventory> inventoriesToSend = trinket.getTrackingUpdates();
        if (!inventoriesToSend.isEmpty()) {
            PacketByteBuf buf = PacketByteBufs.create();
            buf.writeInt(entity.getId());

            NbtCompound tag = new NbtCompound();
            for (TrinketInventory trinketInventory : inventoriesToSend) {
                trinketInventory.update(); // Manually update the inventory to ensure the state is correct
                tag.put(TrinketsUtil.getSlotId(trinketInventory.getSlotType()), trinketInventory.getSyncTag());
            }
            buf.writeNbt(tag);
            buf.writeNbt(new NbtCompound()); // Empty tag for the trinket stacks

            // network handler might be null on server start
            if (entity instanceof ServerPlayerEntity serverPlayer && serverPlayer.networkHandler != null) {
                ServerPlayNetworking.send(serverPlayer, TrinketsNetwork.SYNC_INVENTORY, buf);
                ((TrinketPlayerScreenHandler) serverPlayer.playerScreenHandler).trinkets$updateTrinketSlots(false);
            }
        }
    }

    private Multimap<String, EntityAttributeModifier> getModifiersMap() {
        Multimap<String, EntityAttributeModifier> modifiersMap = HashMultimap.create();
        modifiers.forEach(mod -> {
            modifiersMap.put(mod.getAttribute().slot, mod.getModifier());
        });
        return modifiersMap;
    }

    public static PowerFactory<Power> createFactory() {
        return new PowerFactory<>(Shappoli.identifier("modify_trinket_slots"),
            new SerializableData()
                .add("modifier", ShappoliTrinketsDataTypes.SLOT_ENTITY_ATTRIBUTE_MODIFIER, null)
                .add("modifiers", ShappoliTrinketsDataTypes.SLOT_ENTITY_ATTRIBUTE_MODIFIERS, null)
            ,
            data -> (type, player) -> {
                ModifyTrinketSlotsPower power = new ModifyTrinketSlotsPower(type, player);
                data.ifPresent("modifier", power::addModifier);
                data.<List<SlotEntityAttributeModifier>>ifPresent("modifiers", mods -> mods.forEach(power::addModifier));
                return power;
            }
        );
    }
}
