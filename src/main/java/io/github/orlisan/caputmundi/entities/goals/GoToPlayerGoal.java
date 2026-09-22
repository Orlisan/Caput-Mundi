package io.github.orlisan.caputmundi.entities.goals;

import io.github.orlisan.caputmundi.entities.AquilaEntity;

import java.util.EnumSet;

public class GoToPlayerGoal extends AquilaGoalConCostruttore{
    public GoToPlayerGoal(AquilaEntity entity) {
        super(entity);
        setFlags(EnumSet.of(Flag.LOOK, Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        return entity.playerToSpy != null;
    }

    @Override
    public void tick() {
        super.tick();
        this.entity.getNavigation().moveTo(entity.playerToSpy.getX(), entity.playerToSpy.getY()+50, entity.playerToSpy.getZ(), 2.0f);
    }
}
