package io.github.orlisan.caputmundi.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;

import static io.github.orlisan.caputmundi.CaputMundi.LOGGER;

public class CaputMundiClient implements ClientModInitializer {
    StringBuilder vecchioSelected = new StringBuilder();
    public static boolean eraHardcore = false;

    @Override
    public void onInitializeClient() {
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
}