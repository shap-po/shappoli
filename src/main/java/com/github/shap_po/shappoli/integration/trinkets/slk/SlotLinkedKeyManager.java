package com.github.shap_po.shappoli.integration.trinkets.slk;

import com.github.shap_po.shappoli.Shappoli;
import com.github.shap_po.shappoli.integration.trinkets.networking.s2c.SyncSlotLinkedKeysS2CPacket;
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

public class SlotLinkedKeyManager extends IdentifiableMultiJsonDataLoader implements IdentifiableResourceReloadListener {
    public static final String DIRECTORY = "slot_linked_keys";
    public static final Set<Identifier> DEPENDENCIES = new HashSet<>();
    public static final Identifier ID = Shappoli.identifier(DIRECTORY);

    private static final Object2ObjectOpenHashMap<Identifier, SlotLinkedKey> SLOT_LINKED_KEYS_BY_ID = new Object2ObjectOpenHashMap<>();
    private static final Object2ObjectOpenHashMap<Identifier, Integer> LOADING_PRIORITIES = new Object2ObjectOpenHashMap<>();

    private static final Gson GSON = new GsonBuilder()
        .disableHtmlEscaping()
        .setPrettyPrinting()
        .create();

    public SlotLinkedKeyManager() {
        super(GSON, DIRECTORY, ResourceType.SERVER_DATA);

        // load before the power manager so powers can depend on slot linked keys
        PowerManager.DEPENDENCIES.add(ID);
        ServerLifecycleEvents.SYNC_DATA_PACK_CONTENTS.addPhaseOrdering(ID, PowerManager.ID);
        ServerLifecycleEvents.SYNC_DATA_PACK_CONTENTS.register(ID, (player, joined) -> send(player));
    }

    @Override
    protected void apply(MultiJsonDataContainer prepared, ResourceManager manager, Profiler profiler) {
        Shappoli.LOGGER.info("Reading slot linked keys data from data packs...");

        DynamicRegistryManager dynamicRegistries = CalioServer.getDynamicRegistries().orElse(null);
        startBuilding();

        if (dynamicRegistries == null) {
            Shappoli.LOGGER.error("Can't read slot linked keys from data packs without access to dynamic registries!");
            endBuilding();
            return;
        }

        Map<Identifier, List<PrioritizedEntry<SlotLinkedKey>>> loadedSLK = new Object2ObjectOpenHashMap<>();
        prepared.forEach((packName, id, jsonElement) -> {
            try {
                SerializableData.CURRENT_NAMESPACE = id.getNamespace();
                SerializableData.CURRENT_PATH = id.getPath();

                if (!(jsonElement instanceof JsonObject jsonObject)) {
                    throw new JsonSyntaxException("Not a JSON object: " + jsonElement);
                }

                jsonObject.addProperty("id", id.toString());

                SlotLinkedKey slotLinkedKey = SlotLinkedKey.DATA_TYPE.read(dynamicRegistries.getOps(JsonOps.INSTANCE), jsonObject).getOrThrow();
                int currLoadingPriority = JsonHelper.getInt(jsonObject, "loading_priority", 0);

                PrioritizedEntry<SlotLinkedKey> entry = new PrioritizedEntry<>(slotLinkedKey, currLoadingPriority);
                int prevLoadingPriority = LOADING_PRIORITIES.getOrDefault(id, Integer.MIN_VALUE);

                if (slotLinkedKey.shouldReplace() && currLoadingPriority <= prevLoadingPriority) {
                    Shappoli.LOGGER.warn("Ignoring slot linked key \"{}\" with 'replace' set to true from data pack [{}]. Its loading priority ({}) must be higher than {} to replace it!", id, packName, currLoadingPriority, prevLoadingPriority);
                    return; // break
                }

                if (slotLinkedKey.shouldReplace()) {
                    Shappoli.LOGGER.info("Slot linked key \"{}\" has been replaced by data pack [{}]!", id, packName);
                }

                loadedSLK.computeIfAbsent(id, k -> new LinkedList<>()).add(entry);
                LOADING_PRIORITIES.put(id, currLoadingPriority);

            } catch (Exception e) {
                Shappoli.LOGGER.error("There was a problem reading slot linked key \"{}\": {}", id, e.getMessage());
            }
        });

        SerializableData.CURRENT_NAMESPACE = null;
        SerializableData.CURRENT_PATH = null;

        Shappoli.LOGGER.info("Finished reading {} slot linked keys. Merging similar ones...", loadedSLK.size());
        loadedSLK.forEach((id, entries) -> {
            AtomicReference<SlotLinkedKey> currentSlotLinkedKeys = new AtomicReference<>();
            entries.sort(Comparator.comparing(PrioritizedEntry::priority));

            for (PrioritizedEntry<SlotLinkedKey> entry : entries) {
                if (currentSlotLinkedKeys.get() == null) {
                    currentSlotLinkedKeys.set(entry.value());
                } else {
                    currentSlotLinkedKeys.accumulateAndGet(entry.value(), SlotLinkedKey::merge);
                }
            }

            SLOT_LINKED_KEYS_BY_ID.put(id, currentSlotLinkedKeys.get());
        });

        endBuilding();
        Shappoli.LOGGER.info("Finished merging similar slot linked keys. Total count: {}", size());
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
        SLOT_LINKED_KEYS_BY_ID.clear();
    }

    private static void endBuilding() {
        LOADING_PRIORITIES.clear();
        SLOT_LINKED_KEYS_BY_ID.trim();
    }

    public static int size() {
        return SLOT_LINKED_KEYS_BY_ID.size();
    }

    @Nullable
    public static SlotLinkedKey getNullable(Identifier id) {
        return SLOT_LINKED_KEYS_BY_ID.get(id);
    }

    public static Collection<SlotLinkedKey> values() {
        return SLOT_LINKED_KEYS_BY_ID.values();
    }

    public static void send(ServerPlayerEntity player) {
        if (player.server.isDedicated()) {
            ServerPlayNetworking.send(player, new SyncSlotLinkedKeysS2CPacket(SLOT_LINKED_KEYS_BY_ID));
        }
    }

    @Environment(EnvType.CLIENT)
    public static void receive(SyncSlotLinkedKeysS2CPacket packet) {
        startBuilding();

        SLOT_LINKED_KEYS_BY_ID.putAll(packet.slotLinkedKeyMap());

        endBuilding();
    }
}
