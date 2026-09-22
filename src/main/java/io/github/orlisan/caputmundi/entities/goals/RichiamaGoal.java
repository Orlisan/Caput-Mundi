package io.github.orlisan.caputmundi.entities.goals;

import io.github.orlisan.caputmundi.entities.AquilaEntity;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class  RichiamaGoal extends AquilaGoalConCostruttore{
    public RichiamaGoal(AquilaEntity entity) {
        super(entity);
        setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        return entity.richiama && entity.padrone != null;
    }
    Vec3 positionToAtter;
    int ranX, ranZ;
    @Override
    public void start() {
        super.start();
        RandomSource RANDOM = RandomSource.create();
        ranX = RANDOM.nextInt(-1, 2);
        ranZ = RANDOM.nextInt(-1, 2);
        positionToAtter = new Vec3(entity.padrone.getX() + ranX, entity.padrone.getOnPos().getY(), entity.padrone.getZ() + ranZ);
    }

    @Override
    public void tick() {
        super.tick();
        positionToAtter = new Vec3(entity.padrone.getX() + ranX, entity.padrone.getOnPos().getY(), entity.padrone.getZ() + ranZ);

        entity.getNavigation().moveTo(positionToAtter.x, positionToAtter.y, positionToAtter.z(), 1.0f);
        double v = entity.position().distanceTo(positionToAtter);
        if (v < 2) {
            entity.richiama = false;
        } else if (v < 3) {
            entity.setStartAtterraggioAnim(true);
        }
    }

    @Override
    public void stop() {
             entity.isFlying = false;
    }
}

