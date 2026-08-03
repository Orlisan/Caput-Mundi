package io.github.orlisan.caputmundi.blocks;

import io.github.orlisan.caputmundi.CaputMundi;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.Function;

public class CaputMundiBlocks {
    public static Block LAURUS_CROP;
    public static Block registerBlock(String id) {
        Identifier identifier = Identifier.fromNamespaceAndPath(CaputMundi.MOD_ID, id);
        ResourceKey<Block> key = ResourceKey.create(Registries.BLOCK, identifier);
        return Registry.register(BuiltInRegistries.BLOCK, key, new Block(BlockBehaviour.Properties.of().setId(key)));
    }
    public static <T extends Block> T registerCustomBlock(String id, Function<BlockBehaviour.Properties, T> function) {
        Identifier identifier = Identifier.fromNamespaceAndPath(CaputMundi.MOD_ID, id);
        ResourceKey<Block> key = ResourceKey.create(Registries.BLOCK, identifier);
        BlockBehaviour.Properties prop = BlockBehaviour.Properties.of().setId(key);
        T block = function.apply(prop);
        return Registry.register(BuiltInRegistries.BLOCK, key, block);
    }

    public static <T extends Block> T registerCustomBlock(String id, Function<BlockBehaviour.Properties, T> function, BlockBehaviour.Properties props) {
        Identifier identifier = Identifier.fromNamespaceAndPath(CaputMundi.MOD_ID, id);
        ResourceKey<Block> key = ResourceKey.create(Registries.BLOCK, identifier);
        props.setId(key);
        T block = function.apply(props);
        return Registry.register(BuiltInRegistries.BLOCK, key, block);
    }
    public static void register() {
        LAURUS_CROP = registerCustomBlock("laurus_crop", LaurusBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.WHEAT));
    }
}
