package dev.custom.portals.data;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public class PortalComponent implements BasePortalComponent {

    private PortalRegistry portalRegistry;
    private ServerWorld world;
    private PortalPersistentState persistentState;

    public PortalComponent() {
        portalRegistry = new PortalRegistry();
    }

    public PortalComponent(ServerWorld world, PortalPersistentState persistentState) {
        this.world = world;
        this.persistentState = persistentState;
        this.portalRegistry = persistentState.getPortalRegistry();
    }

    @Override
    public PortalRegistry getPortalRegistry() { return portalRegistry; }

    @Override
    public CustomPortal getPortalFromPos(BlockPos pos) {
        return portalRegistry.getPortalFromPos(pos);
    }

    @Override
    public void setPortalRegistry(PortalRegistry portalRegistry) { 
        this.portalRegistry = portalRegistry;
        if (persistentState != null) {
            persistentState.setPortalRegistry(portalRegistry);
        }
    }

    @Override
    public void registerPortal(CustomPortal portal) {
        portalRegistry.register(portal);
        markDirty();
    }

    @Override
    public void unregisterPortal(CustomPortal portal) {
        portalRegistry.unregister(portal);
        markDirty();
    }

    @Override
    public void tryWithAll(CustomPortal portal) {
        portalRegistry.tryWithAll(portal);
        markDirty();
    }

    @Override
    public void refreshPortals() {
        portalRegistry.refreshPortals();
        markDirty();
    }

    private void markDirty() {
        if (persistentState != null) {
            persistentState.markDirty();
        }
    }

    @Override
    public void syncWithAll(MinecraftServer server) {
        if (world != null) {
            PortalStorageManager.syncToAll(world);
        }
    }
}
