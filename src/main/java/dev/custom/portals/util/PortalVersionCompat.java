package dev.custom.portals.util;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.GameRules;

/** Portal APIs before the Minecraft 1.21.11 game-rule refactor. */
public final class PortalVersionCompat {
    private PortalVersionCompat() {}

    public static boolean mobSpawning(ServerWorld world) {
        return world.getGameRules().getBoolean(GameRules.DO_MOB_SPAWNING);
    }

    public static int creativePortalDelay(ServerWorld world) {
        return world.getGameRules().getInt(GameRules.PLAYERS_NETHER_PORTAL_CREATIVE_DELAY);
    }

    public static boolean canEnterWithPortal(ServerWorld source, ServerWorld destination) {
        return source.getServer().isEnterableWithPortal(destination);
    }
}
