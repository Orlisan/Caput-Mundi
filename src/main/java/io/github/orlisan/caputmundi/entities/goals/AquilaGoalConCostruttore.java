package io.github.orlisan.caputmundi.entities.goals;

import io.github.orlisan.caputmundi.entities.AquilaEntity;
import net.minecraft.world.entity.ai.goal.Goal;

public abstract class AquilaGoalConCostruttore extends Goal {
    public AquilaEntity entity;

    public AquilaGoalConCostruttore(AquilaEntity entity) {
        this.entity = entity;
    }

}
