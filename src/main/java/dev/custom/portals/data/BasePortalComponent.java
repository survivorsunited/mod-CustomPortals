package dev.custom.portals.data;

import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public interface BasePortalComponent {
    PortalRegistry getPortalRegistry();
    CustomPortal getPortalFromPos(BlockPos pos);
    //  boolean settingsChanged();
    void setPortalRegistry(PortalRegistry portalRegistry);
    void registerPortal(CustomPortal portal);
    void unregisterPortal(CustomPortal portal);
    void tryWithAll(CustomPortal portal);
    void refreshPortals();
    //void clearPortals();
    void syncWithAll(MinecraftServer server);
}
