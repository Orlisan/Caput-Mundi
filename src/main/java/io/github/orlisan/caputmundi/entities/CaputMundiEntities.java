package io.github.orlisan.caputmundi.entities;

import io.github.orlisan.caputmundi.CaputMundi;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biomes;
import org.jetbrains.annotations.NotNull;

public class CaputMundiEntities {
    private CaputMundiEntities() {
    }

    public static final EntityType<@NotNull AquilaEntity> AQUILA = Registry.register(BuiltInRegistries.ENTITY_TYPE,
            Identifier.fromNamespaceAndPath(CaputMundi.MOD_ID, "aquila"),
            EntityType.Builder.of(AquilaEntity::new, MobCategory.CREATURE).sized(1.5f, 0.4f).build(keyOf("aquila"))
    );

    private static ResourceKey<EntityType<?>> keyOf(String name) {
        return ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(CaputMundi.MOD_ID, name));
    }

    public static void register() {
        FabricDefaultAttributeRegistry.register(AQUILA, AquilaEntity.createAttributes());
        BiomeModifications.addSpawn(BiomeSelectors.includeByKey(
                        Biomes.TAIGA,
                        Biomes.SNOWY_TAIGA,
                        Biomes.JAGGED_PEAKS,
                        Biomes.FROZEN_PEAKS,
                        Biomes.STONY_PEAKS,
                        Biomes.WINDSWEPT_FOREST
                ), MobCategory.CREATURE, AQUILA, 10, 1, 1
        );
    }
}
