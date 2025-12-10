package dev.custom.portals.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.datafixer.DataFixTypes;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;
import net.minecraft.world.PersistentStateType;

import java.util.List;

public class PortalPersistentState extends PersistentState {
    private static final String DATA_KEY = "customportals_portals";
    
    private PortalRegistry portalRegistry;

    private PortalPersistentState() {
        this.portalRegistry = new PortalRegistry();
    }

    public PortalRegistry getPortalRegistry() {
        return portalRegistry;
    }

    public void setPortalRegistry(PortalRegistry registry) {
        this.portalRegistry = registry;
        this.markDirty();
    }

    public static PortalPersistentState readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        PortalPersistentState state = new PortalPersistentState();
        List<CustomPortal> portals = PortalDataSerializer.readPortals(nbt);
        for (CustomPortal portal : portals) {
            state.portalRegistry.register(portal);
        }
        return state;
    }

    private static final Codec<List<CustomPortal>> PORTAL_LIST_CODEC = CustomPortal.CODEC.listOf();
    
    public static final Codec<PortalPersistentState> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        PORTAL_LIST_CODEC.fieldOf("portals").forGetter(state -> state.portalRegistry.getPortals())
    ).apply(instance, portals -> {
        PortalPersistentState state = new PortalPersistentState();
        for (CustomPortal portal : portals) {
            state.portalRegistry.register(portal);
        }
        return state;
    }));

    public static final PersistentStateType<PortalPersistentState> TYPE = new PersistentStateType<PortalPersistentState>(
        DATA_KEY,
        PortalPersistentState::new,
        CODEC,
        DataFixTypes.LEVEL
    );

    public static PortalPersistentState getOrCreate(ServerWorld world) {
        PersistentStateManager manager = world.getPersistentStateManager();
        return manager.getOrCreate(TYPE, DATA_KEY);
    }
}
