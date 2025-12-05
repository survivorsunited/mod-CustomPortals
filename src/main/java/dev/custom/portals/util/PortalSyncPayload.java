package dev.custom.portals.util;

import java.util.List;

import dev.custom.portals.CustomPortals;
import dev.custom.portals.data.CustomPortal;
import dev.custom.portals.data.PortalDataSerializer;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record PortalSyncPayload(List<CustomPortal> portals) implements CustomPayload {
    private static final Identifier PACKET_ID = Identifier.of(CustomPortals.MOD_ID, "portal_sync");
    public static final CustomPayload.Id<PortalSyncPayload> ID = new CustomPayload.Id<>(PACKET_ID);
    public static final PacketCodec<RegistryByteBuf, PortalSyncPayload> CODEC = new PacketCodec<>() {
        @Override
        public PortalSyncPayload decode(RegistryByteBuf buf) {
            return read(buf);
        }

        @Override
        public void encode(RegistryByteBuf buf, PortalSyncPayload value) {
            write(buf, value);
        }
    };

    private static void write(RegistryByteBuf buf, PortalSyncPayload payload) {
        NbtCompound nbt = PortalDataSerializer.createNbt(payload.portals());
        buf.writeNbt(nbt);
    }

    private static PortalSyncPayload read(RegistryByteBuf buf) {
        NbtCompound nbt = buf.readNbt();
        return new PortalSyncPayload(PortalDataSerializer.readPortals(nbt));
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}

