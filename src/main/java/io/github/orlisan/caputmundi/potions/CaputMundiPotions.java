package io.github.orlisan.caputmundi.potions;

import io.github.orlisan.caputmundi.CaputMundi;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.alchemy.Potion;

public class CaputMundiPotions {
    private CaputMundiPotions() {
    }

    public static Holder<Potion> GLOWING;
    public static Holder<Potion> LONG_GLOWING;

    public static Holder<Potion> LAURUS_GLOWING;
    public static Holder<Potion> LAURUS_FIRE_RESISTANCE;
    public static Holder<Potion> LAURUS_REGENERATION;
    public static Holder<Potion> LAURUS_HEALING;
    public static Holder<Potion> LAURUS_INVISIBILITY;
    public static Holder<Potion> LAURUS_HARMING;
    public static Holder<Potion> LAURUS_POISON;
    public static Holder<Potion> LAURUS_LEAPING;
    public static Holder<Potion> LAURUS_SWIFTNESS;
    public static Holder<Potion> LAURUS_NIGHT_VISION;
    public static Holder<Potion> LAURUS_SLOWNESS;
    public static Holder<Potion> LAURUS_STRENGTH;
    public static Holder<Potion> LAURUS_TURTLE_MASTER;
    public static Holder<Potion> LAURUS_WATER_BREATHING;
    public static Holder<Potion> LAURUS_WEAKNESS;
    public static Holder<Potion> LAURUS_LUCK;
    public static Holder<Potion> LAURUS_SLOW_FALLING;
    public static Holder<Potion> LAURUS_WIND_CHARGED;
    public static Holder<Potion> LAURUS_WEAVING;
    public static Holder<Potion> LAURUS_OOZING;
    public static Holder<Potion> LAURUS_INFESTED;

    private static Holder<Potion> register(final ResourceKey<Potion> key, final Potion potion) {
        return Registry.registerForHolder(BuiltInRegistries.POTION, key, potion);
    }

    public static void register() {
        GLOWING = register(CaputMundiPotionsIds.GLOWING, new Potion("glowing", new MobEffectInstance(MobEffects.GLOWING, 3600)));
        LONG_GLOWING = register(CaputMundiPotionsIds.LONG_GLOWING, new Potion("glowing", new MobEffectInstance(MobEffects.GLOWING, 7200)));
        LAURUS_GLOWING = register(CaputMundiPotionsIds.LAURUS_GLOWING, new Potion("glowing", new MobEffectInstance(MobEffects.GLOWING, 3600), new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 1800)));
        LAURUS_FIRE_RESISTANCE = register(CaputMundiPotionsIds.LAURUS_FIRE_RESISTANCE, new Potion("fire_resistance", new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 3600), new MobEffectInstance(MobEffects.RESISTANCE, 1800)));
        LAURUS_REGENERATION = register(CaputMundiPotionsIds.LAURUS_REGENERATION, new Potion("regeneration", new MobEffectInstance(MobEffects.REGENERATION, 900), new MobEffectInstance(MobEffects.ABSORPTION, 450)));
        LAURUS_HEALING = register(CaputMundiPotionsIds.LAURUS_HEALING, new Potion("healing", new MobEffectInstance(MobEffects.INSTANT_HEALTH, 1), new MobEffectInstance(MobEffects.REGENERATION, 450)));
        LAURUS_INVISIBILITY = register(CaputMundiPotionsIds.LAURUS_INVISIBILITY, new Potion("invisibility", new MobEffectInstance(MobEffects.INVISIBILITY, 3600), new MobEffectInstance(MobEffects.SPEED, 1800)));
        LAURUS_HARMING = register(CaputMundiPotionsIds.LAURUS_HARMING, new Potion("harming", new MobEffectInstance(MobEffects.INSTANT_DAMAGE, 1), new MobEffectInstance(MobEffects.POISON, 450)));
        LAURUS_POISON = register(CaputMundiPotionsIds.LAURUS_POISON, new Potion("poison", new MobEffectInstance(MobEffects.POISON, 900), new MobEffectInstance(MobEffects.NAUSEA, 450)));
        LAURUS_LEAPING = register(CaputMundiPotionsIds.LAURUS_LEAPING, new Potion("leaping", new MobEffectInstance(MobEffects.JUMP_BOOST, 3600), new MobEffectInstance(MobEffects.SPEED, 1800)));
        LAURUS_SWIFTNESS = register(CaputMundiPotionsIds.LAURUS_SWIFTNESS, new Potion("swiftness", new MobEffectInstance(MobEffects.SPEED, 3600), new MobEffectInstance(MobEffects.STRENGTH, 1800)));
        LAURUS_NIGHT_VISION = register(CaputMundiPotionsIds.LAURUS_NIGHT_VISION, new Potion("night_vision", new MobEffectInstance(MobEffects.NIGHT_VISION, 3600), new MobEffectInstance(MobEffects.LUCK, 1800)));
        LAURUS_SLOWNESS = register(CaputMundiPotionsIds.LAURUS_SLOWNESS, new Potion("slowness", new MobEffectInstance(MobEffects.SLOWNESS, 1800), new MobEffectInstance(MobEffects.WEAKNESS, 900)));
        LAURUS_STRENGTH = register(CaputMundiPotionsIds.LAURUS_STRENGTH, new Potion("strength", new MobEffectInstance(MobEffects.STRENGTH, 3600), new MobEffectInstance(MobEffects.RESISTANCE, 1800)));
        LAURUS_TURTLE_MASTER = register(CaputMundiPotionsIds.LAURUS_TURTLE_MASTER, new Potion("turtle_master", new MobEffectInstance(MobEffects.SLOWNESS, 400, 3), new MobEffectInstance(MobEffects.RESISTANCE, 400, 2), new MobEffectInstance(MobEffects.WATER_BREATHING, 200)));
        LAURUS_WATER_BREATHING = register(CaputMundiPotionsIds.LAURUS_WATER_BREATHING, new Potion("water_breathing", new MobEffectInstance(MobEffects.WATER_BREATHING, 3600), new MobEffectInstance(MobEffects.CONDUIT_POWER, 1800)));
        LAURUS_WEAKNESS = register(CaputMundiPotionsIds.LAURUS_WEAKNESS, new Potion("weakness", new MobEffectInstance(MobEffects.WEAKNESS, 1800), new MobEffectInstance(MobEffects.SLOWNESS, 900)));
        LAURUS_LUCK = register(CaputMundiPotionsIds.LAURUS_LUCK, new Potion("luck", new MobEffectInstance(MobEffects.LUCK, 6000), new MobEffectInstance(MobEffects.NIGHT_VISION, 3000)));
        LAURUS_SLOW_FALLING = register(CaputMundiPotionsIds.LAURUS_SLOW_FALLING, new Potion("slow_falling", new MobEffectInstance(MobEffects.SLOW_FALLING, 1800), new MobEffectInstance(MobEffects.JUMP_BOOST, 900)));
        LAURUS_WIND_CHARGED = register(CaputMundiPotionsIds.LAURUS_WIND_CHARGED, new Potion("wind_charged", new MobEffectInstance(MobEffects.WIND_CHARGED, 3600), new MobEffectInstance(MobEffects.JUMP_BOOST, 1800)));
        LAURUS_WEAVING = register(CaputMundiPotionsIds.LAURUS_WEAVING, new Potion("weaving", new MobEffectInstance(MobEffects.WEAVING, 3600), new MobEffectInstance(MobEffects.SLOWNESS, 1800)));
        LAURUS_OOZING = register(CaputMundiPotionsIds.LAURUS_OOZING, new Potion("oozing", new MobEffectInstance(MobEffects.OOZING, 3600), new MobEffectInstance(MobEffects.INFESTED, 1800)));
        LAURUS_INFESTED = register(CaputMundiPotionsIds.LAURUS_INFESTED, new Potion("infested", new MobEffectInstance(MobEffects.INFESTED, 3600), new MobEffectInstance(MobEffects.OOZING, 1800)));
    }

}

class CaputMundiPotionsIds {
    public static final ResourceKey<Potion> LAURUS_NIGHT_VISION = register("laurus_night_vision");
    public static final ResourceKey<Potion> LAURUS_INVISIBILITY = register("laurus_invisibility");
    public static final ResourceKey<Potion> LAURUS_LEAPING = register("laurus_leaping");
    public static final ResourceKey<Potion> LAURUS_FIRE_RESISTANCE = register("laurus_fire_resistance");
    public static final ResourceKey<Potion> LAURUS_SWIFTNESS = register("laurus_swiftness");
    public static final ResourceKey<Potion> LAURUS_SLOWNESS = register("laurus_slowness");
    public static final ResourceKey<Potion> LAURUS_TURTLE_MASTER = register("laurus_turtle_master");
    public static final ResourceKey<Potion> LAURUS_WATER_BREATHING = register("laurus_water_breathing");
    public static final ResourceKey<Potion> LAURUS_HEALING = register("laurus_healing");
    public static final ResourceKey<Potion> LAURUS_HARMING = register("laurus_harming");
    public static final ResourceKey<Potion> LAURUS_POISON = register("laurus_poison");
    public static final ResourceKey<Potion> LAURUS_REGENERATION = register("laurus_regeneration");
    public static final ResourceKey<Potion> LAURUS_STRENGTH = register("laurus_strength");
    public static final ResourceKey<Potion> LAURUS_WEAKNESS = register("laurus_weakness");
    public static final ResourceKey<Potion> LAURUS_LUCK = register("laurus_luck");
    public static final ResourceKey<Potion> LAURUS_SLOW_FALLING = register("laurus_slow_falling");
    public static final ResourceKey<Potion> LAURUS_WIND_CHARGED = register("laurus_wind_charged");
    public static final ResourceKey<Potion> LAURUS_WEAVING = register("laurus_weaving");
    public static final ResourceKey<Potion> LAURUS_OOZING = register("laurus_oozing");
    public static final ResourceKey<Potion> LAURUS_INFESTED = register("laurus_infested");
    public static final ResourceKey<Potion> GLOWING = register("glowing");
    public static final ResourceKey<Potion> LONG_GLOWING = register("long_glowing");
    public static final ResourceKey<Potion> LAURUS_GLOWING = register("laurus_glowing");

    static ResourceKey<Potion> register(String name) {
        return ResourceKey.create(Registries.POTION, Identifier.fromNamespaceAndPath(CaputMundi.MOD_ID, name));
    }
}
