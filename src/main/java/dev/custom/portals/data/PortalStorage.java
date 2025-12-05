package dev.custom.portals.data;

import net.minecraft.world.World;

public final class PortalStorage {
    public BasePortalComponent get(World world) {
        return PortalStorageManager.get(world);
    }
}



