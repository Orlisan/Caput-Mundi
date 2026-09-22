package io.github.orlisan.caputmundi.packets;

import io.github.orlisan.caputmundi.CaputMundi;
import io.github.orlisan.caputmundi.CaputMundiConstants;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;

public record LituusPacket(boolean hasAquila, String aquilaName, double aquilaHealt,
                           String aquilaUUID) implements CustomPacketPayload {
    public LituusPacket(FriendlyByteBuf buf) {
        this(buf.readBoolean(), buf.readUtf(), buf.readDouble(), buf.readUtf());
    }

    public LituusPacket(String command, String uuid) {
        this(false, command, 0, uuid);
    }

    @Override
    public @NonNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static final Type<LituusPacket> TYPE = new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(CaputMundi.MOD_ID, "lituus_packet"));

    void encode(FriendlyByteBuf buf) {
        buf.writeBoolean(hasAquila).writeUtf(aquilaName).writeDouble(aquilaHealt).writeUtf(aquilaUUID);
    }

    public static final StreamCodec<FriendlyByteBuf, LituusPacket> CODEC =
            StreamCodec.ofMember(LituusPacket::encode, LituusPacket::new);
}
