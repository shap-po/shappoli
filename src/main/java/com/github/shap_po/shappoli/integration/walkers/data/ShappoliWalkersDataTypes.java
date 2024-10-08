package com.github.shap_po.shappoli.integration.walkers.data;

import com.github.shap_po.shappoli.integration.walkers.ability.factory.ShapeAbilityFactory;
import com.github.shap_po.shappoli.integration.walkers.registry.ShappoliWalkersRegistries;
import com.mojang.serialization.*;
import io.github.apace100.apoli.Apoli;
import io.github.apace100.apoli.factory.Factory;
import io.github.apace100.calio.data.CompoundSerializableDataType;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataType;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.apace100.calio.util.IdentifierAlias;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiFunction;
import java.util.stream.Stream;

public class ShappoliWalkersDataTypes {
    public static final SerializableDataType<ShapeAbilityFactory.Instance> SHAPE_ABILITY = factory(ShappoliWalkersRegistries.SHAPE_ABILITY, "Shape ability");

    public static <F extends Factory, I extends Factory.Instance> SerializableDataType<I> factory(Registry<F> registry, String name) {
        return factory(registry, null, name);
    }

    public static <F extends Factory, I extends Factory.Instance> SerializableDataType<I> factory(Registry<F> registry, IdentifierAlias aliases, String name) {
        return factory("type", registry, aliases, (conditionFactories, id) -> name + " \"" + id + "\" is not registered!");
    }

    /**
     * Almost 1:1 copy of {@link io.github.apace100.apoli.data.ApoliDataTypes#condition(String, Registry, IdentifierAlias, BiFunction)}
     */
    @SuppressWarnings({"unchecked"})
    public static <F extends Factory, I extends Factory.Instance> SerializableDataType<I> factory(String fieldName, Registry<F> registry, @Nullable IdentifierAlias aliases, BiFunction<Registry<F>, Identifier, String> errorHandler) {
        return new CompoundSerializableDataType<>(
            new SerializableData()
                .add(fieldName, SerializableDataType.registry(registry, Apoli.MODID, aliases, errorHandler)),
            serializableData -> {
                boolean root = serializableData.isRoot();
                return new MapCodec<>() {
                    @Override
                    public <T> Stream<T> keys(DynamicOps<T> ops) {
                        return serializableData.keys(ops);
                    }

                    @Override
                    public <T> DataResult<I> decode(DynamicOps<T> ops, MapLike<T> input) {
                        return (DataResult<I>) serializableData.decode(ops, input)
                            .map(factoryData -> (F) factoryData.get(fieldName))
                            .flatMap(factory -> factory.getSerializableData().setRoot(root).decode(ops, input)
                                .map(factory::fromData));
                    }

                    @Override
                    public <T> RecordBuilder<T> encode(I input, DynamicOps<T> ops, RecordBuilder<T> prefix) {
                        prefix.add(fieldName, SerializableDataTypes.IDENTIFIER.write(ops, input.getSerializerId()));
                        input.getSerializableData().setRoot(root).encode(input.getData(), ops, prefix);

                        return prefix;
                    }
                };
            },
            serializableData -> new PacketCodec<>() {
                @Override
                public I decode(RegistryByteBuf buf) {
                    Identifier factoryId = buf.readIdentifier();
                    return (I) registry.getOrEmpty(factoryId)
                        .map(factory -> factory.receive(buf))
                        .orElseThrow(() -> new IllegalStateException(errorHandler.apply(registry, factoryId)));
                }

                @Override
                public void encode(RegistryByteBuf buf, I value) {
                    value.send(buf);
                }
            }
        );
    }
}
