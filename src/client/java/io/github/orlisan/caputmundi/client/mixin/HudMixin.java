package io.github.orlisan.caputmundi.client.mixin;

import io.github.orlisan.caputmundi.client.CaputMundiClient;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.Hud;
import net.minecraft.client.gui.screens.LevelLoadingScreen;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import io.github.orlisan.caputmundi.client.CaputMundiClient.coords2d;

@SuppressWarnings("UnresolvedMixinReference")
@Mixin(Hud.class)
public class HudMixin {
    @Unique
    public Hud self = (Hud) (Object) this;

    @Inject(method = "extractRenderState", at = @At("TAIL"))
    public void renderizzaVisioneAquila(final GuiGraphicsExtractor graphics, final DeltaTracker deltaTracker, CallbackInfo ci) {
        if (!self.isHidden() && !(Minecraft.getInstance().gui.screen() instanceof LevelLoadingScreen)) {
            if (!(CaputMundiClient.vistaAquila == null)) {
                for (int i = 0; i < CaputMundiClient.vistaAquila.size(); i++) {
                    Identifier id = CaputMundiClient.vistaAquila.get(i);
                    coords2d coords = trovaPos(i);
                    graphics.blit(id, coords.x() * 16 + 10, coords.y() * 16 + 10, 16, 16, 0.0f, 0.0f, 1.0f, 1.0f);
                }
            }
        }
    }

    @Unique
    final int texture_size = 16;

    @Unique
    private coords2d trovaPos(int index) {
        return new coords2d(index % texture_size, (index - index % texture_size) / texture_size);
    }
}

