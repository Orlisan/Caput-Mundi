package io.github.orlisan.caputmundi.packets;

import com.mojang.authlib.yggdrasil.response.FriendsListResponse;
import io.github.orlisan.caputmundi.CaputMundi;
import io.github.orlisan.caputmundi.CaputMundiConstants;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

//Non so come serializzare i records
public record AquilaVistaMobsPacket(List<String> names, List<Double> xs,
                                    List<Double> ys) implements CustomPacketPayload {

    @Override
    public @NotNull Type<? extends @NotNull CustomPacketPayload> type() {
        return TYPE;
    }

    public AquilaVistaMobsPacket(FriendlyByteBuf buf) {
        List<String> nomi = new ArrayList<>();
        for (String str = buf.readUtf(); !str.equals("FINE_NOMI"); str = buf.readUtf()) {
            nomi.add(str);
        }
        List<Double> xs = new ArrayList<>();
        for (double i = buf.readDouble(); !(i == Double.MAX_VALUE); i = buf.readDouble()) {
            xs.add(i);
        }
        List<Double> ys = new ArrayList<>();
        for (double i = buf.readDouble(); !(i == Double.MIN_VALUE); i = buf.readDouble()) {
            ys.add(i);
        }
        this(nomi, xs, ys);
    }

    public void encode(FriendlyByteBuf buf) {
        for (String name : names) {
            buf.writeUtf(name);
        }
        buf.writeUtf("FINE_NOMI");
        for (double x : this.xs) {
            buf.writeDouble(x);
        }
        buf.writeDouble(Double.MAX_VALUE);
        for (double y : this.ys) {
            buf.writeDouble(y);
        }
        buf.writeDouble(Double.MIN_VALUE);
    }

    public static final StreamCodec<FriendlyByteBuf, AquilaVistaMobsPacket> CODEC =
            StreamCodec.ofMember(AquilaVistaMobsPacket::encode, AquilaVistaMobsPacket::new);

    public static final Type<@NotNull AquilaVistaMobsPacket> TYPE = new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(CaputMundi.MOD_ID, "aquila_vista_mobs_packet"));

}
