package io.github.orlisan.caputmundi.client.gui;

import io.github.orlisan.caputmundi.CaputMundi;
import io.github.orlisan.caputmundi.client.CaputMundiClient;
import io.github.orlisan.caputmundi.gui.LituusMenu;
import io.github.orlisan.caputmundi.packets.LituusPacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.PlayerFaceExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LituusScreen extends AbstractContainerScreen<LituusMenu> {
    public LituusScreen(LituusMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    LituusMode lituusMode;

    public LituusMode getMode() {
        return lituusMode;
    }

    public enum LituusMode {
        SPY,
        OVERVIEW,
        MAP
    }

    @Override
    public void removed() {
        sendCommand("stopsend");
        super.removed();
    }

    public List<List<Identifier>> vistaAquila = new ArrayList<>();
    public List<String> mobs = new ArrayList<>();
    public List<Double> xMobs = new ArrayList<>();
    public List<Double> yMobs = new ArrayList<>();
    public List<Vec3> realCoordsMobs = new ArrayList<>();

    boolean isSpying = false;
    EditBox playerTargetEditBox;
    LituusButton spyButton;
    LituusButton overviewModeButton;
    LituusButton spyModeButton;
    LituusButton richiamaButton;
    LituusButton mapModButton;

    public void setMode(LituusMode mode) {
        lituusMode = mode;

        spyButton.visible = false;
        playerTargetEditBox.visible = false;
        playerTargetEditBox.setFocused(false);
        richiamaButton.visible = false;

        if (mode == LituusMode.OVERVIEW) {
            richiamaButton.visible = true;
        } else if (mode == LituusMode.SPY) {
            spyButton.visible = true;
            playerTargetEditBox.visible = true;
            playerTargetEditBox.setFocused(true);
        }

    }

    void sendCommand(String command) {
        ClientPlayNetworking.send(new LituusPacket(command, aquilaUUID));
    }

    public String aquilaUUID = "";

    @Override
    protected void init() {
        super.init();
        playerTargetEditBox = new EditBox(font, (int) (leftPos + imageWidth / 2.5), topPos + imageHeight / 6, 100, 30, Component.literal("A player choosing editbox"));
        playerTargetEditBox.setSuggestion("Player name...");
        playerTargetEditBox = addRenderableWidget(playerTargetEditBox);
        overviewModeButton = addRenderableWidget(new LituusButton(() -> {
            setMode(LituusMode.OVERVIEW);
            sendCommand("stopsend");
        }, leftPos + 2,
                topPos + 2, 24, 24, CaputMundiClient.LITUUS_OVERVIEW_SPRITE, 16, 16, 16, 16));
        spyModeButton = addRenderableWidget(new LituusButton(() -> {
            setMode(LituusMode.SPY);
            sendCommand("stopsend");
        }, leftPos + 2, topPos + 28,
                24, 24, CaputMundiClient.LITUUS_OVERVIEW_SPRITE, 16, 16, 16, 16));
        mapModButton = addRenderableWidget(new LituusButton(() -> {
            setMode(LituusMode.MAP);
            sendCommand("startsend");
        }, leftPos + 2, topPos + 54,
                24, 24, CaputMundiClient.LITUUS_OVERVIEW_SPRITE, 16, 16, 16, 16));
        spyButton = addRenderableWidget(new LituusButton(() -> {
            if (this.getMode() != LituusMode.SPY) return;
            if (minecraft.player != null) {
                sendCommand("spy:" + (isSpying ? "stop" : playerTargetEditBox.getValue()) + ":" + minecraft.player.getStringUUID());
                isSpying = !isSpying;
            }
        }, leftPos + imageWidth / 4, topPos + imageHeight / 2, 100, 50, CaputMundiClient.ZOMBIE_SPRITE, 8, 8, 8, 8));
        richiamaButton = addRenderableWidget(new LituusButton(() -> {
            if (this.getMode() != LituusMode.OVERVIEW) return;
            if (minecraft.player != null)
                sendCommand("recall:" + minecraft.player.getStringUUID());
        }, leftPos + imageWidth / 4, topPos + imageHeight / 2, 100, 50, CaputMundiClient.SKELETON_SPRITE, 8, 8, 8, 8));
        setMode(LituusMode.OVERVIEW);

    }

    EditBox xBox;
    EditBox yBox;
    public boolean hasAquila = false;
    public String aquilaName = "Loading...";
    public Double aquilaHealth = Double.NaN;
    public List<CaputMundiClient.PlayerDatas> playerDatas = new ArrayList<>();

    @Override
    public void extractRenderState(@NonNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        graphics.blit(RenderPipelines.GUI_TEXTURED, Identifier.fromNamespaceAndPath(CaputMundi.MOD_ID, "textures/gui/lituus_background.png"), leftPos - 28, topPos - 83, 0, 0, 256, 256, 256, 256, 256, 256);
        if (!hasAquila) {
            graphics.fill(leftPos - 10, topPos - 10, leftPos + imageWidth + 10, topPos + imageHeight + 10, 0x80808080);
            graphics.text(font, "NO EAGLE FOUND", leftPos + imageWidth / 3, topPos + imageHeight / 3, 0xAA808080);
        }
        super.extractRenderState(graphics, mouseX, mouseY, a);


        if (getMode() == LituusMode.OVERVIEW) extractOverview(graphics);
        if (getMode() == LituusMode.MAP) extractMap(graphics, mouseX, mouseY);
    }

    public static final int TRIGGER_RAGGIO = 16;

    public void extractMap(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        int offsetX = leftPos + 25;
        int offsetY = topPos + 10;
        int guiScale = Minecraft.getInstance().getWindow().getGuiScale();
        int coordsSize = Math.round((float) 16 / guiScale);
        AquilaVistaManager.blitAquilaVista(graphics, offsetX, offsetY, vistaAquila);
        var result = AquilaVistaManager.getAquilaVistaMobsDatas(offsetX, offsetY, xMobs, yMobs, mobs, realCoordsMobs);
        for (AquilaVistaManager.AquilaVistaMobsDatas datas : result) {
            float x = datas.coords().x;
            float y = datas.coords().y;
            graphics.blit(RenderPipelines.GUI_TEXTURED, datas.loc(), (int) x, (int) y, 0.0f, 0.0f, datas.dimensionSize(), datas.dimensionSize(), 8, 8, 8, 8);
            if (mouseX > x - TRIGGER_RAGGIO && mouseX < x + 8 + TRIGGER_RAGGIO &&
                mouseY > y - TRIGGER_RAGGIO && mouseY < y + 8 + TRIGGER_RAGGIO) {
                graphics.pose().pushMatrix();
                graphics.pose().scale(0.5f, 0.5f);
                int y0 = (int) ((y - 15) / 0.5f);
                int x0 = (int) ((x - 23) / 0.5f);
                graphics.fill(x0, y0, x0 + 100, y0 + 30, 0xFF000000);
                graphics.text(font, "X: " + datas.realPos().x + " Y: " + datas.realPos().y, x0 + 10, y0 + 5, 0xFFFFFFFF);
                graphics.text(font, "    Z: " + datas.realPos().z, x0 + 10, y0 + 20, 0xFFFFFFFF);
                graphics.pose().popMatrix();
            }
        }
        for (CaputMundiClient.PlayerDatas datas : playerDatas) {
            int x = (int) datas.playerPosInMap().x * coordsSize + offsetX;
            int y = (int) datas.playerPosInMap().y * coordsSize + offsetY;
            ClientPacketListener connection = minecraft.getConnection();
            if (connection != null) {
                PlayerInfo playerInfo = connection.getPlayerInfo(UUID.fromString(datas.playerUUID()));
                if (playerInfo != null) {
                    ClientLevel level = this.minecraft.level;
                    if (level != null) {
                        Player playerByUUID = level.getPlayerByUUID(playerInfo.getProfile().id());
                        boolean flip = playerByUUID != null && AvatarRenderer.isPlayerUpsideDown(playerByUUID);
                        PlayerFaceExtractor.extractRenderState(graphics, playerInfo.getSkin().body().texturePath(), x, y, 8, playerInfo.showHat(), flip, -1);
                    }
                }
            }
            if (mouseX > x - TRIGGER_RAGGIO && mouseX < x + 16 + TRIGGER_RAGGIO &&
                mouseY > y - TRIGGER_RAGGIO && mouseY < y + 16 + TRIGGER_RAGGIO) {
                graphics.pose().pushMatrix();
                graphics.pose().scale(0.5f, 0.5f);
                int y0 = (int) ((y - 45) / 0.5f);
                int x0 = (int) ((x - 23) / 0.5f);
                var pos = AquilaVistaManager.round(datas.playerPos());
                graphics.fill(x0, y0, x0 + 100, y0 + 60, 0xFF000000);
                graphics.text(font, "   " + datas.playerName(), x0 + 10, y0 + 5, 0xFF0000FF);
                graphics.text(font, "X: " + pos.x + " Y: " + pos.y, x0 + 22, y0 + 20, 0xFFFFFFFF);
                graphics.text(font, "    Z: " + pos.z, x0 + 25, y0 + 35, 0xFFFFFFFF);
                graphics.text(font, "Health: " + Math.round(datas.playerHealt()*100)/100, x0 + 25, y0 + 50, 0xFFFF0000);
                graphics.pose().popMatrix();
            }
        }
    }

    public void extractOverview(@NonNull GuiGraphicsExtractor graphics) {
        graphics.text(font, aquilaName.equals("entity.caput_mundi.aquila") ? "Eagle" : aquilaName, (leftPos + imageWidth / 3), topPos + imageHeight / 7, 0xFFFFD700);
        graphics.text(font, aquilaHealth.toString(), leftPos + imageWidth / 3, (topPos + imageHeight / 5), 0xFFFF0000);
    }

    @Override
    protected void extractLabels(@NonNull GuiGraphicsExtractor graphics, int xm, int ym) {
    }
}
