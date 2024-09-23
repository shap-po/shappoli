package com.github.shap_po.shappoli.integration.trinkets.command;

import com.github.shap_po.shappoli.command.ShappoliCommand;
import com.github.shap_po.shappoli.integration.trinkets.util.TrinketsSlotModifierUtil;
import com.github.shap_po.shappoli.integration.trinkets.util.TrinketsUtil;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
import com.mojang.brigadier.exceptions.Dynamic3CommandExceptionType;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketInventory;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Pair;

import java.util.Map;

import static com.mojang.brigadier.arguments.StringArgumentType.string;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class TrinketsCommand {
    public static final DynamicCommandExceptionType NOT_LIVING_ENTITY = new DynamicCommandExceptionType(
        (entity) -> Text.translatable("commands.shappoli.shappoli.trinkets.fail.not_living_entity", entity)
    );
    public static final DynamicCommandExceptionType NO_TRINKET_COMPONENT = new DynamicCommandExceptionType(
        (entity) -> Text.translatable("commands.shappoli.shappoli.trinkets.fail.no_trinket_component", entity)
    );
    public static final Dynamic2CommandExceptionType GROUP_NOT_FOUND = new Dynamic2CommandExceptionType(
        (entity, group) -> Text.translatable("commands.shappoli.shappoli.trinkets.fail.no_group", entity, group)
    );
    public static final Dynamic3CommandExceptionType SLOT_NOT_FOUND = new Dynamic3CommandExceptionType(
        (entity, group, slot) -> Text.translatable("commands.shappoli.shappoli.trinkets.fail.no_slot", entity, group, slot)
    );

    public static void register() {
        ShappoliCommand.registerSubCommand(
            literal("trinkets")
                .then(literal("slots")
                    .requires(source -> source.hasPermissionLevel(2))
                    .then(argument("entity", EntityArgumentType.entity())
                        .then(literal("add")
                            .then(argument("group", string())
                                .then(argument("slot", string())
                                    .then(argument("amount", IntegerArgumentType.integer())
                                        .executes(context -> modifySlotCountCommand(
                                            context.getSource(),
                                            EntityArgumentType.getEntity(context, "entity"),
                                            context.getArgument("group", String.class),
                                            context.getArgument("slot", String.class),
                                            IntegerArgumentType.getInteger(context, "amount"))
                                        )
                                    )
                                )
                            )
                        )
                        .then(literal("set")
                            .then(literal("modifier")
                                .then(argument("group", string())
                                    .then(argument("slot", string())
                                        .then(argument("value", IntegerArgumentType.integer())
                                            .executes(context -> setSlotCountCommand(
                                                context.getSource(),
                                                EntityArgumentType.getEntity(context, "entity"),
                                                context.getArgument("group", String.class),
                                                context.getArgument("slot", String.class),
                                                IntegerArgumentType.getInteger(context, "value"))
                                            )
                                        )
                                    )
                                )
                            )
                        )
                        .then(literal("reset")
                            .then(argument("group", string())
                                .then(argument("slot", string())
                                    .executes(context -> resetSlotContCommand(
                                        context.getSource(),
                                        EntityArgumentType.getEntity(context, "entity"),
                                        context.getArgument("group", String.class),
                                        context.getArgument("slot", String.class))
                                    )
                                )
                            )
                        )
                        .then(literal("count")
                            .then(argument("group", string())
                                .then(argument("slot", string())
                                    .executes(context -> getSlotCountCommand(
                                        context.getSource(),
                                        EntityArgumentType.getEntity(context, "entity"),
                                        context.getArgument("group", String.class),
                                        context.getArgument("slot", String.class))
                                    )
                                )
                            )
                        )
                        .then(literal("clear")
                            .then(literal("modifiers")
                                .executes(context -> clearAllSlotModifiersCommand(
                                    context.getSource(),
                                    EntityArgumentType.getEntity(context, "entity"))
                                )
                                .then(argument("group", string())
                                    .then(argument("slot", string())
                                        .executes(context -> clearSlotModifiersCommand(
                                            context.getSource(),
                                            EntityArgumentType.getEntity(context, "entity"),
                                            context.getArgument("group", String.class),
                                            context.getArgument("slot", String.class))
                                        )
                                    ))
                            )
                        )
                    )
                )
        );
    }

    private static int modifySlotCountCommand(ServerCommandSource source, Entity entity, String group, String slot, int amount) throws CommandSyntaxException {
        Pair<LivingEntity, TrinketComponent> pair = getTrinketComponent(source, entity);
        TrinketInventory slotInventory = getSlot(pair, group, slot);

        TrinketsSlotModifierUtil.modifySlotCount(slotInventory, amount);
        source.sendFeedback(() -> Text.translatable("commands.shappoli.shappoli.trinkets.modify_slot_count", entity.getName(), group, slot, slotInventory.size()), true);

        return slotInventory.size();
    }

    private static int setSlotCountCommand(ServerCommandSource source, Entity entity, String group, String slot, int value) throws CommandSyntaxException {
        Pair<LivingEntity, TrinketComponent> pair = getTrinketComponent(source, entity);
        TrinketInventory slotInventory = getSlot(pair, group, slot);

        TrinketsSlotModifierUtil.setSlotCountModifier(slotInventory, value);
        source.sendFeedback(() -> Text.translatable("commands.shappoli.shappoli.trinkets.set_slot_count", entity.getName(), group, slot, value, slotInventory.size()), true);

        return slotInventory.size();
    }

    private static int resetSlotContCommand(ServerCommandSource source, Entity entity, String group, String slot) throws CommandSyntaxException {
        Pair<LivingEntity, TrinketComponent> pair = getTrinketComponent(source, entity);
        TrinketInventory slotInventory = getSlot(pair, group, slot);

        TrinketsSlotModifierUtil.resetSlotCount(slotInventory);
        source.sendFeedback(() -> Text.translatable("commands.shappoli.shappoli.trinkets.reset_slot_count", entity.getName(), group, slot, slotInventory.size()), true);

        return slotInventory.size();
    }

    private static int getSlotCountCommand(ServerCommandSource source, Entity entity, String group, String slot) throws CommandSyntaxException {
        Pair<LivingEntity, TrinketComponent> pair = getTrinketComponent(source, entity);
        TrinketInventory slotInventory = getSlot(pair, group, slot);

        source.sendFeedback(() -> Text.translatable("commands.shappoli.shappoli.trinkets.get_slot_count", entity.getName(), slotInventory.size(), group, slot), true);

        return Command.SINGLE_SUCCESS;
    }


    private static int clearAllSlotModifiersCommand(ServerCommandSource source, Entity entity) throws CommandSyntaxException {
        Pair<LivingEntity, TrinketComponent> pair = getTrinketComponent(source, entity);
        TrinketComponent trinketComponent = pair.getRight();

        trinketComponent.clearModifiers();
        TrinketsUtil.updateInventories(trinketComponent);
        source.sendFeedback(() -> Text.translatable("commands.shappoli.shappoli.trinkets.clear_slot_modifiers.all", entity.getName()), true);

        return Command.SINGLE_SUCCESS;
    }

    private static int clearSlotModifiersCommand(ServerCommandSource source, Entity entity, String group, String slot) throws CommandSyntaxException {
        Pair<LivingEntity, TrinketComponent> pair = getTrinketComponent(source, entity);
        TrinketComponent trinketComponent = pair.getRight();

        TrinketInventory slotInventory = getSlot(pair, group, slot);

        slotInventory.clearModifiers();
        TrinketsUtil.updateInventories(trinketComponent);
        source.sendFeedback(() -> Text.translatable("commands.shappoli.shappoli.trinkets.clear_slot_modifiers.slot", entity.getName(), group, slot), true);

        return Command.SINGLE_SUCCESS;
    }


    private static Pair<LivingEntity, TrinketComponent> getTrinketComponent(ServerCommandSource source, Entity entity) throws CommandSyntaxException {
        if (!(entity instanceof LivingEntity livingEntity)) {
            throw NOT_LIVING_ENTITY.create(entity.getName());
        }

        TrinketComponent trinketComponent = TrinketsApi.getTrinketComponent(livingEntity).orElse(null);
        if (trinketComponent == null) {
            throw NO_TRINKET_COMPONENT.create(entity.getName());
        }

        return new Pair<>(livingEntity, trinketComponent);
    }

    private static Map<String, TrinketInventory> getSlotGroup(Pair<LivingEntity, TrinketComponent> entityAndTrinketComponent, String group) throws CommandSyntaxException {
        TrinketComponent trinketComponent = entityAndTrinketComponent.getRight();
        Map<String, TrinketInventory> slotGroup = trinketComponent.getInventory().getOrDefault(group, null);
        if (slotGroup == null) {
            throw GROUP_NOT_FOUND.create(entityAndTrinketComponent.getLeft().getName(), group);
        }
        return slotGroup;
    }

    private static TrinketInventory getSlot(Pair<LivingEntity, TrinketComponent> entityAndTrinketComponent, String group, String slot) throws CommandSyntaxException {
        Map<String, TrinketInventory> slotGroup = getSlotGroup(entityAndTrinketComponent, group);
        TrinketInventory slotInventory = slotGroup.getOrDefault(slot, null);
        if (slotInventory == null) {
            throw SLOT_NOT_FOUND.create(entityAndTrinketComponent.getLeft().getName(), group, slot);
        }
        return slotInventory;
    }
}
