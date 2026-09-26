package io.github.orlisan.caputmundi.entities;

import io.github.orlisan.caputmundi.CaputMundi;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.core.DefaultedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
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

    public static final EntityType<@NotNull PilumLanciato> PILUM = register(
            Identifier.fromNamespaceAndPath(CaputMundi.MOD_ID, "pilum"),
            EntityType.Builder.<PilumLanciato>of(PilumLanciato::new, MobCategory.MISC).noLootTable().sized(0.5F, 0.5F).eyeHeight(0.13F).clientTrackingRange(4).updateInterval(20)
    );



    private static ResourceKey<EntityType<?>> keyOf(String name) {
        return ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(CaputMundi.MOD_ID, name));
    }

    private static  EntityType<@NotNull PilumLanciato> register(final Identifier id, final EntityType.Builder<PilumLanciato> builder) {
        return Registry.register(BuiltInRegistries.ENTITY_TYPE, id, builder.build(ResourceKey.create(Registries.ENTITY_TYPE, id)));
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
