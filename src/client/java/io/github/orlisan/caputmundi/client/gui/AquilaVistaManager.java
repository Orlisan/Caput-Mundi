package io.github.orlisan.caputmundi.client.gui;

import io.github.orlisan.caputmundi.client.CaputMundiClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
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
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AquilaVistaManager {
    private static final Map<Identifier, TextureAtlasSprite> cache = new HashMap<>();

    private static final Map<String, Identifier> paths = Map.of(
            "minecraft:zombie", CaputMundiClient.ZOMBIE_SPRITE,
            "minecraft:creepe", CaputMundiClient.CREEPER_SPRITE,
            "minecraft:enderm", CaputMundiClient.ENDERMAN_SPRITE,
            "minecraft:spider", CaputMundiClient.SPIDER_SPRITE,
            "minecraft:cave_s", CaputMundiClient.CAVE_SPIDER_SPRITE,
            "minecraft:skelet", CaputMundiClient.SKELETON_SPRITE
    );

    public static void blitAquilaVista(GuiGraphicsExtractor graphics, int offsetX, int offsetY, List<List<Identifier>> vistaAquila) {
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
                    CaputMundiClient.coords2d coords = trovaPos(i);
                    int size = Math.round((float) 16 / guiScale);
                    int x0 = coords.x() * size + offsetX;
                    int y0 = coords.y() * size + offsetY;
                    int x1 = x0 + size;
                    int y1 = y0 + size;
                    //         CaputMundi.LOGGER.info("U0:{}, U1:{}, V0: {}, V1:{}", sprite.getU0(), sprite.getU1(), sprite.getV0(), sprite.getV1());
                    graphics.blit(finalId, x0, y0, x1, y1, sprite.getU0(), sprite.getU1(), sprite.getV0(), sprite.getV1());
                }
            }
        }
    }

    public record AquilaVistaMobsDatas(Vec2 coords, int dimensionSize, Identifier loc, Vec3 realPos) {
    }

    public static List<AquilaVistaMobsDatas> getAquilaVistaMobsDatas(int offsetX, int offsetY, List<Double> xMobs, List<Double> yMobs, List<String> mobs, List<Vec3> realPoss) {
        int guiScale = Minecraft.getInstance().getWindow().getGuiScale();
        ArrayList<AquilaVistaMobsDatas> result = new ArrayList<>();
        if (mobs != null && xMobs != null && yMobs != null &&
            !mobs.isEmpty() && !xMobs.isEmpty() && !yMobs.isEmpty()) {
            for (int i = 0; i < mobs.size(); i++) {
                String mobName = mobs.get(i);
                int dimensionSize = Math.round((float) 8 / guiScale);
                int coordsSize = Math.round((float) 16 / guiScale);
                double x0 = xMobs.get(i) * coordsSize + offsetX;
                double y0 = yMobs.get(i) * coordsSize + offsetY;

                Identifier location = paths.get(mobName.substring(0, 16));
                if (location != null) {
                    try {
                        result.add(new AquilaVistaMobsDatas(new Vec2((float) x0, (float) y0), dimensionSize, location, round(realPoss.get(i))));
                    } catch (IndexOutOfBoundsException e) {
                        result.add(new AquilaVistaMobsDatas(new Vec2((float) x0, (float) y0), dimensionSize, location, new Vec3(0, 0, 0)));
                    }
                }
            }
        }
        return result;
    }

    public static Vec3 round(Vec3 input) {
        return new Vec3((double) Math.round(input.x * 100) / 100, (double) Math.round(input.y * 100) / 100, (double) Math.round(input.y * 100) / 100);
    }

    private static CaputMundiClient.coords2d trovaPos(int index) {
        return new CaputMundiClient.coords2d(index % texture_size, (index - index % texture_size) / texture_size);
    }

    static final int texture_size = 16;

}
