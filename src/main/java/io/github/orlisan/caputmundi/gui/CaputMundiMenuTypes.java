package io.github.orlisan.caputmundi.gui;

import io.github.orlisan.caputmundi.CaputMundi;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import org.jetbrains.annotations.NotNull;

public class CaputMundiMenuTypes{
    public static final MenuType<@NotNull LituusMenu> LITUUS_MENU = Registry.register(
            BuiltInRegistries.MENU, Identifier.fromNamespaceAndPath(CaputMundi.MOD_ID,"lituus_menu"),
            new MenuType<>(LituusMenu::new, FeatureFlags.DEFAULT_FLAGS)
    );
}
