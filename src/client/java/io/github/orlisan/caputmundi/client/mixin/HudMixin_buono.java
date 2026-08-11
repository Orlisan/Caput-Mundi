/*package io.github.orlisan.caputmundi.client.mixin;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.Hud;
import net.minecraft.client.gui.screens.LevelLoadingScreen;
import net.minecraft.client.renderer.block.BlockStateModelSet;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import io.github.orlisan.caputmundi.client.CaputMundiClient.coords2d;

import java.io.Console;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.github.orlisan.caputmundi.client.CaputMundiClient.vistaAquila;
import static io.github.orlisan.caputmundi.CaputMundi.LOGGER;

@SuppressWarnings("UnresolvedMixinReference")
@Mixin(Hud.class)
public class HudMixin {
    @Unique
    public Hud self = (Hud) (Object) this;
    @Unique
    private final Map<Identifier, TextureAtlasSprite> cache = new HashMap<>();

    @Inject(method = "extractRenderState", at = @At("TAIL"))
    public void renderizzaVisioneAquila(final GuiGraphicsExtractor graphics, final DeltaTracker deltaTracker, CallbackInfo ci) {
        //   LOGGER.info("RenderizzaVisioneAquila Chiamato");
        if (!self.isHidden() && !(Minecraft.getInstance().gui.screen() instanceof LevelLoadingScreen)) {
            //     LOGGER.info("Sono valido");
            if (!(vistaAquila == null)) {
                //       LOGGER.info("VistaAquila non è null");
                //   LOGGER.info("Lunghezza: {}", vistaAquila.size());
                for (int i = 0; i < vistaAquila.size(); i++) {
                    for (Identifier id : vistaAquila.get(i)) {
                        //       LOGGER.info("Blocco che sto per renderizzare: {}", id);
                        TextureAtlasSprite sprite;
                        if (!cache.containsKey(id)) {
                            // Identifier id = vistaAquila.get(i);
                            BlockState state = BuiltInRegistries.BLOCK.get(id).map(Holder.Reference::value).orElse(Blocks.AIR).defaultBlockState();
                            BlockStateModelSet set = Minecraft.getInstance().getModelManager().getBlockStateModelSet();
                            BlockStateModel model = set.get(state);
                            List<BlockStateModelPart> parts = new ArrayList<>();
                            model.collectParts(RandomSource.create(state.getSeed(BlockPos.ZERO)), parts);
                            TextureAtlasSprite sprite1 = null;
                 /*   float U0;
                    float U1;
                    float V0;
                    float V1;*//*
                            for (BlockStateModelPart part : parts) {
                                List<BakedQuad> quads = part.getQuads(Direction.UP);
                                if (!quads.isEmpty()) {
                                    BakedQuad first = quads.getFirst();
                                    sprite1 = first.materialInfo().sprite();
                   /*        long packetUv0 = first.packedUV0();
                           U0 = UVPair.unpackU(packetUv0);
                            V0 = UVPair.unpackV(packetUv0);

                            long packetUv1 = first.packedUV1();
                            U1 = UVPair.unpackU(packetUv1);
                            V1 = UVPair.unpackV(packetUv1);*//*
                                    break;
                                }
                            }
                            if (sprite1 == null) {
                                sprite1 = set.getParticleMaterial(state).sprite();
                            }
                            cache.put(id, sprite1);
                            sprite = sprite1;
                        } else {
                            sprite = cache.get(id);
                        }

                        Identifier finalId = sprite.atlasLocation();
                        //  LOGGER.info("FinalId: {}", finalId);
                        coords2d coords = trovaPos(i);
                        int size = Math.round((float) 16 / Minecraft.getInstance().getWindow().getGuiScale());
                        int x0 = coords.x() * size + 10;
                        int y0 = coords.y() * size + 10;
                        int x1 = x0 + size;
                        int y1 = y0 + size;
                     /*  BlockState state = Minecraft.getInstance().level.getBlockState(new BlockPos(1, 1, 1));
                        Minecraft.getInstance().getBlockColors().getT*//*
                        graphics.blit(finalId, x0, y0, x1, y1, sprite.getU0(), sprite.getU1(), sprite.getV0(), sprite.getV1());
                    }
                }
            }
        }
    }
*/
    /**
     * Commento testimone del mio modo per prendere da solo le uv
     */
    // record uv(float u, float v) {}
  /*  uv convertToBinary(long start) {
        List<Byte> result = new ArrayList<>();
        while(start > 0) {
            result.add((byte) (start % 2));
            //Non mi fido dei troncamenti di java, faccio da solo
            start = (start-start%2)/2;
        }
        Collections.reverse(result);
        String collect = result.stream().map(String::valueOf).collect(Collectors.joining(""));
        return new uv(Float.intBitsToFloat(Integer.parseInt(collect.substring(0, collect.length()/2))), Float.intBitsToFloat(Integer.parseInt(collect.substring(collect.length()/2, collect.length()-1))));
    }*//*
    @Unique
    final int texture_size = 16;

    @Unique
    private coords2d trovaPos(int index) {
        return new coords2d(index % texture_size, (index - index % texture_size) / texture_size);
    }
}
*/
