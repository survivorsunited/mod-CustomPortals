package dev.custom.portals.registry;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/**
 * Dispatches particle factory registration to the version-specific implementation
 * (CPParticles21_9 for 1.21.9+, CPParticles for older versions).
 */
public final class CPParticleFactoryRegistration {

    private static final String CLASS_21_9 = "dev.custom.portals.registry.CPParticles21_9";
    private static final String CLASS_LEGACY = "dev.custom.portals.registry.CPParticles";
    private static final String METHOD = "registerFactoryRegistries";

    private CPParticleFactoryRegistration() {}

    @Environment(EnvType.CLIENT)
    public static void register() {
        try {
            Class.forName(CLASS_21_9).getMethod(METHOD).invoke(null);
            return;
        } catch (Throwable ignored) {
        }
        try {
            Class.forName(CLASS_LEGACY).getMethod(METHOD).invoke(null);
        } catch (Throwable t) {
            throw new RuntimeException("Failed to register portal particle factories", t);
        }
    }
}
