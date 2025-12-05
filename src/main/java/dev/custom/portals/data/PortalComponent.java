package dev.custom.portals.data;

import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class PortalComponent implements BasePortalComponent {

    private PortalRegistry portalRegistry;

    public PortalComponent() {
        portalRegistry = new PortalRegistry();
    }

    @Override
    public PortalRegistry getPortalRegistry() { return portalRegistry; }

    @Override
    public CustomPortal getPortalFromPos(BlockPos pos) {
        return portalRegistry.getPortalFromPos(pos);
    }

    @Override
    public void setPortalRegistry(PortalRegistry portalRegistry) { this.portalRegistry = portalRegistry; }

    @Override
    public void registerPortal(CustomPortal portal) {
        portalRegistry.register(portal);
    }

    @Override
    public void unregisterPortal(CustomPortal portal) {
        portalRegistry.unregister(portal);
    }

    @Override
    public void tryWithAll(CustomPortal portal) {
        portalRegistry.tryWithAll(portal);
    }

    @Override
    public void refreshPortals() {
        portalRegistry.refreshPortals();
    }

    @Override
    public void syncWithAll(MinecraftServer server) {
        PortalStorageManager.syncToAll(server);
    }
}
