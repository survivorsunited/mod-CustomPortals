package dev.custom.portals.registry;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.particle.v1.FabricSpriteProvider;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.PortalParticle;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.util.math.random.Random;

public class CPParticles21_9 {

    @Environment(EnvType.CLIENT)
    public static void registerFactoryRegistries() {
        ParticleFactoryRegistry.getInstance().register(CPParticlesConstants.BLACK_PORTAL_PARTICLE, BlackPortalParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(CPParticlesConstants.BLUE_PORTAL_PARTICLE, BluePortalParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(CPParticlesConstants.BROWN_PORTAL_PARTICLE, BrownPortalParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(CPParticlesConstants.CYAN_PORTAL_PARTICLE, CyanPortalParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(CPParticlesConstants.GRAY_PORTAL_PARTICLE, GrayPortalParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(CPParticlesConstants.GREEN_PORTAL_PARTICLE, GreenPortalParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(CPParticlesConstants.LIGHT_BLUE_PORTAL_PARTICLE, LightBluePortalParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(CPParticlesConstants.LIGHT_GRAY_PORTAL_PARTICLE, LightGrayPortalParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(CPParticlesConstants.LIME_PORTAL_PARTICLE, LimePortalParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(CPParticlesConstants.MAGENTA_PORTAL_PARTICLE, MagentaPortalParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(CPParticlesConstants.ORANGE_PORTAL_PARTICLE, OrangePortalParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(CPParticlesConstants.PINK_PORTAL_PARTICLE, PinkPortalParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(CPParticlesConstants.RED_PORTAL_PARTICLE, RedPortalParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(CPParticlesConstants.WHITE_PORTAL_PARTICLE, WhitePortalParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(CPParticlesConstants.YELLOW_PORTAL_PARTICLE, YellowPortalParticle.Factory::new);
    }

    @Environment(EnvType.CLIENT)
    static class BlackPortalParticle extends PortalParticle {
        protected BlackPortalParticle(ClientWorld clientWorld, double d, double e, double f, double g, double h, double i, SpriteProvider sprites) {
            super(clientWorld, d, e, f, g, h, i, sprites.getSprite(clientWorld.random));
            this.red = this.green = this.blue = 0.0F;
        }
        @Environment(EnvType.CLIENT)
        public static class Factory implements ParticleFactory<SimpleParticleType> {
            private final FabricSpriteProvider sprites;
            public Factory(FabricSpriteProvider sprites) { this.sprites = sprites; }
            public Particle createParticle(SimpleParticleType type, ClientWorld world, double x, double y, double z, double vX, double vY, double vZ) {
                return createParticle(type, world, x, y, z, vX, vY, vZ, world.getRandom());
            }
            public Particle createParticle(SimpleParticleType type, ClientWorld world, double x, double y, double z, double vX, double vY, double vZ, Random random) {
                return new BlackPortalParticle(world, x, y, z, vX, vY, vZ, sprites);
            }
        }
    }

    @Environment(EnvType.CLIENT)
    static class BluePortalParticle extends PortalParticle {
        protected BluePortalParticle(ClientWorld clientWorld, double d, double e, double f, double g, double h, double i, SpriteProvider sprites) {
            super(clientWorld, d, e, f, g, h, i, sprites.getSprite(clientWorld.random));
            float j = this.random.nextFloat() * 0.6F + 0.4F;
            this.red = 0.2F * j; this.green = 0.2F * j; this.blue = 1.0F * j;
        }
        @Environment(EnvType.CLIENT)
        public static class Factory implements ParticleFactory<SimpleParticleType> {
            private final FabricSpriteProvider sprites;
            public Factory(FabricSpriteProvider sprites) { this.sprites = sprites; }
            public Particle createParticle(SimpleParticleType type, ClientWorld world, double x, double y, double z, double vX, double vY, double vZ) {
                return createParticle(type, world, x, y, z, vX, vY, vZ, world.getRandom());
            }
            public Particle createParticle(SimpleParticleType type, ClientWorld world, double x, double y, double z, double vX, double vY, double vZ, Random random) {
                return new BluePortalParticle(world, x, y, z, vX, vY, vZ, sprites);
            }
        }
    }

    @Environment(EnvType.CLIENT)
    static class BrownPortalParticle extends PortalParticle {
        protected BrownPortalParticle(ClientWorld clientWorld, double d, double e, double f, double g, double h, double i, SpriteProvider sprites) {
            super(clientWorld, d, e, f, g, h, i, sprites.getSprite(clientWorld.random));
            float j = this.random.nextFloat() * 0.6F + 0.4F;
            this.red = 0.575F * j; this.green = 0.45F * j; this.blue = 0.325F * j;
        }
        @Environment(EnvType.CLIENT)
        public static class Factory implements ParticleFactory<SimpleParticleType> {
            private final FabricSpriteProvider sprites;
            public Factory(FabricSpriteProvider sprites) { this.sprites = sprites; }
            public Particle createParticle(SimpleParticleType type, ClientWorld world, double x, double y, double z, double vX, double vY, double vZ) {
                return createParticle(type, world, x, y, z, vX, vY, vZ, world.getRandom());
            }
            public Particle createParticle(SimpleParticleType type, ClientWorld world, double x, double y, double z, double vX, double vY, double vZ, Random random) {
                return new BrownPortalParticle(world, x, y, z, vX, vY, vZ, sprites);
            }
        }
    }

    @Environment(EnvType.CLIENT)
    static class CyanPortalParticle extends PortalParticle {
        protected CyanPortalParticle(ClientWorld clientWorld, double d, double e, double f, double g, double h, double i, SpriteProvider sprites) {
            super(clientWorld, d, e, f, g, h, i, sprites.getSprite(clientWorld.random));
            float j = this.random.nextFloat() * 0.6F + 0.4F;
            this.blue = 0.8F * j; this.red = 0.2F * j; this.green = 0.65F * j;
        }
        @Environment(EnvType.CLIENT)
        public static class Factory implements ParticleFactory<SimpleParticleType> {
            private final FabricSpriteProvider sprites;
            public Factory(FabricSpriteProvider sprites) { this.sprites = sprites; }
            public Particle createParticle(SimpleParticleType type, ClientWorld world, double x, double y, double z, double vX, double vY, double vZ) {
                return createParticle(type, world, x, y, z, vX, vY, vZ, world.getRandom());
            }
            public Particle createParticle(SimpleParticleType type, ClientWorld world, double x, double y, double z, double vX, double vY, double vZ, Random random) {
                return new CyanPortalParticle(world, x, y, z, vX, vY, vZ, sprites);
            }
        }
    }

    @Environment(EnvType.CLIENT)
    static class GrayPortalParticle extends PortalParticle {
        protected GrayPortalParticle(ClientWorld clientWorld, double d, double e, double f, double g, double h, double i, SpriteProvider sprites) {
            super(clientWorld, d, e, f, g, h, i, sprites.getSprite(clientWorld.random));
            float j = this.random.nextFloat() * 0.6F + 0.4F;
            this.red = this.green = this.blue = 0.5F * j;
        }
        @Environment(EnvType.CLIENT)
        public static class Factory implements ParticleFactory<SimpleParticleType> {
            private final FabricSpriteProvider sprites;
            public Factory(FabricSpriteProvider sprites) { this.sprites = sprites; }
            public Particle createParticle(SimpleParticleType type, ClientWorld world, double x, double y, double z, double vX, double vY, double vZ) {
                return createParticle(type, world, x, y, z, vX, vY, vZ, world.getRandom());
            }
            public Particle createParticle(SimpleParticleType type, ClientWorld world, double x, double y, double z, double vX, double vY, double vZ, Random random) {
                return new GrayPortalParticle(world, x, y, z, vX, vY, vZ, sprites);
            }
        }
    }

    @Environment(EnvType.CLIENT)
    static class GreenPortalParticle extends PortalParticle {
        protected GreenPortalParticle(ClientWorld clientWorld, double d, double e, double f, double g, double h, double i, SpriteProvider sprites) {
            super(clientWorld, d, e, f, g, h, i, sprites.getSprite(clientWorld.random));
            float j = this.random.nextFloat() * 0.6F + 0.4F;
            this.red = 0.2F * j; this.green = 0.5F * j; this.blue = 0.2F * j;
        }
        @Environment(EnvType.CLIENT)
        public static class Factory implements ParticleFactory<SimpleParticleType> {
            private final FabricSpriteProvider sprites;
            public Factory(FabricSpriteProvider sprites) { this.sprites = sprites; }
            public Particle createParticle(SimpleParticleType type, ClientWorld world, double x, double y, double z, double vX, double vY, double vZ) {
                return createParticle(type, world, x, y, z, vX, vY, vZ, world.getRandom());
            }
            public Particle createParticle(SimpleParticleType type, ClientWorld world, double x, double y, double z, double vX, double vY, double vZ, Random random) {
                return new GreenPortalParticle(world, x, y, z, vX, vY, vZ, sprites);
            }
        }
    }

    @Environment(EnvType.CLIENT)
    static class LightBluePortalParticle extends PortalParticle {
        protected LightBluePortalParticle(ClientWorld clientWorld, double d, double e, double f, double g, double h, double i, SpriteProvider sprites) {
            super(clientWorld, d, e, f, g, h, i, sprites.getSprite(clientWorld.random));
            float j = this.random.nextFloat() * 0.6F + 0.4F;
            this.red = 0.6F * j; this.blue = 1.0F * j; this.green = 0.7F * j;
        }
        @Environment(EnvType.CLIENT)
        public static class Factory implements ParticleFactory<SimpleParticleType> {
            private final FabricSpriteProvider sprites;
            public Factory(FabricSpriteProvider sprites) { this.sprites = sprites; }
            public Particle createParticle(SimpleParticleType type, ClientWorld world, double x, double y, double z, double vX, double vY, double vZ) {
                return createParticle(type, world, x, y, z, vX, vY, vZ, world.getRandom());
            }
            public Particle createParticle(SimpleParticleType type, ClientWorld world, double x, double y, double z, double vX, double vY, double vZ, Random random) {
                return new LightBluePortalParticle(world, x, y, z, vX, vY, vZ, sprites);
            }
        }
    }

    @Environment(EnvType.CLIENT)
    static class LightGrayPortalParticle extends PortalParticle {
        protected LightGrayPortalParticle(ClientWorld clientWorld, double d, double e, double f, double g, double h, double i, SpriteProvider sprites) {
            super(clientWorld, d, e, f, g, h, i, sprites.getSprite(clientWorld.random));
            float j = this.random.nextFloat() * 0.6F + 0.4F;
            this.red = this.green = this.blue = 0.7F * j;
        }
        @Environment(EnvType.CLIENT)
        public static class Factory implements ParticleFactory<SimpleParticleType> {
            private final FabricSpriteProvider sprites;
            public Factory(FabricSpriteProvider sprites) { this.sprites = sprites; }
            public Particle createParticle(SimpleParticleType type, ClientWorld world, double x, double y, double z, double vX, double vY, double vZ) {
                return createParticle(type, world, x, y, z, vX, vY, vZ, world.getRandom());
            }
            public Particle createParticle(SimpleParticleType type, ClientWorld world, double x, double y, double z, double vX, double vY, double vZ, Random random) {
                return new LightGrayPortalParticle(world, x, y, z, vX, vY, vZ, sprites);
            }
        }
    }

    @Environment(EnvType.CLIENT)
    static class LimePortalParticle extends PortalParticle {
        protected LimePortalParticle(ClientWorld clientWorld, double d, double e, double f, double g, double h, double i, SpriteProvider sprites) {
            super(clientWorld, d, e, f, g, h, i, sprites.getSprite(clientWorld.random));
            float j = this.random.nextFloat() * 0.6F + 0.4F;
            this.red = 0.5F * j; this.green = 1.0F * j; this.blue = 0.3F * j;
        }
        @Environment(EnvType.CLIENT)
        public static class Factory implements ParticleFactory<SimpleParticleType> {
            private final FabricSpriteProvider sprites;
            public Factory(FabricSpriteProvider sprites) { this.sprites = sprites; }
            public Particle createParticle(SimpleParticleType type, ClientWorld world, double x, double y, double z, double vX, double vY, double vZ) {
                return createParticle(type, world, x, y, z, vX, vY, vZ, world.getRandom());
            }
            public Particle createParticle(SimpleParticleType type, ClientWorld world, double x, double y, double z, double vX, double vY, double vZ, Random random) {
                return new LimePortalParticle(world, x, y, z, vX, vY, vZ, sprites);
            }
        }
    }

    @Environment(EnvType.CLIENT)
    static class MagentaPortalParticle extends PortalParticle {
        protected MagentaPortalParticle(ClientWorld clientWorld, double d, double e, double f, double g, double h, double i, SpriteProvider sprites) {
            super(clientWorld, d, e, f, g, h, i, sprites.getSprite(clientWorld.random));
            float j = this.random.nextFloat() * 0.6F + 0.4F;
            this.red = 1.0F * j; this.green = 0.4F * j; this.blue = 1.0F * j;
        }
        @Environment(EnvType.CLIENT)
        public static class Factory implements ParticleFactory<SimpleParticleType> {
            private final FabricSpriteProvider sprites;
            public Factory(FabricSpriteProvider sprites) { this.sprites = sprites; }
            public Particle createParticle(SimpleParticleType type, ClientWorld world, double x, double y, double z, double vX, double vY, double vZ) {
                return createParticle(type, world, x, y, z, vX, vY, vZ, world.getRandom());
            }
            public Particle createParticle(SimpleParticleType type, ClientWorld world, double x, double y, double z, double vX, double vY, double vZ, Random random) {
                return new MagentaPortalParticle(world, x, y, z, vX, vY, vZ, sprites);
            }
        }
    }

    @Environment(EnvType.CLIENT)
    static class OrangePortalParticle extends PortalParticle {
        protected OrangePortalParticle(ClientWorld clientWorld, double d, double e, double f, double g, double h, double i, SpriteProvider sprites) {
            super(clientWorld, d, e, f, g, h, i, sprites.getSprite(clientWorld.random));
            float j = this.random.nextFloat() * 0.6F + 0.4F;
            this.red = 1.0F * j; this.green = 0.7F * j; this.blue = 0.2F * j;
        }
        @Environment(EnvType.CLIENT)
        public static class Factory implements ParticleFactory<SimpleParticleType> {
            private final FabricSpriteProvider sprites;
            public Factory(FabricSpriteProvider sprites) { this.sprites = sprites; }
            public Particle createParticle(SimpleParticleType type, ClientWorld world, double x, double y, double z, double vX, double vY, double vZ) {
                return createParticle(type, world, x, y, z, vX, vY, vZ, world.getRandom());
            }
            public Particle createParticle(SimpleParticleType type, ClientWorld world, double x, double y, double z, double vX, double vY, double vZ, Random random) {
                return new OrangePortalParticle(world, x, y, z, vX, vY, vZ, sprites);
            }
        }
    }

    @Environment(EnvType.CLIENT)
    static class PinkPortalParticle extends PortalParticle {
        protected PinkPortalParticle(ClientWorld clientWorld, double d, double e, double f, double g, double h, double i, SpriteProvider sprites) {
            super(clientWorld, d, e, f, g, h, i, sprites.getSprite(clientWorld.random));
            float j = this.random.nextFloat() * 0.6F + 0.4F;
            this.red = 1.0F * j; this.green = 0.6F * j; this.blue = 0.9F * j;
        }
        @Environment(EnvType.CLIENT)
        public static class Factory implements ParticleFactory<SimpleParticleType> {
            private final FabricSpriteProvider sprites;
            public Factory(FabricSpriteProvider sprites) { this.sprites = sprites; }
            public Particle createParticle(SimpleParticleType type, ClientWorld world, double x, double y, double z, double vX, double vY, double vZ) {
                return createParticle(type, world, x, y, z, vX, vY, vZ, world.getRandom());
            }
            public Particle createParticle(SimpleParticleType type, ClientWorld world, double x, double y, double z, double vX, double vY, double vZ, Random random) {
                return new PinkPortalParticle(world, x, y, z, vX, vY, vZ, sprites);
            }
        }
    }

    @Environment(EnvType.CLIENT)
    static class RedPortalParticle extends PortalParticle {
        protected RedPortalParticle(ClientWorld clientWorld, double d, double e, double f, double g, double h, double i, SpriteProvider sprites) {
            super(clientWorld, d, e, f, g, h, i, sprites.getSprite(clientWorld.random));
            float j = this.random.nextFloat() * 0.6F + 0.4F;
            this.red = 1.0F * j; this.green = 0.2F * j; this.blue = 0.2F * j;
        }
        @Environment(EnvType.CLIENT)
        public static class Factory implements ParticleFactory<SimpleParticleType> {
            private final FabricSpriteProvider sprites;
            public Factory(FabricSpriteProvider sprites) { this.sprites = sprites; }
            public Particle createParticle(SimpleParticleType type, ClientWorld world, double x, double y, double z, double vX, double vY, double vZ) {
                return createParticle(type, world, x, y, z, vX, vY, vZ, world.getRandom());
            }
            public Particle createParticle(SimpleParticleType type, ClientWorld world, double x, double y, double z, double vX, double vY, double vZ, Random random) {
                return new RedPortalParticle(world, x, y, z, vX, vY, vZ, sprites);
            }
        }
    }

    @Environment(EnvType.CLIENT)
    static class WhitePortalParticle extends PortalParticle {
        protected WhitePortalParticle(ClientWorld clientWorld, double d, double e, double f, double g, double h, double i, SpriteProvider sprites) {
            super(clientWorld, d, e, f, g, h, i, sprites.getSprite(clientWorld.random));
            float j = this.random.nextFloat() * 0.6F + 0.4F;
            this.red = this.green = this.blue = 1.0F * j;
        }
        @Environment(EnvType.CLIENT)
        public static class Factory implements ParticleFactory<SimpleParticleType> {
            private final FabricSpriteProvider sprites;
            public Factory(FabricSpriteProvider sprites) { this.sprites = sprites; }
            public Particle createParticle(SimpleParticleType type, ClientWorld world, double x, double y, double z, double vX, double vY, double vZ) {
                return createParticle(type, world, x, y, z, vX, vY, vZ, world.getRandom());
            }
            public Particle createParticle(SimpleParticleType type, ClientWorld world, double x, double y, double z, double vX, double vY, double vZ, Random random) {
                return new WhitePortalParticle(world, x, y, z, vX, vY, vZ, sprites);
            }
        }
    }

    @Environment(EnvType.CLIENT)
    static class YellowPortalParticle extends PortalParticle {
        protected YellowPortalParticle(ClientWorld clientWorld, double d, double e, double f, double g, double h, double i, SpriteProvider sprites) {
            super(clientWorld, d, e, f, g, h, i, sprites.getSprite(clientWorld.random));
            float j = this.random.nextFloat() * 0.6F + 0.4F;
            this.red = 1.0F * j; this.green = 1.0F * j; this.blue = 0.2F * j;
        }
        @Environment(EnvType.CLIENT)
        public static class Factory implements ParticleFactory<SimpleParticleType> {
            private final FabricSpriteProvider sprites;
            public Factory(FabricSpriteProvider sprites) { this.sprites = sprites; }
            public Particle createParticle(SimpleParticleType type, ClientWorld world, double x, double y, double z, double vX, double vY, double vZ) {
                return createParticle(type, world, x, y, z, vX, vY, vZ, world.getRandom());
            }
            public Particle createParticle(SimpleParticleType type, ClientWorld world, double x, double y, double z, double vX, double vY, double vZ, Random random) {
                return new YellowPortalParticle(world, x, y, z, vX, vY, vZ, sprites);
            }
        }
    }
}
