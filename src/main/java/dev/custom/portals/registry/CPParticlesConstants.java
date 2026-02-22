package dev.custom.portals.registry;

import dev.custom.portals.CustomPortals;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class CPParticlesConstants {

	public static final SimpleParticleType BLACK_PORTAL_PARTICLE = FabricParticleTypes.simple();
	public static final SimpleParticleType BLUE_PORTAL_PARTICLE = FabricParticleTypes.simple();
	public static final SimpleParticleType BROWN_PORTAL_PARTICLE = FabricParticleTypes.simple();
	public static final SimpleParticleType CYAN_PORTAL_PARTICLE = FabricParticleTypes.simple();
	public static final SimpleParticleType GRAY_PORTAL_PARTICLE = FabricParticleTypes.simple();
	public static final SimpleParticleType GREEN_PORTAL_PARTICLE = FabricParticleTypes.simple();
	public static final SimpleParticleType LIGHT_BLUE_PORTAL_PARTICLE = FabricParticleTypes.simple();
	public static final SimpleParticleType LIGHT_GRAY_PORTAL_PARTICLE = FabricParticleTypes.simple();
	public static final SimpleParticleType LIME_PORTAL_PARTICLE = FabricParticleTypes.simple();
	public static final SimpleParticleType MAGENTA_PORTAL_PARTICLE = FabricParticleTypes.simple();
	public static final SimpleParticleType ORANGE_PORTAL_PARTICLE = FabricParticleTypes.simple();
	public static final SimpleParticleType PINK_PORTAL_PARTICLE = FabricParticleTypes.simple();
	public static final SimpleParticleType RED_PORTAL_PARTICLE = FabricParticleTypes.simple();
	public static final SimpleParticleType WHITE_PORTAL_PARTICLE = FabricParticleTypes.simple();
	public static final SimpleParticleType YELLOW_PORTAL_PARTICLE = FabricParticleTypes.simple();

	public static void registerParticles() {
		Registry.register(Registries.PARTICLE_TYPE, Identifier.of(CustomPortals.MOD_ID, "black_portal_particle"), BLACK_PORTAL_PARTICLE);
		Registry.register(Registries.PARTICLE_TYPE, Identifier.of(CustomPortals.MOD_ID, "blue_portal_particle"), BLUE_PORTAL_PARTICLE);
		Registry.register(Registries.PARTICLE_TYPE, Identifier.of(CustomPortals.MOD_ID, "brown_portal_particle"), BROWN_PORTAL_PARTICLE);
		Registry.register(Registries.PARTICLE_TYPE, Identifier.of(CustomPortals.MOD_ID, "cyan_portal_particle"), CYAN_PORTAL_PARTICLE);
		Registry.register(Registries.PARTICLE_TYPE, Identifier.of(CustomPortals.MOD_ID, "gray_portal_particle"), GRAY_PORTAL_PARTICLE);
		Registry.register(Registries.PARTICLE_TYPE, Identifier.of(CustomPortals.MOD_ID, "green_portal_particle"), GREEN_PORTAL_PARTICLE);
		Registry.register(Registries.PARTICLE_TYPE, Identifier.of(CustomPortals.MOD_ID, "light_blue_portal_particle"), LIGHT_BLUE_PORTAL_PARTICLE);
		Registry.register(Registries.PARTICLE_TYPE, Identifier.of(CustomPortals.MOD_ID, "light_gray_portal_particle"), LIGHT_GRAY_PORTAL_PARTICLE);
		Registry.register(Registries.PARTICLE_TYPE, Identifier.of(CustomPortals.MOD_ID, "lime_portal_particle"), LIME_PORTAL_PARTICLE);
		Registry.register(Registries.PARTICLE_TYPE, Identifier.of(CustomPortals.MOD_ID, "magenta_portal_particle"), MAGENTA_PORTAL_PARTICLE);
		Registry.register(Registries.PARTICLE_TYPE, Identifier.of(CustomPortals.MOD_ID, "orange_portal_particle"), ORANGE_PORTAL_PARTICLE);
		Registry.register(Registries.PARTICLE_TYPE, Identifier.of(CustomPortals.MOD_ID, "pink_portal_particle"), PINK_PORTAL_PARTICLE);
		Registry.register(Registries.PARTICLE_TYPE, Identifier.of(CustomPortals.MOD_ID, "red_portal_particle"), RED_PORTAL_PARTICLE);
		Registry.register(Registries.PARTICLE_TYPE, Identifier.of(CustomPortals.MOD_ID, "white_portal_particle"), WHITE_PORTAL_PARTICLE);
		Registry.register(Registries.PARTICLE_TYPE, Identifier.of(CustomPortals.MOD_ID, "yellow_portal_particle"), YELLOW_PORTAL_PARTICLE);
	}
}
