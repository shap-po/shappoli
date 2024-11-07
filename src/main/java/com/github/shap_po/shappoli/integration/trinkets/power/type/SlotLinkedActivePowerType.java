package com.github.shap_po.shappoli.integration.trinkets.power.type;

import com.github.shap_po.shappoli.Shappoli;
import com.github.shap_po.shappoli.integration.trinkets.data.ShappoliTrinketsDataTypes;
import com.github.shap_po.shappoli.integration.trinkets.slk.SlotLinkedKey;
import com.github.shap_po.shappoli.power.type.ActiveCooldownPowerType;
import com.github.shap_po.shappoli.util.MiscUtil;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.factory.PowerTypeFactory;
import io.github.apace100.apoli.util.HudRender;
import io.github.apace100.apoli.util.InventoryUtil;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Pair;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class SlotLinkedActivePowerType extends ActiveCooldownPowerType {
    private final @Nullable Consumer<Pair<World, StackReference>> itemAction;
    private final @Nullable Predicate<Pair<World, ItemStack>> itemCondition;
    private final Function<ItemStack, Integer> processor;
    private final int limit;
    private final List<SlotLinkedKey> slotLinkedKeys;

    public SlotLinkedActivePowerType(
        Power power, LivingEntity entity,
        int cooldownDuration,
        HudRender hudRender,

        Consumer<Entity> entityAction,
        Consumer<Pair<World, StackReference>> itemAction,
        Predicate<Pair<World, ItemStack>> itemCondition,

        Function<ItemStack, Integer> processor,
        int limit,

        List<SlotLinkedKey> slotLinkedKeys,
        boolean continuous
    ) {
        super(
            power, entity,
            cooldownDuration,
            hudRender,
            entityAction,

            // gather keys from slot linked keys
            slotLinkedKeys.stream()
                .flatMap(slotLinkedKeybinding -> slotLinkedKeybinding.getKeys().stream())
                .peek(key -> key.continuous = continuous)
                .toList(),

            true,
            continuous
        );

        this.itemAction = itemAction;
        this.itemCondition = itemCondition;
        this.processor = processor;
        this.limit = limit;
        this.slotLinkedKeys = slotLinkedKeys;
    }

    @Override
    public void onUse(Key key) {
        if (canUse()) {
            Shappoli.LOGGER.debug("Trying to use slot linked active power: {} with key {}", this.getPowerId(), key);
            use();
        }
    }

    public static PowerTypeFactory getFactory() {
        return new PowerTypeFactory<>(
            Shappoli.identifier("slot_linked_active"),
            new SerializableData()
                .add("cooldown", SerializableDataTypes.INT, 1)
                .add("hud_render", ApoliDataTypes.HUD_RENDER, HudRender.DONT_RENDER)

                .add("entity_action", ApoliDataTypes.ENTITY_ACTION, null)
                .add("item_action", ApoliDataTypes.ITEM_ACTION, null)
                .add("item_condition", ApoliDataTypes.ITEM_CONDITION, null)

                .add("process_mode", ApoliDataTypes.PROCESS_MODE, InventoryUtil.ProcessMode.STACKS)
                .add("limit", SerializableDataTypes.INT, 0)

                .add("slot_linked_key", ShappoliTrinketsDataTypes.SLOT_LINKED_KEYBINDING, null)
                .add("slot_linked_keys", ShappoliTrinketsDataTypes.SLOT_LINKED_KEYBINDINGS, null)
                .add("continuous", SerializableDataTypes.BOOLEAN, false)
            ,
            data -> (power, player) -> new SlotLinkedActivePowerType(
                power, player,
                data.getInt("cooldown"),
                data.get("hud_render"),

                data.get("entity_action"),
                data.get("item_action"),
                data.get("item_condition"),

                data.<InventoryUtil.ProcessMode>get("process_mode").getProcessor(),
                data.getInt("limit"),

                MiscUtil.listFromData(data, "slot_linked_key", "slot_linked_keys"),
                data.getBoolean("continuous")
            ))
            .allowCondition();
    }
}
