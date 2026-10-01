package dev.custom.portals.util;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.rule.GameRules;

/** Registry-backed game rules and world-level portal restrictions in Minecraft 1.21.11. */
public final class PortalVersionCompat {
    private PortalVersionCompat() {}

    public static boolean mobSpawning(ServerWorld world) {
        return world.getGameRules().getValue(GameRules.DO_MOB_SPAWNING);
    }

    public static int creativePortalDelay(ServerWorld world) {
        return world.getGameRules().getValue(GameRules.PLAYERS_NETHER_PORTAL_CREATIVE_DELAY);
    }

    public static boolean canEnterWithPortal(ServerWorld source, ServerWorld destination) {
        return source.isEnterableWithPortal(destination);
    }
}
