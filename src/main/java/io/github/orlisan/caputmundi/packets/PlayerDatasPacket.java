package io.github.orlisan.caputmundi.packets;

import io.github.orlisan.caputmundi.CaputMundi;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
public record PlayerDatasPacket(String playerUUID, String playerName, float playerHealth, Vec3 playerPos, Vec2 playerPosInMap) implements CustomPacketPayload {
    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
    public static final Type<PlayerDatasPacket> TYPE = new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(CaputMundi.MOD_ID, "player_datas_packet"));
    void encode(FriendlyByteBuf buf) {
        buf.writeUtf(playerUUID);
        buf.writeUtf(playerName);
        buf.writeFloat(playerHealth);

        buf.writeDouble(playerPos.x);
        buf.writeDouble(playerPos.y);
        buf.writeDouble(playerPos.z);

        buf.writeFloat(playerPosInMap.x);
        buf.writeFloat(playerPosInMap.y);
    }
    PlayerDatasPacket(FriendlyByteBuf buf) {
        this(buf.readUtf(), buf.readUtf(), buf.readFloat(), new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble()), new Vec2(buf.readFloat(), buf.readFloat()));
    }
    public static final StreamCodec<FriendlyByteBuf, PlayerDatasPacket> CODEC =
            StreamCodec.ofMember(PlayerDatasPacket::encode, PlayerDatasPacket::new);
}
