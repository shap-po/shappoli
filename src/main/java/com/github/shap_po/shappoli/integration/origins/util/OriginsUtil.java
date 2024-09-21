package com.github.shap_po.shappoli.integration.origins.util;

import io.github.apace100.origins.component.OriginComponent;
import io.github.apace100.origins.origin.Origin;
import io.github.apace100.origins.origin.OriginLayer;
import io.github.apace100.origins.origin.OriginLayers;
import io.github.apace100.origins.origin.OriginRegistry;
import io.github.apace100.origins.registry.ModComponents;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings({"UnusedReturnValue"})
public class OriginsUtil {
    public static boolean setOrigin(@Nullable OriginComponent originComponent, @Nullable OriginLayer layer, @Nullable Origin origin) {
        if (originComponent == null || layer == null || origin == null) {
            return false;
        }
        originComponent.setOrigin(layer, origin);
        originComponent.sync();
        return true;
    }

    public static boolean setOrigin(Entity entity, @Nullable OriginLayer layer, @Nullable Origin origin) {
        return setOrigin(getOriginComponent(entity), layer, origin);
    }

    public static @Nullable Origin getOrigin(@Nullable OriginComponent originComponent, OriginLayer layer) {
        if (originComponent == null) {
            return null;
        }
        return originComponent.getOrigin(layer);
    }

    public static @Nullable Origin getOrigin(Entity entity, OriginLayer layer) {
        return getOrigin(getOriginComponent(entity), layer);
    }

    public static @Nullable Origin getOrigin(Identifier id) {
        try {
            return OriginRegistry.get(id);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public static @Nullable OriginLayer getLayer(Identifier layerId) {
        return OriginLayers.getNullableLayer(layerId);
    }

    public static @Nullable OriginComponent getOriginComponent(Entity entity) {
        return ModComponents.ORIGIN.getNullable(entity);
    }
}
