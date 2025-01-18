package com.github.shap_po.shappoli.integration.trinkets.component.item;

import com.github.shap_po.shappoli.Shappoli;
import com.github.shap_po.shappoli.integration.trinkets.util.TrinketsUtil;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.emi.trinkets.TrinketSlot;
import dev.emi.trinkets.api.SlotReference;
import io.github.apace100.apoli.Apoli;
import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerManager;
import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * {@link io.github.apace100.apoli.component.item.ItemPowersComponent} for trinkets
 */
public class TrinketItemPowersComponent {
    public static final TrinketItemPowersComponent DEFAULT = new TrinketItemPowersComponent(Set.of());

    public static final Codec<TrinketItemPowersComponent> CODEC = TrinketItemPowersComponent.Entry.SET_CODEC.xmap(
        TrinketItemPowersComponent::new,
        TrinketItemPowersComponent::entries
    );

    public static final PacketCodec<ByteBuf, TrinketItemPowersComponent> PACKET_CODEC = PacketCodecs.collection(ObjectLinkedOpenHashSet::new, TrinketItemPowersComponent.Entry.PACKET_CODEC).xmap(
        TrinketItemPowersComponent::new,
        TrinketItemPowersComponent::entries
    );

    final ObjectLinkedOpenHashSet<TrinketItemPowersComponent.Entry> entries;

    TrinketItemPowersComponent(Collection<TrinketItemPowersComponent.Entry> entries) {
        this.entries = new ObjectLinkedOpenHashSet<>(entries);
    }

    @Override
    public String toString() {
        return "TrinketItemPowersComponent{entries=" + entries + "}";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (!(obj instanceof TrinketItemPowersComponent that)) {
            return false;
        } else {
            return Objects.equals(this.entries(), that.entries());
        }
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(entries);
    }

    private ObjectLinkedOpenHashSet<TrinketItemPowersComponent.Entry> entries() {
        return entries;
    }

    public Stream<TrinketItemPowersComponent.Entry> stream() {
        return entries.stream();
    }

    public void appendTooltip(PlayerEntity player, ItemStack stack, List<Text> tooltip, TooltipType type) {
        // check if the tooltip already has a field for trinkets attributes
        boolean addedText = tooltip.stream()
            .anyMatch((t) ->
                (t instanceof MutableText mt) &&
                    (mt.getContent() instanceof TranslatableTextContent ttc) &&
                    (Objects.equals(ttc.getKey(), "trinkets.tooltip.attributes.all"))
            );

        for (TrinketItemPowersComponent.Entry entry : entries) {
            Power power = PowerManager.getNullable(entry.powerId());
            if (power == null || entry.hidden()) {
                continue;
            }

            boolean canEquip = TrinketsUtil.getSlots(player)
                .anyMatch((slotReference -> TrinketSlot.canInsert(stack, slotReference, player)));

            if (!canEquip) {
                continue;
            }

            if (!addedText) {
                tooltip.add(Text.translatable("trinkets.tooltip.attributes.all").formatted(Formatting.GRAY));
                addedText = true;
            }

            tooltip.add(Text
                .translatable("tooltip.apoli.stack_power.name", power.getName())
                .formatted(entry.negative()
                    ? Formatting.RED
                    : Formatting.YELLOW));

            if (!entry.showDescription && !type.isAdvanced()) {
                continue;
            }

            tooltip.add(Text
                .translatable("tooltip.apoli.stack_power.description", power.getDescription())
                .formatted(Formatting.GRAY));

        }
    }

    public boolean isEmpty() {
        return entries.isEmpty();
    }

    public int size() {
        return entries.size();
    }

    private static void onChange(ItemStack stack, SlotReference slot, LivingEntity entity, ChangeCallback changeCallback) {
        Identifier sourceId = Shappoli.identifier("item/" + slot.getId());

        List<Power> grantedPowers = new ObjectArrayList<>();
        TrinketItemPowersComponent currStackPowers = stack.getOrDefault(ShappoliTrinketsDataComponentTypes.TRINKET_POWERS, DEFAULT);

        for (TrinketItemPowersComponent.Entry currEntry : currStackPowers.entries) {
            PowerManager.getOptional(currEntry.powerId()).ifPresent(grantedPowers::add);
        }

        if (!grantedPowers.isEmpty()) {
            changeCallback.onChange(entity, Map.of(sourceId, grantedPowers), true);
        }
    }

    @FunctionalInterface
    interface ChangeCallback {
        void onChange(@NotNull Entity entity, Map<Identifier, Collection<Power>> powersBySource, boolean sync);
    }

    public static void onEquip(ItemStack stack, SlotReference slot, LivingEntity entity) {
        onChange(stack, slot, entity, PowerHolderComponent::grantPowers);
    }

    public static void onUnequip(ItemStack stack, SlotReference slot, LivingEntity entity) {
        onChange(stack, slot, entity, PowerHolderComponent::revokePowers);
    }

    public static boolean hasPower(ItemStack stack, Identifier powerId) {
        return stack.getOrDefault(ShappoliTrinketsDataComponentTypes.TRINKET_POWERS, TrinketItemPowersComponent.DEFAULT)
            .stream()
            .anyMatch(entry -> entry.powerId().equals(powerId));
    }

    public record Entry(Identifier powerId, boolean hidden, boolean showDescription, boolean negative) {
        public static final MapCodec<TrinketItemPowersComponent.Entry> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Identifier.CODEC.fieldOf("power").forGetter(TrinketItemPowersComponent.Entry::powerId),
            Codec.BOOL.optionalFieldOf("hidden", false).forGetter(TrinketItemPowersComponent.Entry::hidden),
            Codec.BOOL.optionalFieldOf("show_description", false).forGetter(TrinketItemPowersComponent.Entry::showDescription),
            Codec.BOOL.optionalFieldOf("negative", false).forGetter(TrinketItemPowersComponent.Entry::negative)
        ).apply(instance, TrinketItemPowersComponent.Entry::new));

        public static final PacketCodec<ByteBuf, TrinketItemPowersComponent.Entry> PACKET_CODEC = PacketCodec.tuple(
            Identifier.PACKET_CODEC, TrinketItemPowersComponent.Entry::powerId,
            PacketCodecs.BOOL, TrinketItemPowersComponent.Entry::hidden,
            PacketCodecs.BOOL, TrinketItemPowersComponent.Entry::showDescription,
            PacketCodecs.BOOL, TrinketItemPowersComponent.Entry::negative,
            TrinketItemPowersComponent.Entry::new
        );

        public static final Codec<Set<TrinketItemPowersComponent.Entry>> SET_CODEC = MAP_CODEC.codec().listOf().xmap(
            ImmutableSet::copyOf,
            ImmutableList::copyOf
        );

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            } else if (obj instanceof TrinketItemPowersComponent.Entry other) {
                return this.powerId().equals(other.powerId());
            } else {
                return false;
            }
        }

        @Override
        public int hashCode() {
            return Objects.hash(powerId);
        }
    }

    public static TrinketItemPowersComponent.Builder builder() {
        return builder(DEFAULT);
    }

    public static TrinketItemPowersComponent.Builder builder(TrinketItemPowersComponent baseItemPowers) {
        return new TrinketItemPowersComponent.Builder(baseItemPowers);
    }

    public static class Builder {
        private final ObjectLinkedOpenHashSet<TrinketItemPowersComponent.Entry> entries = new ObjectLinkedOpenHashSet<>();

        private Builder(TrinketItemPowersComponent baseItemPowers) {
            this.entries.addAll(baseItemPowers.entries);
        }

        public TrinketItemPowersComponent.Builder add(Identifier powerId, boolean hidden, boolean showDescription, boolean negative) {
            NbtCompound entryNbt = new NbtCompound();

            entryNbt.putString("power", powerId.toString());
            entryNbt.putBoolean("hidden", hidden);
            entryNbt.putBoolean("show_description", showDescription);
            entryNbt.putBoolean("negative", negative);


            TrinketItemPowersComponent.Entry.MAP_CODEC.codec().parse(NbtOps.INSTANCE, entryNbt)
                .resultOrPartial(err -> Apoli.LOGGER.warn("Cannot add element ({}) as an item power entry: {}", entryNbt, err))
                .ifPresent(entries::add);

            return this;
        }

        public TrinketItemPowersComponent.Builder remove(Identifier powerId) {
            return remove(powerId, modifierSlot -> {});
        }

        public TrinketItemPowersComponent.Builder remove(Identifier powerId, Consumer<Collection<TrinketItemPowersComponent.Entry>> removalCallback) {
            ObjectListIterator<TrinketItemPowersComponent.Entry> entryIterator = entries.iterator();
            ObjectLinkedOpenHashSet<TrinketItemPowersComponent.Entry> removedEntries = new ObjectLinkedOpenHashSet<>();

            while (entryIterator.hasNext()) {
                TrinketItemPowersComponent.Entry entry = entryIterator.next();

                if (entry.powerId().equals(powerId)) {
                    removedEntries.add(entry);
                    entryIterator.remove();
                }
            }

            if (!removedEntries.isEmpty()) {
                removalCallback.accept(removedEntries);
            }

            return this;
        }

        public TrinketItemPowersComponent.Builder remove(Predicate<TrinketItemPowersComponent.Entry> entryPredicate) {
            entries.removeIf(entryPredicate);
            return this;
        }

        public TrinketItemPowersComponent build() {
            return !entries.isEmpty()
                ? new TrinketItemPowersComponent(entries)
                : DEFAULT;
        }
    }
}
