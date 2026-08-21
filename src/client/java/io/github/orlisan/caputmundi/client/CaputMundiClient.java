package io.github.orlisan.caputmundi.client;

import io.github.orlisan.caputmundi.CaputMundi;
import io.github.orlisan.caputmundi.client.gui.LituusScreen;
import io.github.orlisan.caputmundi.client.renderer.AquilaRenderer;
import io.github.orlisan.caputmundi.entities.CaputMundiEntities;
import io.github.orlisan.caputmundi.gui.CaputMundiMenuTypes;
import io.github.orlisan.caputmundi.packets.AquilaVistaMobsPacket;
import io.github.orlisan.caputmundi.packets.AquilaVistaPacket;
import io.github.orlisan.caputmundi.packets.LituusInitialPacket;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.screens.MenuScreens;
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
    public static List<String> mobs;
    public static List<Double> xMobs;
    public static List<Double> yMobs;
    boolean giaSettataVista = false;
    public static final Identifier ZOMBIE_SPRITE = Identifier.fromNamespaceAndPath(CaputMundi.MOD_ID, "textures/gui/aquila_zombie_sprite.png");
    public static final Identifier CREEPER_SPRITE = Identifier.fromNamespaceAndPath(CaputMundi.MOD_ID, "textures/gui/aquila_creeper_sprite.png");
    public static final Identifier ENDERMAN_SPRITE = Identifier.fromNamespaceAndPath(CaputMundi.MOD_ID, "textures/gui/aquila_enderman_sprite.png");
    public static final Identifier SPIDER_SPRITE = Identifier.fromNamespaceAndPath(CaputMundi.MOD_ID, "textures/gui/aquila_spider_sprite.png");
    public static final Identifier CAVE_SPIDER_SPRITE = Identifier.fromNamespaceAndPath(CaputMundi.MOD_ID, "textures/gui/aquila_cave_spider_sprite.png");
    public static final Identifier SKELETON_SPRITE = Identifier.fromNamespaceAndPath(CaputMundi.MOD_ID, "textures/gui/aquila_skeleton_sprite.png");

    public static final Identifier LITUUS_OVERVIEW_SPRITE = Identifier.fromNamespaceAndPath(CaputMundi.MOD_ID, "textures/gui/lituus_overview.png");

    @Override
    public void onInitializeClient() {
        MenuScreens.register(CaputMundiMenuTypes.LITUUS_MENU, LituusScreen::new);
        ClientPlayNetworking.registerGlobalReceiver(AquilaVistaPacket.TYPE, (packet, context) -> {
            context.client().execute(() -> {
                //  if (!giaSettataVista) {
                vistaAquila.clear();
                for (List<String> list : packet.blockIds()) {
                    List<Identifier> builder = new ArrayList<>();
                    for (String str : list) {
                        builder.add(Identifier.parse(str));
                    }
                    Collections.reverse(builder);
                    vistaAquila.add(builder);
                }
            });
        });
        ClientPlayNetworking.registerGlobalReceiver(AquilaVistaMobsPacket.TYPE, (packet, context) -> {
            context.client().execute(() -> {
                mobs = packet.names();
                xMobs = packet.xs();
                yMobs = packet.ys();
            });
        });
        ClientPlayNetworking.registerGlobalReceiver(LituusInitialPacket.TYPE, (packet, context) -> {
            context.client().execute(() -> {
                if(context.client().gui.screen() instanceof LituusScreen scrn) {
                    scrn.hasAquila = packet.hasAquila();
                    scrn.aquilaName = packet.aquilaName();
                    scrn.aquilaHealth = packet.aquilaHealt();
                }
            });
        });
        EntityRenderers.register(
                CaputMundiEntities.AQUILA,
                AquilaRenderer::new
        );
        //Builder per aggirare il voluto final delle lambda
        ClientPlayConnectionEvents.JOIN.register((_, _, client) -> {
            if (client.level != null && client.level.getLevelData().isHardcore()) {
                LOGGER.info("JOIN HARDCORE CHIAMATO");
                eraHardcore = true;
                vecchioSelected.setLength(0);
                vecchioSelected.append(client.getLanguageManager().getSelected());
                client.getLanguageManager().setSelected("la_la");
                client.getLanguageManager().onResourceManagerReload(client.getResourceManager());
            }
        });
        ClientPlayConnectionEvents.DISCONNECT.register((_, client) -> {
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