package io.github.orlisan.caputmundi.mixin;

import io.github.orlisan.caputmundi.PilaCountAccessor;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class AvatarTickMixin {
    @Inject(method="tick", at=@At("TAIL"))
    public void ticcaPila(CallbackInfo ci) {
        if(this instanceof PilaCountAccessor accessor) {
            if(accessor.getPila$Count() > 0 && Math.random() <= 0.0001) {
                accessor.setPila$Count(accessor.getPila$Count()-1);
            }
        }
    }
}
