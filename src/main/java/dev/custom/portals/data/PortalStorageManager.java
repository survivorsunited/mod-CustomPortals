package dev.custom.portals.data;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import dev.custom.portals.util.PortalSyncPayload;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtIo;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.WorldSavePath;
import net.minecraft.world.World;
import net.minecraft.nbt.NbtSizeTracker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class PortalStorageManager {
    private static final Logger LOGGER = LoggerFactory.getLogger("CustomPortals/Storage");
    private static final String DATA_FILE = "custom_portals.dat";
    private static final PortalComponent SERVER_COMPONENT = new PortalComponent();
    private static final PortalComponent CLIENT_COMPONENT = new PortalComponent();

    private static Path savePath;
    private static boolean initialized;

    private PortalStorageManager() {}

    public static BasePortalComponent get(World world) {
        if (world instanceof ServerWorld) {
            return SERVER_COMPONENT;
        }
        return CLIENT_COMPONENT;
    }

    public static void initialize(MinecraftServer server) {
        if (server == null || initialized) {
            return;
        }
        Path dataDir = server.getSavePath(WorldSavePath.ROOT).resolve("data");
        savePath = dataDir.resolve(DATA_FILE);
        loadFromDisk();
        initialized = true;
    }

    public static void clearServerState() {
        initialized = false;
        savePath = null;
        SERVER_COMPONENT.setPortalRegistry(new PortalRegistry());
    }

    public static void applyClientSnapshot(List<CustomPortal> portals) {
        PortalRegistry registry = new PortalRegistry();
        for (CustomPortal portal : portals) {
            registry.register(portal);
        }
        CLIENT_COMPONENT.setPortalRegistry(registry);
    }

    public static void syncToAll(MinecraftServer server) {
        if (server == null) {
            return;
        }
        List<CustomPortal> portals = snapshot();
        persistSnapshot(portals);
        PortalSyncPayload payload = new PortalSyncPayload(portals);
        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
            ServerPlayNetworking.send(player, payload);
        }
    }

    public static void syncToPlayer(ServerPlayerEntity player) {
        if (player == null) {
            return;
        }
        ServerPlayNetworking.send(player, new PortalSyncPayload(snapshot()));
    }

    public static List<CustomPortal> snapshot() {
        return new ArrayList<>(SERVER_COMPONENT.getPortalRegistry().getPortals());
    }

    private static void loadFromDisk() {
        PortalRegistry registry = new PortalRegistry();
        if (savePath != null && Files.exists(savePath)) {
            try {
                NbtCompound nbt = NbtIo.readCompressed(savePath, NbtSizeTracker.ofUnlimitedBytes());
                for (CustomPortal portal : PortalDataSerializer.readPortals(nbt)) {
                    registry.register(portal);
                }
            } catch (IOException exception) {
                LOGGER.error("Failed to load Custom Portals data", exception);
            }
        }
        SERVER_COMPONENT.setPortalRegistry(registry);
    }

    private static void persistSnapshot(List<CustomPortal> portals) {
        if (savePath == null) {
            return;
        }
        try {
            Files.createDirectories(savePath.getParent());
            NbtCompound nbt = PortalDataSerializer.createNbt(portals);
            NbtIo.writeCompressed(nbt, savePath);
        } catch (IOException exception) {
            LOGGER.error("Failed to save Custom Portals data", exception);
        }
    }
}

