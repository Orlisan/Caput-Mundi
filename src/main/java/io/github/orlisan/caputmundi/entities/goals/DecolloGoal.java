package io.github.orlisan.caputmundi.entities.goals;

import io.github.orlisan.caputmundi.CaputMundi;
import io.github.orlisan.caputmundi.entities.AquilaEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;
import java.util.Objects;

public class DecolloGoal extends AquilaGoalConCostruttore {
    public static final BlockPos failPos = new BlockPos(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE);

    public DecolloGoal(AquilaEntity entity) {
        super(entity);
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        return entity.startDecollo;
    }

    @Override
    public boolean canContinueToUse() {
        return !entity.getNavigation().isDone();
    }

  /*  @Override
    public void tick() {
        if (isSimilarPos(targetPos, entity.blockPosition())) {
            entity.startDecollo = false;
        }
        super.tick();
    }*/

    static boolean isSimilarPos(BlockPos a, BlockPos b) {
        return Math.abs(a.getX() - b.getX()) < 1 &&
               Math.abs(a.getY() - b.getY()) < 1 &&
               Math.abs(a.getZ() - b.getZ()) < 1;
    }

    BlockPos targetPos;

    @Override
    public void start() {
        targetPos = findGoodStartPlace(Objects.requireNonNullElse(entity.padrone, entity).getOnPos());
        CaputMundi.LOGGER.info("Target pos: {}", targetPos);
        if (targetPos != failPos) {
            entity.isFlying = true;
            entity.getNavigation().moveTo(targetPos.getX(), targetPos.getY(), targetPos.getZ(), 1.0);
            entity.getLookControl().setLookAt(Vec3.atLowerCornerOf(targetPos));
        } else {
            entity.startDecollo = false;
        }
    }


    public BlockPos findGoodStartPlace(BlockPos currentPlace) {
        Level level = entity.level();
        for (int y = currentPlace.getY() + 30; y > currentPlace.getY(); y--) {
            for (int x = currentPlace.getX() - 10; x <= currentPlace.getX() + 10; x++) {
                for (int z = currentPlace.getZ() - 10; z <= currentPlace.getZ() + 10; z++) {
                    BlockPos pos = new BlockPos(x, y, z);
                    if (level.isEmptyBlock(pos)) {
                        CaputMundi.LOGGER.info("Posizione: {}", pos);
                        return pos;
                    }
                }
            }
        }
        return failPos;
    }

    @Override
    public void stop() {
        entity.startDecollo = false;
        super.stop();
    }
}
