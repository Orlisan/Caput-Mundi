package io.github.orlisan.caputmundi.entities.goals;

import io.github.orlisan.caputmundi.entities.AquilaEntity;

public class MoveToCoordGoal extends AquilaGoalConCostruttore {
    public MoveToCoordGoal(AquilaEntity entity) {
        super(entity);
    }

    @Override
    public boolean canUse() {
        return entity.coordsToMove != AquilaEntity.NO_MOVES;
    }

    @Override
    public void tick() {
        if (entity.position().distanceTo(entity.coordsToMove) < 2.0) entity.coordsToMove = AquilaEntity.NO_MOVES;
        else entity.getNavigation().moveTo(entity.coordsToMove.x, entity.coordsToMove.y, entity.coordsToMove.z, 1.5f);
        super.tick();
    }
}
