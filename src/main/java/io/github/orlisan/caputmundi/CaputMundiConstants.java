package io.github.orlisan.caputmundi;

import com.geckolib.constant.DataTickets;
import com.geckolib.constant.dataticket.DataTicket;
import com.google.common.reflect.TypeToken;
import io.github.orlisan.caputmundi.entities.AquilaEntity;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.resources.Identifier;

public final class CaputMundiConstants {
    public static final DataTicket<Boolean> HAS_COLLAR =
            DataTickets.create("has_collar", new TypeToken<>() {
            });
    public static final AttachmentType<AquilaEntity> AQUILA_VISUALIZZATA =
            AttachmentRegistry.create(Identifier.fromNamespaceAndPath(CaputMundi.MOD_ID, "aquila_visualizzata"));
    public static final int PACKET_STRING_MAX_LENGTH = 32767;
}
