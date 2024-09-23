package com.github.shap_po.shappoli.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.CommandNode;
import net.minecraft.server.command.ServerCommandSource;

import static net.minecraft.server.command.CommandManager.literal;

public class ShappoliCommand {
    private static final LiteralArgumentBuilder<ServerCommandSource> rootNode = literal("shappoli");

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(rootNode);
    }

    public static void registerSubCommand(CommandNode<ServerCommandSource> node) {
        rootNode.then(node);
    }

    public static void registerSubCommand(LiteralArgumentBuilder<ServerCommandSource> node) {
        rootNode.then(node);
    }
}
