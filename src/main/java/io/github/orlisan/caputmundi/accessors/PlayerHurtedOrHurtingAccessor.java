package io.github.orlisan.caputmundi.accessors;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public interface PlayerHurtedOrHurtingAccessor {
    boolean caput_mundi$isHurtedOrHurting();
    LivingEntity caput_mundi$aquilaTarget();
    void caput_mundi$setAquilaTarget(LivingEntity entity);
}
