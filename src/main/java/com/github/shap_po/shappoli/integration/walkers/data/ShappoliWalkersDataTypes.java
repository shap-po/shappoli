package com.github.shap_po.shappoli.integration.walkers.data;

import com.github.shap_po.shappoli.integration.walkers.power.factory.ability.ShapeAbilityFactory;
import com.github.shap_po.shappoli.integration.walkers.registry.ShappoliWalkersRegistries;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import io.github.apace100.calio.ClassUtil;
import io.github.apace100.calio.data.SerializableDataType;
import io.github.apace100.calio.util.DynamicIdentifier;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

public class ShappoliWalkersDataTypes {
    public static final SerializableDataType<ShapeAbilityFactory.Instance> SHAPE_ABILITY = new SerializableDataType<>(
        ClassUtil.castClass(ShapeAbilityFactory.Instance.class),
        (buff, instance) -> instance.write(buff),
        buff -> {
            Identifier factoryId = buff.readIdentifier();
            return ShappoliWalkersRegistries.SHAPE_ABILITY.getOrEmpty(factoryId)
                .orElseThrow(() -> new IllegalArgumentException("Unknown ability factory: " + factoryId))
                .read(buff);
        },
        jsonElement -> {
            if (!(jsonElement instanceof JsonObject jsonObject)) {
                throw new JsonSyntaxException("Expected a JSON object.");
            }
            Identifier factoryId = DynamicIdentifier.of(JsonHelper.getString(jsonObject, "type"));
            return ShappoliWalkersRegistries.SHAPE_ABILITY.getOrEmpty(factoryId)
                .orElseThrow(() -> new IllegalArgumentException("Unknown ability factory: " + factoryId))
                .read(jsonObject);
        },
        ShapeAbilityFactory.Instance::toJson
    );
}
