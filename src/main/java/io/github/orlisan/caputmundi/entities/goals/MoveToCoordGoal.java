package io.github.orlisan.caputmundi.entities.goals;

import io.github.orlisan.caputmundi.entities.AquilaEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class MoveToCoordGoal extends Goal {
    MovableToCoord entity;
    float minDistance;

    public MoveToCoordGoal(MovableToCoord entity, float minDistance) {
        this.entity = entity;
        this.minDistance = minDistance;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        return entity.coordsToMove() != AquilaEntity.NO_MOVES;
    }

    int tickCounter = -1;

    @Override
    public void tick() {
        tickCounter++;
        if (tickCounter == 10) tickCounter = 0;
        if (entity.position().distanceTo(entity.coordsToMove()) < minDistance) entity.setCoordsToMove(AquilaEntity.NO_MOVES);
        else if (tickCounter == 0)
            entity.getNavigation().moveTo(entity.coordsToMove().x, entity.coordsToMove().y, entity.coordsToMove().z, 1.5f);
        super.tick();
    }

    public interface MovableToCoord {
        PathNavigation getNavigation();
        Vec3 coordsToMove();
        void setCoordsToMove(Vec3 move);
        Vec3 position();
    }
}
