package com.github.shap_po.shappoli.integration.walkers.ability.factory;

import io.github.apace100.apoli.action.factory.ActionTypeFactory;
import io.github.apace100.calio.data.SerializableData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;

public class ShapeAbilityFactory<E extends LivingEntity> extends ActionTypeFactory<Pair<ServerPlayerEntity, E>> {
    public ShapeAbilityFactory(Identifier id, SerializableData serializableData, @NotNull BiConsumer<SerializableData.Instance, Pair<ServerPlayerEntity, E>> effect) {
        super(id, serializableData, effect);
    }
}
