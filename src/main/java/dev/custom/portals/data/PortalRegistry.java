package dev.custom.portals.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import net.minecraft.util.math.BlockPos;

public class PortalRegistry {

    private List<CustomPortal> portals;
    private Map<String, CustomPortal> portalPositions;

    public PortalRegistry() {
        portals = new ArrayList<CustomPortal>();
        portalPositions = new HashMap<String, CustomPortal>();
    }

    private static String getPortalPositionKey(String dimensionId, BlockPos pos) {
        return dimensionId + "|" + pos.asLong();
    }
    
    public void register(CustomPortal portal) {
        tryWithAll(portal);
        for (BlockPos blockPos : portal.getPortalBlocks()) {
            portalPositions.put(getPortalPositionKey(portal.getDimensionId(), blockPos), portal);
        }
        portals.add(portal);
    }

    public void unregister(CustomPortal portal) {
        portals.remove(portal);
        if (portal.hasLinked()) {
            tryWithAll(portal.getLinked());
        }
        for (BlockPos blockPos : portal.getPortalBlocks()) {
            portalPositions.remove(getPortalPositionKey(portal.getDimensionId(), blockPos));
        }
    }

    public void tryWithAll(CustomPortal portal) {
        portal.setLinked(null);
        for (CustomPortal p : portals) {
            portal.tryLink(p);
        }
    }

    public void refreshPortals() {
        for (CustomPortal portal : portals) {
            tryWithAll(portal);
        }
    }

    public List<CustomPortal> getPortals() { return portals; }

    //public void clear() { portals.clear(); }

    public CustomPortal getPortalFromPos(String dimensionId, BlockPos pos) {
        if (dimensionId == null) {
            return null;
        }
        return portalPositions.get(getPortalPositionKey(dimensionId, pos));
    }

    /**
     * Legacy fallback for callers that do not have world context. Returns a portal only
     * when the position is unique across all dimensions; otherwise returns null to avoid
     * routing entities through the wrong dimension's portal.
     */
    public CustomPortal getPortalFromPos(BlockPos pos) {
        CustomPortal matchedPortal = null;
        for (CustomPortal portal : portals) {
            if (portal.getPortalBlocks().contains(pos)) {
                if (matchedPortal != null) {
                    return null;
                }
                matchedPortal = portal;
            }
        }
        return matchedPortal;
    }
}