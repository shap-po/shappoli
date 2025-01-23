package com.github.shap_po.shappoli.integration.trinkets.keybinding;

import com.github.shap_po.shappoli.Shappoli;
import com.github.shap_po.shappoli.integration.trinkets.networking.s2c.SyncTrinketKeyBindingsS2CPacket;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.mojang.serialization.JsonOps;
import io.github.apace100.apoli.power.PowerManager;
import io.github.apace100.apoli.util.PrioritizedEntry;
import io.github.apace100.calio.CalioServer;
import io.github.apace100.calio.data.IdentifiableMultiJsonDataLoader;
import io.github.apace100.calio.data.MultiJsonDataContainer;
import io.github.apace100.calio.data.SerializableData;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.profiler.Profiler;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class TrinketKeyBindingManager extends IdentifiableMultiJsonDataLoader implements IdentifiableResourceReloadListener {
    public static final Set<Identifier> DEPENDENCIES = new HashSet<>();
    public static final Identifier ID = Shappoli.identifier("trinket_keybindings");

    private static final Object2ObjectOpenHashMap<Identifier, TrinketKeyBinding> TRINKET_KEY_BINDINGS_BY_ID = new Object2ObjectOpenHashMap<>();
    private static final Object2ObjectOpenHashMap<Identifier, Integer> LOADING_PRIORITIES = new Object2ObjectOpenHashMap<>();

    private static final Gson GSON = new GsonBuilder()
        .disableHtmlEscaping()
        .setPrettyPrinting()
        .create();

    public TrinketKeyBindingManager() {
        super(GSON, "trinket_keybindings", ResourceType.SERVER_DATA);

        // load before the power manager so powers can depend on trinket keybindings
        PowerManager.DEPENDENCIES.add(ID);
        ServerLifecycleEvents.SYNC_DATA_PACK_CONTENTS.addPhaseOrdering(ID, PowerManager.ID);
        ServerLifecycleEvents.SYNC_DATA_PACK_CONTENTS.register(ID, (player, joined) -> send(player));
    }

    @Override
    protected void apply(MultiJsonDataContainer prepared, ResourceManager manager, Profiler profiler) {
        Shappoli.LOGGER.info("Reading trinket keybindings data from data packs...");

        DynamicRegistryManager dynamicRegistries = CalioServer.getDynamicRegistries().orElse(null);
        startBuilding();

        if (dynamicRegistries == null) {
            Shappoli.LOGGER.error("Can't read trinket keybindings from data packs without access to dynamic registries!");
            endBuilding();
            return;
        }

        Map<Identifier, List<PrioritizedEntry<TrinketKeyBinding>>> loadedSLK = new Object2ObjectOpenHashMap<>();
        prepared.forEach((packName, id, jsonElement) -> {
            try {
                SerializableData.CURRENT_NAMESPACE = id.getNamespace();
                SerializableData.CURRENT_PATH = id.getPath();

                if (!(jsonElement instanceof JsonObject jsonObject)) {
                    throw new JsonSyntaxException("Not a JSON object: " + jsonElement);
                }

                jsonObject.addProperty("id", id.toString());

                TrinketKeyBinding trinketKeybinding = TrinketKeyBinding.DATA_TYPE.read(dynamicRegistries.getOps(JsonOps.INSTANCE), jsonObject).getOrThrow();
                int currLoadingPriority = JsonHelper.getInt(jsonObject, "loading_priority", 0);

                PrioritizedEntry<TrinketKeyBinding> entry = new PrioritizedEntry<>(trinketKeybinding, currLoadingPriority);
                int prevLoadingPriority = LOADING_PRIORITIES.getOrDefault(id, Integer.MIN_VALUE);

                if (trinketKeybinding.shouldReplace() && currLoadingPriority <= prevLoadingPriority) {
                    Shappoli.LOGGER.warn("Ignoring trinket keybinding \"{}\" with 'replace' set to true from data pack [{}]. Its loading priority ({}) must be higher than {} to replace it!", id, packName, currLoadingPriority, prevLoadingPriority);
                    return; // break
                }

                if (trinketKeybinding.shouldReplace()) {
                    Shappoli.LOGGER.info("trinket keybinding \"{}\" has been replaced by data pack [{}]!", id, packName);
                }

                loadedSLK.computeIfAbsent(id, k -> new LinkedList<>()).add(entry);
                LOADING_PRIORITIES.put(id, currLoadingPriority);

            } catch (Exception e) {
                Shappoli.LOGGER.error("There was a problem reading trinket keybinding \"{}\": {}", id, e.getMessage());
            }
        });

        SerializableData.CURRENT_NAMESPACE = null;
        SerializableData.CURRENT_PATH = null;

        Shappoli.LOGGER.info("Finished reading {} trinket keybindings. Merging similar ones...", loadedSLK.size());
        loadedSLK.forEach((id, entries) -> {
            AtomicReference<TrinketKeyBinding> currentTrinketKeyBindings = new AtomicReference<>();
            entries.sort(Comparator.comparing(PrioritizedEntry::priority));

            for (PrioritizedEntry<TrinketKeyBinding> entry : entries) {
                if (currentTrinketKeyBindings.get() == null) {
                    currentTrinketKeyBindings.set(entry.value());
                } else {
                    currentTrinketKeyBindings.accumulateAndGet(entry.value(), TrinketKeyBinding::merge);
                }
            }

            TRINKET_KEY_BINDINGS_BY_ID.put(id, currentTrinketKeyBindings.get());
        });

        endBuilding();
        Shappoli.LOGGER.info("Finished merging similar trinket keybindings. Total count: {}", size());
    }

    @Override
    public Identifier getFabricId() {
        return ID;
    }

    @Override
    public Collection<Identifier> getFabricDependencies() {
        return DEPENDENCIES;
    }

    private static void startBuilding() {
        LOADING_PRIORITIES.clear();
        TRINKET_KEY_BINDINGS_BY_ID.clear();
    }

    private static void endBuilding() {
        LOADING_PRIORITIES.clear();
        TRINKET_KEY_BINDINGS_BY_ID.trim();
    }

    public static int size() {
        return TRINKET_KEY_BINDINGS_BY_ID.size();
    }

    @Nullable
    public static TrinketKeyBinding getNullable(Identifier id) {
        return TRINKET_KEY_BINDINGS_BY_ID.get(id);
    }

    public static Collection<TrinketKeyBinding> values() {
        return TRINKET_KEY_BINDINGS_BY_ID.values();
    }

    public static void send(ServerPlayerEntity player) {
        if (player.server.isDedicated()) {
            ServerPlayNetworking.send(player, new SyncTrinketKeyBindingsS2CPacket(TRINKET_KEY_BINDINGS_BY_ID));
        }
    }

    @Environment(EnvType.CLIENT)
    public static void receive(SyncTrinketKeyBindingsS2CPacket packet) {
        startBuilding();

        TRINKET_KEY_BINDINGS_BY_ID.putAll(packet.trinketKeyBindingMap());

        endBuilding();
    }
}
