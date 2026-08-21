package io.github.orlisan.caputmundi.entities.goals;

import io.github.orlisan.caputmundi.CaputMundi;
import io.github.orlisan.caputmundi.entities.AquilaEntity;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.phys.AABB;

import java.util.EnumSet;

public class AttaccoDistanzaGoal extends AquilaGoalConCostruttore {

    public AttaccoDistanzaGoal(AquilaEntity entity) {
        super(entity);
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK, Flag.TARGET));
    }

    @Override
    public boolean canUse() {
        return entity.distanceAttack;
    }

    double initialY;

    @Override
    public void start() {
        super.start();
        initialY = entity.getY();
        if (entity.getTarget() != null) {
            if (entity.distanceTo(entity.getTarget()) < 3.0f) {
                entity.distanceAttack = false;
            }
            entity.getNavigation().stop();
            CaputMundi.LOGGER.info("Stop navigation, moveTo sta per essere chiamato. entity:{}", entity.getId());
            entity.getNavigation().moveTo(entity.getTarget(), 5.0);
            entity.setAggressive(true);
        } else {
            AABB aabb = new AABB(entity.position().x - 8, entity.position().y - 40, entity.position().z - 8,
                    entity.position().x + 8, entity.position().y, entity.position().z + 8);
            RandomSource random = RandomSource.create();
            boolean founded = false;
            for (LivingEntity mob : entity.level().getEntitiesOfClass(Animal.class, aabb)) {
                if (random.nextDouble() > 0.3) {
                    entity.setTarget(mob);
                    entity.getNavigation().stop();
                    CaputMundi.LOGGER.info("Stop navigation, moveTo sta per essere chiamato. entity:{}", entity.getId());
                    entity.getNavigation().moveTo(entity.getTarget(), 5.0);
                    entity.getLookControl().setLookAt(entity.getTarget());
                    entity.setAggressive(true);
                    founded = true;
                    break;
                }
            }
            if (!founded) entity.distanceAttack = false;
        }
    }

    @Override
    public void tick() {
        super.tick();

        if (entity.getTarget() != null && entity.distanceTo(entity.getTarget()) < 3.0f) {
            entity.getTarget().hurtServer(getServerLevel(entity.level()), this.entity.level().damageSources().fall(), (float) ((initialY - entity.getY()) / 1.5));
            entity.distanceAttack = false;
            entity.vicinoAttack = true;
        } else if (entity.getTarget() != null) {
            entity.getNavigation().stop();
            CaputMundi.LOGGER.info("Stop navigation, moveTo sta per essere chiamato. entity:{}", entity.getId());
            entity.getNavigation().moveTo(entity.getTarget(), 5.0);
            entity.getLookControl().setLookAt(entity.getTarget());
        }
    }
}
