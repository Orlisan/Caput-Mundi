package io.github.orlisan.caputmundi.items;

import io.github.orlisan.caputmundi.CaputMundi;
import io.github.orlisan.caputmundi.blocks.CaputMundiBlocks;
import io.github.orlisan.caputmundi.entities.CaputMundiEntities;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.block.Block;

import java.util.function.Function;

public class CaputMundiItems {
    public static Item LAURUS_LEAF, LAURUS_SEED;
    public static Item LAURUS_CROWN, GOLDEN_LAURUS_CROWN;
    public static Item IMPERIAL_GOLD_INGOT;
    public static Item AQUILA_SPAWN_EGG_ITEM, AQUILA_ARMOR_ITEM, LITUUS_ITEM;

    private static Item registerItem(String id) {
        Identifier identifier = Identifier.fromNamespaceAndPath(CaputMundi.MOD_ID, id);
        ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, identifier);
        return Registry.register(BuiltInRegistries.ITEM, key, new Item(new Item.Properties().setId(key)));
    }

    private static Item registerBlockItem(String id, Block block) {
        Identifier identifier = Identifier.fromNamespaceAndPath(CaputMundi.MOD_ID, id);
        ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, identifier);
        return Registry.register(BuiltInRegistries.ITEM, key, new BlockItem(block, new Item.Properties().setId(key)));
    }

    private static Item registerItem(String id, Item.Properties props) {
        Identifier identifier = Identifier.fromNamespaceAndPath(CaputMundi.MOD_ID, id);
        ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, identifier);
        return Registry.register(BuiltInRegistries.ITEM, key, new Item(props.setId(key)));
    }

    private static <T extends Item> T registerItem(String id, Function<Item.Properties, T> factory, Item.Properties props) {
        Identifier identifier = Identifier.fromNamespaceAndPath(CaputMundi.MOD_ID, id);
        ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, identifier);
        props.setId(key);
        T item = factory.apply(props);
        return Registry.register(BuiltInRegistries.ITEM, key, item);
    }

    public static void register() {
        LAURUS_LEAF = registerItem("laurus_leaves");
        LAURUS_SEED = registerBlockItem("laurus_seed", CaputMundiBlocks.LAURUS_CROP);
        LAURUS_CROWN = registerItem("laurus_crown", LaurusCrownItem::new, new Item.Properties().durability(360).equippable(EquipmentSlot.HEAD));
        //  GOLDEN_LAURUS_CROWN = registerItem("golden_laurus_crown", LaurusCrownItem::new, new Item.Properties().durability(720).equippable(EquipmentSlot.HEAD));
        LITUUS_ITEM = registerItem("lituus_item", LituusItem::new, new Item.Properties());
        IMPERIAL_GOLD_INGOT = registerItem("imperial_gold_ingot");
        AQUILA_ARMOR_ITEM = registerItem("aquila_armor");
        AQUILA_SPAWN_EGG_ITEM = registerItem("aquila_spawn_egg", SpawnEggItem::new, new Item.Properties().spawnEgg(CaputMundiEntities.AQUILA));
    }
}