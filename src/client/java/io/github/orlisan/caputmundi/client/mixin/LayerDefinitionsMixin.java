package io.github.orlisan.caputmundi.client.mixin;

import com.google.common.collect.ImmutableMap;
import com.llamalad7.mixinextras.sugar.Local;
import io.github.orlisan.caputmundi.client.model.CaputMundiModelLayers;
import io.github.orlisan.caputmundi.client.model.PilumModel;
import net.minecraft.client.model.geom.LayerDefinitions;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(LayerDefinitions.class)
public class LayerDefinitionsMixin {
    @Inject(method = "createRoots", at = @At("RETURN"), cancellable = true)
    private static void addPilumLayerDef(CallbackInfoReturnable<Map<ModelLayerLocation, LayerDefinition>> cir, @Local(name = "result") ImmutableMap.Builder<ModelLayerLocation, LayerDefinition> result) {
        result.put(CaputMundiModelLayers.PILUM, PilumModel.createBodyLayer());
        cir.setReturnValue(result.build());
    }
}
