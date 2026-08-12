package io.github.orlisan.caputmundi.items;

import io.github.orlisan.caputmundi.CaputMundi;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class CaputMundiCreativeTab {
    public static void register() {
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, Identifier.fromNamespaceAndPath(CaputMundi.MOD_ID, "caput_mundi_creative_tab"),
                CreativeModeTab.builder(CreativeModeTab.Row.TOP, 7)
                        .title(Component.translatable("caput.mundi.tab"))
                        .icon(() -> new ItemStack(CaputMundiItems.LAURUS_CROWN))
                        .displayItems((params, output) -> {
                            output.accept(CaputMundiItems.LAURUS_CROWN);
                            output.accept(CaputMundiItems.LAURUS_LEAF);
                            output.accept(CaputMundiItems.LAURUS_SEED);
                            output.accept(CaputMundiItems.IMPERIAL_GOLD_INGOT);
                            output.accept(CaputMundiItems.AQUILA_SPAWN_EGG_ITEM);
                            //       output.accept(CaputMundiItems.GOLDEN_LAURUS_CROWN);
                        })
                        .build());
    }
}
