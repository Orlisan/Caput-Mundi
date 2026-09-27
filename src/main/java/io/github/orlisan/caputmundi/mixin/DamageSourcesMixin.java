/*package io.github.orlisan.caputmundi.mixin;

import io.github.orlisan.caputmundi.entities.CaputMundiDamages;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.damagesource.DamageSources;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DamageSources.class)
public class DamageSourcesMixin {
    @Inject(at=@At("TAIL"), method="<init>")
    public void addMyDamages(RegistryAccess registries, CallbackInfo ci) {
        CaputMundiDamages.PILUM = ((DamageSources) (Object) this).source(CaputMundiDamages.PILUM_DAMAGE);
    }
}*/
