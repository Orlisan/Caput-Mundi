package io.github.orlisan.caputmundi.mixin;

import io.github.orlisan.caputmundi.CaputMundi;
import io.github.orlisan.caputmundi.items.CaputMundiItems;
import io.github.orlisan.caputmundi.potions.CaputMundiPotions;
import net.minecraft.core.Holder;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static io.github.orlisan.caputmundi.potions.CaputMundiPotions.*;
import static io.github.orlisan.caputmundi.items.CaputMundiItems.LAURUS_LEAF;
import static net.minecraft.world.item.alchemy.Potions.*;

@Mixin(PotionBrewing.class)
public class PotionBrewingMixin {

    @Inject(method = "addVanillaMixes", at = @At("TAIL"))
    private static void addCaputMundiPotions(final PotionBrewing.Builder builder, CallbackInfo ci) {
        CaputMundi.LOGGER.info("Mixin Pozioni chiamato!");
        builder.addStartMix(LAURUS_LEAF, GLOWING);
        builder.addStartMix(Items.WHEAT, LUCK);
        builder.addMix(GLOWING, Items.REDSTONE, LONG_GLOWING);

        builder.addMix(GLOWING, LAURUS_LEAF, LAURUS_GLOWING);
        builder.addMix(FIRE_RESISTANCE, LAURUS_LEAF, LAURUS_FIRE_RESISTANCE);
        builder.addMix(REGENERATION, LAURUS_LEAF, LAURUS_REGENERATION);
        builder.addMix(HEALING, LAURUS_LEAF, LAURUS_HEALING);
        builder.addMix(INVISIBILITY, LAURUS_LEAF, LAURUS_INVISIBILITY);
        builder.addMix(HARMING, LAURUS_LEAF, LAURUS_HARMING);
        builder.addMix(POISON, LAURUS_LEAF, LAURUS_POISON);
        builder.addMix(LEAPING, LAURUS_LEAF, LAURUS_LEAPING);
        builder.addMix(SWIFTNESS, LAURUS_LEAF, LAURUS_SWIFTNESS);
        builder.addMix(NIGHT_VISION, LAURUS_LEAF, LAURUS_NIGHT_VISION);
        builder.addMix(SLOWNESS, LAURUS_LEAF, LAURUS_SLOWNESS);
        builder.addMix(STRENGTH, LAURUS_LEAF, LAURUS_STRENGTH);
        builder.addMix(TURTLE_MASTER, LAURUS_LEAF, LAURUS_TURTLE_MASTER);
        builder.addMix(WATER_BREATHING, LAURUS_LEAF, LAURUS_WATER_BREATHING);
        builder.addMix(WEAKNESS, LAURUS_LEAF, LAURUS_WEAKNESS);
        builder.addMix(LUCK, LAURUS_LEAF, LAURUS_LUCK);
        builder.addMix(SLOW_FALLING, LAURUS_LEAF, LAURUS_SLOW_FALLING);
        builder.addMix(WIND_CHARGED, LAURUS_LEAF, LAURUS_WIND_CHARGED);
        builder.addMix(WEAVING, LAURUS_LEAF, LAURUS_WEAVING);
        builder.addMix(OOZING, LAURUS_LEAF, LAURUS_OOZING);
        builder.addMix(INFESTED, LAURUS_LEAF, LAURUS_INFESTED);
    }
}
