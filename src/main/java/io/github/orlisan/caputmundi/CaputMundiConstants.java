package io.github.orlisan.caputmundi;

import com.geckolib.constant.DataTickets;
import com.geckolib.constant.dataticket.DataTicket;
import com.google.common.reflect.TypeToken;
import io.github.orlisan.caputmundi.entities.AquilaEntity;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.gamerules.GameRule;
import net.minecraft.world.level.gamerules.GameRuleCategory;

import java.util.List;

public final class CaputMundiConstants {
    public static final DataTicket<Boolean> HAS_COLLAR =
            DataTickets.create("has_collar", new TypeToken<>() {
            });
    public static final DataTicket<Boolean> HAS_ARMOR =
            DataTickets.create("has_armor", new TypeToken<>() {
            });
    public static final AttachmentType<AquilaEntity> AQUILA_VISUALIZZATA =
            AttachmentRegistry.create(Identifier.fromNamespaceAndPath(CaputMundi.MOD_ID, "aquila_visualizzata"));
    public static final int PACKET_STRING_MAX_LENGTH = 32767;

    public static final List<Component> SPLASHES_ROMANI = List.of(
            Component.literal("Veni, vidi, vici").withStyle(Style.EMPTY.withColor(0xC00000)),
            Component.literal("Alea iacta est").withStyle(Style.EMPTY.withColor(0x660033)),
            Component.literal("Memento mori").withStyle(Style.EMPTY.withColor(0x555555)),
            Component.literal("Carpe diem").withStyle(Style.EMPTY.withColor(0xFFFFFF))
    );
    public static GameRule<Integer> AQUILA_VIEW_UPDATE_TICKS/* = registerInteger("aquila_view_update_ticks", GameRuleCategory.MOBS, 20, 0)*/;
    private CaputMundiConstants(){}
    static void register() {
        AQUILA_VIEW_UPDATE_TICKS = GameRuleBuilder.forInteger(20).category(GameRuleCategory.MOBS).minValue(1).buildAndRegister(Identifier.fromNamespaceAndPath(CaputMundi.MOD_ID, "aquila_view_update_ticks"));
    }
}
