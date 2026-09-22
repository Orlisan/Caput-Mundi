package io.github.orlisan.caputmundi.client;

import io.github.orlisan.caputmundi.CaputMundi;
import io.github.orlisan.caputmundi.client.gui.LituusScreen;
import io.github.orlisan.caputmundi.client.renderer.AquilaRenderer;
import io.github.orlisan.caputmundi.entities.CaputMundiEntities;
import io.github.orlisan.caputmundi.gui.CaputMundiMenuTypes;
import io.github.orlisan.caputmundi.packets.AquilaVistaMobsPacket;
import io.github.orlisan.caputmundi.packets.AquilaVistaPacket;
import io.github.orlisan.caputmundi.packets.LituusPacket;
import io.github.orlisan.caputmundi.packets.PlayerDatasPacket;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CaputMundiClient implements ClientModInitializer {
    StringBuilder vecchioSelected = new StringBuilder();
    public static boolean eraHardcore = false;
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
        ClientPlayNetworking.registerGlobalReceiver(AquilaVistaPacket.TYPE, (packet, context) -> context.client().execute(() -> {
            if (context.client().gui.screen() instanceof LituusScreen scrn) {
                scrn.vistaAquila.clear();
                for (List<String> list : packet.blockIds()) {
                    List<Identifier> builder = new ArrayList<>();
                    for (String str : list) {
                        builder.add(Identifier.parse(str));
                    }
                    Collections.reverse(builder);
                    scrn.vistaAquila.add(builder);
                }
            }
        }));
        ClientPlayNetworking.registerGlobalReceiver(AquilaVistaMobsPacket.TYPE, (packet, context) -> context.client().execute(() -> {
            if (context.client().gui.screen() instanceof LituusScreen scrn) {
                scrn.mobs = packet.names();
                scrn.xMobs = packet.xs();
                scrn.yMobs = packet.ys();
                scrn.realCoordsMobs = packet.positions();
            }
        }));
        ClientPlayNetworking.registerGlobalReceiver(LituusPacket.TYPE, (packet, context) -> context.client().execute(() -> {

            if (context.client().gui.screen() instanceof LituusScreen scrn) {
                if(packet.aquilaName().equals("clearplayers")) {
                    scrn.playerDatas.clear();
                } else {
                    scrn.hasAquila = packet.hasAquila();
                    scrn.aquilaName = packet.aquilaName();
                    scrn.aquilaHealth = packet.aquilaHealt();
                    scrn.aquilaUUID = packet.aquilaUUID();
                }
            }
        }));
        ClientPlayNetworking.registerGlobalReceiver(PlayerDatasPacket.TYPE, (packet, context) -> context.client().execute(() -> {
            if (context.client().gui.screen() instanceof LituusScreen scrn) {
                scrn.playerDatas.add(new PlayerDatas(packet.playerUUID(), packet.playerName(), packet.playerHealth(), packet.playerPos(), packet.playerPosInMap()));
            }
        }));
        EntityRenderers.register(
                CaputMundiEntities.AQUILA,
                AquilaRenderer::new
        );
        //Builder per aggirare il voluto final delle lambda
        ClientPlayConnectionEvents.JOIN.register((_, _, client) -> {
            if (client.level != null && client.level.getLevelData().isHardcore()) {
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
                client.getLanguageManager().setSelected(vecchioSelected.toString());
                vecchioSelected.setLength(0);
                client.getLanguageManager().onResourceManagerReload(client.getResourceManager());
            }
        });
        // This entrypoint is suitable for setting up client-specific logic, such as rendering.
    }

    public record coords2d(int x, int y) {
    }
    public record PlayerDatas(String playerUUID, String playerName, float playerHealt, Vec3 playerPos, Vec2 playerPosInMap) {}
}
