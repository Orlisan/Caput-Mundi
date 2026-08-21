package io.github.orlisan.caputmundi.client.gui;

import io.github.orlisan.caputmundi.client.CaputMundiClient;
import io.github.orlisan.caputmundi.gui.LituusMenu;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import org.jspecify.annotations.NonNull;

public class LituusScreen extends AbstractContainerScreen<LituusMenu> {
    public LituusScreen(LituusMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    LituusMode lituusMode;

    public LituusMode getMode() {
        return lituusMode;
    }

    public enum LituusMode {
        SPY_ATTACK,
        OVERVIEW
    }

    EditBox playerTargetEditBox;
    LituusButton spyButton;
    LituusButton spyModeButton;

    public void setMode(LituusMode mode) {
        lituusMode = mode;
        if (mode == LituusMode.OVERVIEW) {
            spyButton.visible = false;
            playerTargetEditBox.visible = false;

        } else if (mode == LituusMode.SPY_ATTACK) {
            spyButton.visible = true;
            playerTargetEditBox.visible = true;
        }

    }

    @Override
    protected void init() {
        super.init();
        playerTargetEditBox = new EditBox(font, (int) (leftPos + imageWidth / 2.5), topPos + imageHeight / 6, 100, 50, Component.literal("A player choosing editbox"));
        playerTargetEditBox.setSuggestion("Player name...");
        playerTargetEditBox = addRenderableWidget(playerTargetEditBox);
        spyModeButton = addRenderableWidget(new LituusButton(() -> {
            setMode(LituusMode.OVERVIEW);
        }, leftPos+2, topPos+2, 24, 24, CaputMundiClient.LITUUS_OVERVIEW_SPRITE, 16, 16,            16, 16));
        spyButton = addRenderableWidget(new LituusButton(() -> {
            if (this.getMode() != LituusMode.SPY_ATTACK) return;
            this.getMenu().spyPlayer(playerTargetEditBox.getValue());
        }, leftPos + imageWidth / 4, topPos + imageHeight / 4, 100, 50, CaputMundiClient.ZOMBIE_SPRITE, 8, 8, 8, 8));
    }

    boolean tick1 = true;
    public boolean hasAquila = false;
    public String aquilaName = "Loading...";
    public Double aquilaHealth = Double.NaN;
    @Override
    public void extractRenderState(@NonNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        super.extractRenderState(graphics, mouseX, mouseY, a);
        if (tick1) {
            setMode(LituusMode./*OVERVIEW*/SPY_ATTACK);
            tick1 = false;
        }
        if (!hasAquila) {
            graphics.fill(leftPos, topPos, leftPos+imageWidth, topPos +imageHeight, 0x80808080);
            graphics.text(font, "NO EAGLE FOUND", leftPos + imageWidth / 3, topPos + imageHeight / 3, 0xAA808080);
        }
        if(getMode() == LituusMode.OVERVIEW) extractOverview(graphics);
    }
    public void extractOverview(@NonNull GuiGraphicsExtractor graphics) {
        graphics.text(font, aquilaName,  (int) (leftPos + imageWidth / 2.5), topPos + imageHeight / 6, 0xFFFFD700);
        graphics.text(font, aquilaHealth.toString(), leftPos + imageWidth / 3, (int) (topPos + imageHeight / 1.5), 0xFFFF0000);
    }
    @Override
    protected void extractLabels(GuiGraphicsExtractor graphics, int xm, int ym) {
    }
}
