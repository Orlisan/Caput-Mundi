package io.github.orlisan.caputmundi.entities;

import io.github.orlisan.caputmundi.CaputMundi;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.item.alchemy.Potion;

public class CaputMundiDamages {
    public static ResourceKey<DamageType> PILUM_DAMAGE = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.fromNamespaceAndPath(CaputMundi.MOD_ID,"pilum"));
    public static ResourceKey<DamageType> PLUMBATA_DAMAGE = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.fromNamespaceAndPath(CaputMundi.MOD_ID,"plumbata"));
   // public static ResourceKey<DamageType> GLADIUS_DAMAGE = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.fromNamespaceAndPath(CaputMundi.MOD_ID,"gladius"));
   // public static ResourceKey<DamageType> PUGIO_DAMAGE = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.fromNamespaceAndPath(CaputMundi.MOD_ID,"pugio"));


}
