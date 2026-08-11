package io.github.orlisan.caputmundi.client.mixin;

import io.github.orlisan.caputmundi.CaputMundiConstants;
import net.minecraft.client.gui.components.SplashRenderer;
import net.minecraft.client.resources.SplashManager;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.util.RandomSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(SplashManager.class)
public class SplashManagerMixin {


    @Unique
    private final RandomSource RANDOM = RandomSource.create();

    @Inject(method = "getSplash", at = @At("HEAD"), cancellable = true)
    public void addCaputMundiSplashes(CallbackInfoReturnable<SplashRenderer> cir) {
        if (RANDOM.nextDouble() > 0.1d) {
            cir.setReturnValue(new SplashRenderer(CaputMundiConstants.SPLASHES_ROMANI.get(RANDOM.nextInt(CaputMundiConstants.SPLASHES_ROMANI.size()))));
        }
    }
}
