package com.github.shap_po.shappoli.integration.trinkets.power;

import com.github.shap_po.shappoli.Shappoli;
import com.github.shap_po.shappoli.integration.trinkets.data.ShappoliTrinketsDataTypes;
import com.github.shap_po.shappoli.integration.trinkets.data.SlotEntityAttributeModifier;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import dev.emi.trinkets.TrinketPlayerScreenHandler;
import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketInventory;
import dev.emi.trinkets.api.TrinketsApi;
import dev.emi.trinkets.payload.SyncInventoryPayload;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.factory.PowerTypeFactory;
import io.github.apace100.apoli.power.factory.PowerTypes;
import io.github.apace100.apoli.power.type.PowerType;
import io.github.apace100.calio.data.SerializableData;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.*;

/**
 * Based on the {@link io.github.apace100.apoli.power.type.AttributePowerType}
 */
public class ModifyTrinketSlotPower extends PowerType {
    protected final List<SlotEntityAttributeModifier> modifiers = new LinkedList<>();
    private boolean applied = false;

    public ModifyTrinketSlotPower(Power power, LivingEntity entity) {
        super(power, entity);
    }

    @SuppressWarnings({"UnusedReturnValue"})
    public ModifyTrinketSlotPower addModifier(SlotEntityAttributeModifier modifier) {
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
            Map<String, NbtCompound> map = new HashMap<>();
            for (TrinketInventory trinketInventory : inventoriesToSend) {
                map.put(trinketInventory.getSlotType().getId(), trinketInventory.getSyncTag());
            }
            SyncInventoryPayload packet = new SyncInventoryPayload(entity.getId(), new HashMap<>(), map);
            for (ServerPlayerEntity player : PlayerLookup.tracking(entity)) {
                ServerPlayNetworking.send(player, packet);
            }

            if (entity instanceof ServerPlayerEntity serverPlayer) {
                ServerPlayNetworking.send(serverPlayer, packet);

                if (!inventoriesToSend.isEmpty()) {
                    ((TrinketPlayerScreenHandler) serverPlayer.playerScreenHandler).trinkets$updateTrinketSlots(false);
                }
            }
            inventoriesToSend.clear();
        }
    }

    private Multimap<String, EntityAttributeModifier> getModifiersMap() {
        Multimap<String, EntityAttributeModifier> modifiersMap = HashMultimap.create();
        modifiers.forEach(mod -> {
            modifiersMap.put(mod.getAttribute().slot, mod.getModifier());
        });
        return modifiersMap;
    }

    public static PowerTypeFactory createFactory() {
        PowerTypeFactory<?> factory = new PowerTypeFactory<>(Shappoli.identifier("modify_trinket_slot"),
            new SerializableData()
                .add("modifier", ShappoliTrinketsDataTypes.SLOT_ENTITY_ATTRIBUTE_MODIFIER, null)
                .add("modifiers", ShappoliTrinketsDataTypes.SLOT_ENTITY_ATTRIBUTE_MODIFIERS, null)
            ,
            data -> (type, player) -> {
                ModifyTrinketSlotPower power = new ModifyTrinketSlotPower(type, player);
                data.ifPresent("modifier", power::addModifier);
                data.<List<SlotEntityAttributeModifier>>ifPresent("modifiers", mods -> mods.forEach(power::addModifier));
                return power;
            }
        );

        PowerTypes.ALIASES.addPathAlias("modify_trinket_slots", factory.getSerializerId().getPath());
        return factory;
    }
}
