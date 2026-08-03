package io.github.orlisan.caputmundi.client.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.LanguageSelectScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Screen.class)
public class ScreenMixin {
    @Inject(at = @At("TAIL"), method = "extractRenderState")
    public void mettiTesto(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final float a, CallbackInfo ci) {
        Screen self = (Screen) (Object) this;
        if (self instanceof LanguageSelectScreen && Minecraft.getInstance().level != null && Minecraft.getInstance().level.getLevelData().isHardcore()) {
            graphics.pose().pushMatrix();
            graphics.pose().scale(2, 2);
            graphics.text(Minecraft.getInstance().font, "Latino effugere non potes.", self.width / 3 / 2, self.height / 2 / 2, 0xFFFF0000);
            graphics.pose().popMatrix();
        }
    }
}
