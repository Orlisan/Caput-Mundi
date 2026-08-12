package io.github.orlisan.caputmundi.entities.goals;

import io.github.orlisan.caputmundi.entities.AquilaEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;

public class AttaccoRavvicinatoGoal extends MeleeAttackGoal {
    public AttaccoRavvicinatoGoal(PathfinderMob mob, double speedModifier, boolean followingTargetEvenIfNotSeen) {
        super(mob, speedModifier, followingTargetEvenIfNotSeen);
    }

    @Override
    public boolean canUse() {
        return super.canUse() && ((AquilaEntity) mob).vicinoAttack;
    }

    @Override
    public boolean canContinueToUse() {
        return super.canContinueToUse() && ((AquilaEntity) mob).vicinoAttack;
    }

    @Override
    public void stop() {
        if (mob instanceof AquilaEntity entity) {
            entity.startDecollo = true;
        }
        super.stop();
    }
}
