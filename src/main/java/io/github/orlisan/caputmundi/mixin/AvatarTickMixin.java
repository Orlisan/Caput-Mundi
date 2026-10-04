package io.github.orlisan.caputmundi.mixin;

import io.github.orlisan.caputmundi.accessors.PilaCountAccessor;
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
            if(accessor.caput_mundi$getPilaCount() > 0 && Math.random() <= 0.001) {
                IO.println("Prima di rimuovere: "+accessor.caput_mundi$getPilaCount());
                accessor.caput_mundi$setPilaCount(accessor.caput_mundi$getPilaCount() - 1);
                IO.println("Pilum Tolto di mezzo: "+accessor.caput_mundi$getPilaCount());
            }
        }
    }
}
