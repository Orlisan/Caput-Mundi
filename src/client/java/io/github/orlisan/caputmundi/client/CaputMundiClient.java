package io.github.orlisan.caputmundi.client;

import io.github.orlisan.caputmundi.client.renderer.AquilaRenderer;
import io.github.orlisan.caputmundi.entities.CaputMundiEntities;
import io.github.orlisan.caputmundi.packets.AquilaVistaPacket;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.resources.Identifier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static io.github.orlisan.caputmundi.CaputMundi.LOGGER;

public class CaputMundiClient implements ClientModInitializer {
    StringBuilder vecchioSelected = new StringBuilder();
    public static boolean eraHardcore = false;
    public static List<List<Identifier>> vistaAquila = new ArrayList<>();
    boolean giaSettataVista = false;

    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(AquilaVistaPacket.TYPE, (packet, context) -> {
            context.client().execute(() -> {
                //  if (!giaSettataVista) {
                vistaAquila.clear();
                for (List<String> list : packet.blockIds()) {
                    List<Identifier> builder = new ArrayList<>();
                    for (String str : list) {
                        builder.add(Identifier.parse(str));
                        LOGGER.info("Blocco:{}", Identifier.parse(str));
                    }
                    Collections.reverse(builder);
                    vistaAquila.add(builder);
                }
                //    giaSettataVista = true;
                // }
            });
        });
        EntityRenderers.register(
                CaputMundiEntities.AQUILA,
                AquilaRenderer::new
        );
        //Builder per aggirare il voluto final delle lambda
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            if (client.level != null && client.level.getLevelData().isHardcore()) {
                LOGGER.info("JOIN HARDCORE CHIAMATO");
                eraHardcore = true;
                vecchioSelected.setLength(0);
                vecchioSelected.append(client.getLanguageManager().getSelected());
                client.getLanguageManager().setSelected("la_la");
                client.getLanguageManager().onResourceManagerReload(client.getResourceManager());
            }
        });
        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
            if (eraHardcore) {
                eraHardcore = false;
                LOGGER.info("Era in un mondo hardcore");
                client.getLanguageManager().setSelected(vecchioSelected.toString());
                vecchioSelected.setLength(0);
                client.getLanguageManager().onResourceManagerReload(client.getResourceManager());
            }
        });
        // This entrypoint is suitable for setting up client-specific logic, such as rendering.
    }

    public record coords2d(int x, int y) {
    }
}