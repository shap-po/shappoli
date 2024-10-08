package com.github.shap_po.shappoli.integration.walkers.power.factory.ability;

import com.google.gson.JsonObject;
import io.github.apace100.apoli.power.factory.Factory;
import io.github.apace100.calio.data.SerializableData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ShapeAbilityFactory implements Factory {
    private final Identifier identifier;
    private final BiConsumer<SerializableData.Instance, Pair<ServerPlayerEntity, LivingEntity>> useAbility;

    protected final SerializableData data;

    public ShapeAbilityFactory(Identifier identifier, SerializableData data, @NotNull BiConsumer<SerializableData.Instance, Pair<ServerPlayerEntity, LivingEntity>> useAbility) {
        this.identifier = identifier;
        this.data = data.copy();
        this.useAbility = useAbility;
    }

    public class Instance implements Consumer<Pair<ServerPlayerEntity, LivingEntity>> {
        protected final SerializableData.Instance dataInstance;

        protected Instance(SerializableData.Instance data) {
            this.dataInstance = data;
        }

        @Override
        public void accept(Pair<ServerPlayerEntity, LivingEntity> playerAndShape) {
            useAbility.accept(dataInstance, playerAndShape);
        }

        public void write(PacketByteBuf buf) {
            buf.writeIdentifier(identifier);
            data.write(buf, dataInstance);
        }

        public JsonObject toJson() {
            JsonObject jsonObject = data.write(dataInstance);
            jsonObject.addProperty("type", identifier.toString());

            return jsonObject;
        }
    }

    @Override
    public Identifier getSerializerId() {
        return identifier;
    }

    @Override
    public SerializableData getSerializableData() {
        return data;
    }

    public Instance read(JsonObject json) {
        return new Instance(data.read(json));
    }

    public Instance read(PacketByteBuf buffer) {
        return new Instance(data.read(buffer));
    }
}
