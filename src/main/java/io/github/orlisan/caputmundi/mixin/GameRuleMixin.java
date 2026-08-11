/*package io.github.orlisan.caputmundi.mixin;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.serialization.Codec;
import io.github.orlisan.caputmundi.CaputMundiConstants;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.level.gamerules.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.ToIntFunction;

@Mixin(GameRules.class)
public class GameRuleMixin {

    @ModifyVariable(method = "<init>(Ljava/util/List;)V", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private static List<GameRule<?>> mettiRules(List<GameRule<?>> rules) {
        List<GameRule<?>> copy = new ArrayList<>(rules);
        copy.add(CaputMundiConstants.AQUILA_VIEW_UPDATE_TICKS);
        return copy;
    }


}*/
