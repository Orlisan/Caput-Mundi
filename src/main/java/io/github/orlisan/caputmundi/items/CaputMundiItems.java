package io.github.orlisan.caputmundi.items;

import com.mojang.serialization.Codec;
import io.github.orlisan.caputmundi.CaputMundi;
import io.github.orlisan.caputmundi.blocks.CaputMundiBlocks;
import io.github.orlisan.caputmundi.entities.CaputMundiEntities;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.references.ItemIds;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.component.BlocksAttacks;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BannerPatternLayers;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class CaputMundiItems {
    public static Item LAURUS_LEAF, LAURUS_SEED;
    public static Item LAURUS_CROWN, GOLDEN_LAURUS_CROWN;
    public static Item IMPERIAL_GOLD_INGOT, IMPERIAL_GOLD_WHEAT;
    public static Item AQUILA_SPAWN_EGG_ITEM, AQUILA_ARMOR_ITEM, LITUUS_ITEM;
    public static Item PILUM, GLADIUS, PUGIO, PLUMBATA, SCUTUM;

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

    public static final DataComponentType<Integer> PLUMBATA_COUNT =
            Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE,
                    Identifier.fromNamespaceAndPath(CaputMundi.MOD_ID, "plumbata_count"),
                    DataComponentType.<Integer>builder().persistent(Codec.INT)
                            .networkSynchronized(ByteBufCodecs.VAR_INT).build());

    public static final DataComponentType<Integer> SCUTUM_COLOR =
            Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE,
                    Identifier.fromNamespaceAndPath(CaputMundi.MOD_ID, "scutum_color"),
                    DataComponentType.<Integer>builder().persistent(Codec.INT)
                            .networkSynchronized(ByteBufCodecs.VAR_INT).build());


    public static void register() {
        LAURUS_LEAF = registerItem("laurus_leaves");
        LAURUS_SEED = registerBlockItem("laurus_seed", CaputMundiBlocks.LAURUS_CROP);
        LAURUS_CROWN = registerItem("laurus_crown", LaurusCrownItem::new, new Item.Properties().durability(360).equippable(EquipmentSlot.HEAD));
        GOLDEN_LAURUS_CROWN = registerItem("golden_laurus_crown", LaurusCrownItem::new, new Item.Properties().durability(720).equippable(EquipmentSlot.HEAD));
        LITUUS_ITEM = registerItem("lituus_item", LituusItem::new, new Item.Properties());
        IMPERIAL_GOLD_INGOT = registerItem("imperial_gold_ingot");
        AQUILA_ARMOR_ITEM = registerItem("aquila_armor");
        AQUILA_SPAWN_EGG_ITEM = registerItem("aquila_spawn_egg", SpawnEggItem::new, new Item.Properties().spawnEgg(CaputMundiEntities.AQUILA));
        PILUM = registerItem("pilum_item", PilumItem::new, new Item.Properties().attributes(PilumItem.createAttributes()).stacksTo(1));
        GLADIUS = registerItem("gladius_item", GladiusItem::new, new Item.Properties());
        PUGIO = registerItem("pugio_item", PugioItem::new, new Item.Properties());
        IMPERIAL_GOLD_WHEAT = registerItem("imperial_gold_wheat");
        PLUMBATA = registerItem("plumbata_item", PlumbataItem::new, new Item.Properties());
        //SCUTUM = registerItem("scutum_item", ShieldItem::new, new Item.Properties());
        SCUTUM = registerItem("scutum_item", ScutumItem::new, (new Item.Properties()).durability(436).repairable(ItemTags.WOODEN_TOOL_MATERIALS)
                .equippableUnswappable(EquipmentSlot.OFFHAND).delayedComponent(DataComponents.BLOCKS_ATTACKS, (context) ->
                        new BlocksAttacks(0.35F, 1.0F, List.of(new BlocksAttacks.DamageReduction(135.0F, Optional.empty(),
                                0.0F, 1.0F)), new BlocksAttacks.ItemDamageFunction(3.5F, 1.0F, 1.0F),
                                Optional.of(context.getOrThrow(DamageTypeTags.BYPASSES_SHIELD)), Optional.of(SoundEvents.SHIELD_BLOCK),
                                Optional.of(SoundEvents.SHIELD_BREAK))).component(DataComponents.BREAK_SOUND, SoundEvents.SHIELD_BREAK));

    }
}