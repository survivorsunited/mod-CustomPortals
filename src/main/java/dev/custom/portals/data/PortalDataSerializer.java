package dev.custom.portals.data;

import java.util.Collections;
import java.util.List;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtOps;

public final class PortalDataSerializer {
    private static final String PORTALS_KEY = "portals";
    private static final Codec<List<CustomPortal>> PORTAL_LIST_CODEC = CustomPortal.CODEC.listOf();

    private PortalDataSerializer() {}

    public static List<CustomPortal> readPortals(NbtCompound nbt) {
        if (nbt == null || !nbt.contains(PORTALS_KEY)) {
            return Collections.emptyList();
        }
        NbtElement element = nbt.get(PORTALS_KEY);
        if (!(element instanceof NbtList portalList)) {
            return Collections.emptyList();
        }
        DataResult<List<CustomPortal>> decoded = PORTAL_LIST_CODEC.parse(NbtOps.INSTANCE, portalList);
        return decoded.result().orElseGet(Collections::emptyList);
    }

    public static void writePortals(NbtCompound nbt, List<CustomPortal> portals) {
        if (nbt == null) {
            return;
        }
        DataResult<NbtElement> encoded = PORTAL_LIST_CODEC.encodeStart(NbtOps.INSTANCE, portals);
        encoded.result().ifPresent(element -> nbt.put(PORTALS_KEY, element));
    }

    public static NbtCompound createNbt(List<CustomPortal> portals) {
        NbtCompound nbt = new NbtCompound();
        writePortals(nbt, portals);
        return nbt;
    }
}

