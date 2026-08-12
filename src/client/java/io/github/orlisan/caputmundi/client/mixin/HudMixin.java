package io.github.orlisan.caputmundi.client.mixin;

import io.github.orlisan.caputmundi.client.CaputMundiClient;
import io.github.orlisan.caputmundi.client.CaputMundiClient.coords2d;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.Hud;
import net.minecraft.client.gui.screens.LevelLoadingScreen;
import net.minecraft.client.renderer.RenderPipelines;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.github.orlisan.caputmundi.client.CaputMundiClient.vistaAquila;
import static io.github.orlisan.caputmundi.client.CaputMundiClient.mobs;
import static io.github.orlisan.caputmundi.client.CaputMundiClient.xMobs;
import static io.github.orlisan.caputmundi.client.CaputMundiClient.yMobs;

@SuppressWarnings("UnresolvedMixinReference")
@Mixin(Hud.class)
public class HudMixin {
    @Unique
    private final Map<Identifier, TextureAtlasSprite> cache = new HashMap<>();
    //   @Unique
    //   private  final Identifier AQUILA_MOBS_ATLAS = Identifier.fromNamespaceAndPath(CaputMundi.MOD_ID, "textures/gui/aquila_mobs_atlas.png");
    @Unique
    private final Map<String, Identifier> paths = Map.of(
            "minecraft:zombie", CaputMundiClient.ZOMBIE_SPRITE,
            "minecraft:creepe", CaputMundiClient.CREEPER_SPRITE,
            "minecraft:enderm", CaputMundiClient.ENDERMAN_SPRITE,
            "minecraft:spider", CaputMundiClient.SPIDER_SPRITE,
            "minecraft:cave_s", CaputMundiClient.CAVE_SPIDER_SPRITE,
            "minecraft:skelet", CaputMundiClient.SKELETON_SPRITE
    );

    @Inject(method = "extractRenderState", at = @At("TAIL"))
    public void renderizzaVisioneAquila(final GuiGraphicsExtractor graphics, final DeltaTracker deltaTracker, CallbackInfo ci) {
        //   LOGGER.info("RenderizzaVisioneAquila Chiamato");
        Hud self = (Hud) (Object) this;
        // CaputMundi.LOGGER.error("PERCHÈ: {} {} {}", self, cache, paths);
        if (self != null && !self.isHidden() && !(Minecraft.getInstance().gui.screen() instanceof LevelLoadingScreen)) {
            //     LOGGER.info("Sono valido");
            //CaputMundi.LOGGER.info("Mixin Chiamato");
            int guiScale = Minecraft.getInstance().getWindow().getGuiScale();
            if (!(vistaAquila == null)) {
                for (int i = 0; i < vistaAquila.size(); i++) {
                    for (Identifier id : vistaAquila.get(i)) {
                        TextureAtlasSprite sprite;
                        if (!cache.containsKey(id)) {
                            BlockState state = BuiltInRegistries.BLOCK.get(id).map(Holder.Reference::value).orElse(Blocks.AIR).defaultBlockState();
                            BlockStateModelSet set = Minecraft.getInstance().getModelManager().getBlockStateModelSet();
                            BlockStateModel model = set.get(state);
                            List<BlockStateModelPart> parts = new ArrayList<>();
                            model.collectParts(RandomSource.create(state.getSeed(BlockPos.ZERO)), parts);
                            TextureAtlasSprite sprite1 = null;

                            for (BlockStateModelPart part : parts) {
                                List<BakedQuad> quads = part.getQuads(Direction.UP);
                                if (!quads.isEmpty()) {
                                    BakedQuad first = quads.getFirst();
                                    sprite1 = first.materialInfo().sprite();

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
                        coords2d coords = trovaPos(i);
                        int size = Math.round((float) 16 / guiScale);
                        int x0 = coords.x() * size + 10;
                        int y0 = coords.y() * size + 10;
                        int x1 = x0 + size;
                        int y1 = y0 + size;
                        //         CaputMundi.LOGGER.info("U0:{}, U1:{}, V0: {}, V1:{}", sprite.getU0(), sprite.getU1(), sprite.getV0(), sprite.getV1());
                        graphics.blit(finalId, x0, y0, x1, y1, sprite.getU0(), sprite.getU1(), sprite.getV0(), sprite.getV1());
                    }
                }
            }
            if (mobs != null && xMobs != null && yMobs != null &&
                !mobs.isEmpty() && !xMobs.isEmpty() && !yMobs.isEmpty()) {
                for (int i = 0; i < mobs.size(); i++) {
                    String mobName = mobs.get(i);
                    int dimensionSize = Math.round((float) 8 / guiScale);
                    int coordsSize = Math.round((float) 16 / guiScale);
                    double x0 = xMobs.get(i) * coordsSize + 10;
                    double y0 = yMobs.get(i) * coordsSize + 10;

                    Identifier location = paths.get(mobName.substring(0, 16)); //Lascia stare il substring quello non lo ho ancora testato, è per risolvere il problema della sovrascrizione della mappa
                    if (location != null) {
                        graphics.blit(RenderPipelines.GUI_TEXTURED, location, (int) x0, (int) y0, 0.0f, 0.0f, dimensionSize, dimensionSize, 8, 8, 8, 8);
                    }
                }
            }

        }

    }

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
    }*/
    @Unique
    final int texture_size = 16;

    @Unique
    private coords2d trovaPos(int index) {
        return new coords2d(index % texture_size, (index - index % texture_size) / texture_size);
    }


}

