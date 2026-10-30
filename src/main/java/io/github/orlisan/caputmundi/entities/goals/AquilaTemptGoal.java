package io.github.orlisan.caputmundi.entities.goals;

import io.github.orlisan.caputmundi.entities.AquilaEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.item.ItemStack;

import java.util.function.Predicate;

public class AquilaTemptGoal extends TemptGoal {
    public AquilaTemptGoal(PathfinderMob mob, double speedModifier, Predicate<ItemStack> items, boolean canScare) {
        super(mob, speedModifier, items, canScare);
    }

    @Override
    public boolean canUse() {
        AquilaEntity mob1 = (AquilaEntity) mob;
        return mob1.padrone == null && super.canUse();
    }
}
