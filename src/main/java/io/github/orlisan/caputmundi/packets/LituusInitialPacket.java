package io.github.orlisan.caputmundi.packets;

import io.github.orlisan.caputmundi.CaputMundi;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;

import java.util.function.Function;

public record LituusInitialPacket(boolean hasAquila, String aquilaName, double aquilaHealt) implements CustomPacketPayload{
    public LituusInitialPacket(FriendlyByteBuf buf) {
        this(buf.readBoolean(), buf.readUtf(), buf.readDouble());
    }

    @Override
    public @NonNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
    public static final Type<LituusInitialPacket> TYPE = new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(CaputMundi.MOD_ID, "lituus_packet"));
    void encode(FriendlyByteBuf buf) {
        buf.writeBoolean(hasAquila).writeUtf(aquilaName).writeDouble(aquilaHealt);
    }
    public static final StreamCodec<FriendlyByteBuf, LituusInitialPacket> CODEC =
            StreamCodec.ofMember(LituusInitialPacket::encode, LituusInitialPacket::new);
}
