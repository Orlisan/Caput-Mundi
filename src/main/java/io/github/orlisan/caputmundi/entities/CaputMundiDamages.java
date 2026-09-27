package io.github.orlisan.caputmundi.entities;

import io.github.orlisan.caputmundi.CaputMundi;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;

public class CaputMundiDamages {
    public static ResourceKey<DamageType> PILUM_DAMAGE = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.fromNamespaceAndPath(CaputMundi.MOD_ID,"pilum"));
    //public static DamageSource PILUM;
}
