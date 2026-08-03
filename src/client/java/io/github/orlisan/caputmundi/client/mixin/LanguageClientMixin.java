package io.github.orlisan.caputmundi.client.mixin;

import com.google.common.collect.ImmutableMap;
import io.github.orlisan.caputmundi.client.CaputMundiClient;
import net.minecraft.client.resources.language.LanguageInfo;
import net.minecraft.client.resources.language.LanguageManager;
import net.minecraft.server.packs.PackResources;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Map;
import java.util.stream.Stream;

import static io.github.orlisan.caputmundi.CaputMundi.LOGGER;

@Mixin(LanguageManager.class)
public class LanguageClientMixin {
    @Shadow
    private static Map<String, LanguageInfo> extractLanguages(Stream<PackResources> resourcePacks) {
        return null;
    }

    @Redirect(
            method = "onResourceManagerReload",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/resources/language/LanguageManager;extractLanguages(Ljava/util/stream/Stream;)Ljava/util/Map;")
    )
    private Map<String, LanguageInfo> mettiSoloLatino(Stream<PackResources> resourcePacks) {
        if (CaputMundiClient.eraHardcore/*Minecraft.getInstance().level != null && Minecraft.getInstance().level.getLevelData().isHardcore()*/) {
            LOGGER.info("Sono in hardcore");
            return ImmutableMap.of("la_la", new LanguageInfo("SPQR", "Latinus", false));
        } else {
            return extractLanguages(resourcePacks);
        }
    }
}