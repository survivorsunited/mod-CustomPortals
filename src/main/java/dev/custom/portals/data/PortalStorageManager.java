package dev.custom.portals.data;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

import dev.custom.portals.util.PortalSyncPayload;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class PortalStorageManager {
    private static final Logger LOGGER = LoggerFactory.getLogger("CustomPortals/Storage");
    private static final PortalComponent CLIENT_COMPONENT = new PortalComponent();
    private static final Map<ServerWorld, PortalComponent> WORLD_COMPONENTS = new ConcurrentHashMap<>();

    private PortalStorageManager() {}

    public static BasePortalComponent get(World world) {
        if (world instanceof ServerWorld serverWorld) {
            return WORLD_COMPONENTS.computeIfAbsent(serverWorld, w -> {
                PortalPersistentState state = PortalPersistentState.getOrCreate(serverWorld);
                return new PortalComponent(serverWorld, state);
            });
        }
        return CLIENT_COMPONENT;
    }

    public static void applyClientSnapshot(List<CustomPortal> portals) {
        PortalRegistry registry = new PortalRegistry();
        for (CustomPortal portal : portals) {
            registry.register(portal);
        }
        CLIENT_COMPONENT.setPortalRegistry(registry);
    }

    public static void syncToAll(ServerWorld world) {
        if (world == null) {
            return;
        }
        BasePortalComponent component = get(world);
        List<CustomPortal> portals = new ArrayList<>(component.getPortalRegistry().getPortals());
        
        PortalSyncPayload payload = new PortalSyncPayload(portals);
        MinecraftServer server = world.getServer();
        if (server != null) {
            for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
                if (player.getWorld() == world) {
                    ServerPlayNetworking.send(player, payload);
                }
            }
        }
    }

    public static void syncToPlayer(ServerPlayerEntity player) {
        if (player == null || !(player.getWorld() instanceof ServerWorld serverWorld)) {
            return;
        }
        BasePortalComponent component = get(serverWorld);
        List<CustomPortal> portals = new ArrayList<>(component.getPortalRegistry().getPortals());
        ServerPlayNetworking.send(player, new PortalSyncPayload(portals));
    }

    public static void clearWorldState(ServerWorld world) {
        WORLD_COMPONENTS.remove(world);
    }
}

