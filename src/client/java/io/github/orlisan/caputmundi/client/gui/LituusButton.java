package io.github.orlisan.caputmundi.client.gui;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.InputWithModifiers;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;

public class LituusButton extends AbstractButton {
    Identifier sprite;
     final int scrWid;
     final int scrHei;
     final int txWid;
     final int txHei;
     final Runnable onPress;
    public LituusButton(Runnable onPress, int x, int y, int width, int height, Identifier sprite, int scrWid, int scrHei, int txWid, int txHei) {
        super(x, y, width, height, Component.literal(""));
        this.sprite = sprite;
        this.scrWid = scrWid;
        this.scrHei = scrHei;
        this.txWid = txWid;
        this.txHei = txHei;
        this.onPress = onPress;
    }

    @Override
    public void onPress(InputWithModifiers input) {
        onPress.run();
    }

    void extractSprite(GuiGraphicsExtractor graphics) {
        graphics.blit(RenderPipelines.GUI_TEXTURED, sprite, getX(), getY(), 0.0f, 0.0f, width, height, scrWid, scrHei, txWid, txHei);
    }

    @Override
    protected void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        this.extractSprite(graphics);
    }
    @Override
    protected void updateWidgetNarration(NarrationElementOutput output) {

    }
}
