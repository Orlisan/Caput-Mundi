package io.github.orlisan.caputmundi.entities.goals;

import io.github.orlisan.caputmundi.entities.AquilaEntity;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class RuotaInCerchioGoal extends AquilaGoalConCostruttore {


    public RuotaInCerchioGoal(AquilaEntity entity) {
        super(entity);
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        return entity.ruotaInCerchio;
    }

    @Override
    public void start() {
        super.start();
        entity.isFlying = true;
    }
    //TODO Scegliere centro cerchio e ruotare
    @Override
    public void tick() {
        super.tick();

    }
}
