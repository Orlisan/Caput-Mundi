package io.github.orlisan.caputmundi.mixin;

import io.github.orlisan.caputmundi.accessors.PlayerHurtedOrHurtingAccessor;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import net.minecraft.world.phys.HitResult.Type;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Projectile.class)
public class LivingEntityHurtingByProjectileMixin {
    @Inject(method="onHit", at=@At("HEAD"))
    public void ownerIsHurting(final HitResult hitResult, CallbackInfo ci) {
        HitResult.Type type = hitResult.getType();
        if (type == Type.ENTITY) {
            EntityHitResult entityHitResult = (EntityHitResult) hitResult;
            Entity owner = ((Projectile) (Object) this).getOwner();
            if(owner instanceof PlayerHurtedOrHurtingAccessor ac && entityHitResult.getEntity() instanceof LivingEntity target) {
                ac.caput_mundi$setAquilaTarget(target);
            }
        }
    }
}
