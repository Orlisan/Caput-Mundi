package io.github.orlisan.caputmundi.client.mixin;

import net.minecraft.client.resources.SplashManager;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.ArrayList;
import java.util.List;

@Mixin(SplashManager.class)
public class SplashManagerMixin {
    @ModifyVariable(method = "apply", argsOnly = true, ordinal = 0, at = @At("HEAD"))
    private List<Component> addCaputMundiSplashes(List<Component> preparations) {
        List<Component> newPreparations = new ArrayList<>(preparations);
        newPreparations.clear(); //debug
        newPreparations.add(Component.literal("Veni, vidi, vici").withStyle(Style.EMPTY.withColor(0xC00000)));
        newPreparations.add(Component.literal("Alea iacta est").withStyle(Style.EMPTY.withColor(0x660033)));
        newPreparations.add(Component.literal("Memento mori").withStyle(Style.EMPTY.withColor(0x555555)));
        newPreparations.add(Component.literal("Carpe diem").withStyle(Style.EMPTY.withColor(0xFFFFFF)));
        return newPreparations;
    }
}
