package io.github.orlisan.caputmundi.mixin;

import io.github.orlisan.caputmundi.accessors.PlayerHurtedOrHurtingAccessor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityHurtOrHurtingMixin implements PlayerHurtedOrHurtingAccessor {
    @Unique
    public boolean nnhovogliadiriscriverlo = false;
    @Unique
    public LivingEntity aquilaTarget = null;
    @Override
    public void caput_mundi$setAquilaTarget(LivingEntity target) {
        nnhovogliadiriscriverlo = true;
        aquilaTarget = target;
    }
    @Override
    public boolean caput_mundi$isHurtedOrHurting() {
        return nnhovogliadiriscriverlo;
    }

    @Override
    public LivingEntity caput_mundi$aquilaTarget() {
        return aquilaTarget;
    }

    @Inject(method = "hurtServer", at = @At("RETURN"))
    public void settaFlag(ServerLevel level, DamageSource source, float damage, CallbackInfoReturnable<?> cir) {
        if (cir.getReturnValueZ() && source.getEntity() instanceof LivingEntity livingEntity) {
            caput_mundi$setAquilaTarget(livingEntity);
        }
    }

    @Inject(method = "doHurtTarget", at = @At("RETURN"))
    public void settaFlag2(ServerLevel level, Entity target, CallbackInfoReturnable<?> cir) {
        if (target instanceof LivingEntity entity) {
            caput_mundi$setAquilaTarget(entity);
        }
    }
}
