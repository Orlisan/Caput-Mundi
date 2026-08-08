package io.github.orlisan.caputmundi.packets;

import io.github.orlisan.caputmundi.CaputMundi;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type;
import net.minecraft.resources.Identifier;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("NullableProblems")
public record AquilaVistaPacket(ArrayList<ArrayList<String>> blockIds) implements CustomPacketPayload {
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

  /*  ic void write(FriendlyByteBuf buf) {
        for(String blockId: blockIds) {
            buf.writeUtf(blockId);
        }
    }*/

    public static final Type<AquilaVistaPacket> TYPE = new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(CaputMundi.MOD_ID, "aquila_vista_packet"));
    static StreamCodec<FriendlyByteBuf, ArrayList<String>> CODEC2 =
            ByteBufCodecs.collection(java.util.ArrayList::new, ByteBufCodecs.stringUtf8(32767));
    public static final StreamCodec<FriendlyByteBuf, AquilaVistaPacket> CODEC =
            ByteBufCodecs.collection(java.util.ArrayList::new, CODEC2).map(
                    AquilaVistaPacket::new,
                    AquilaVistaPacket::blockIds
            ).cast();
}
