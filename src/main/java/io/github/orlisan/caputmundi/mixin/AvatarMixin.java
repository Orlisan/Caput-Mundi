package io.github.orlisan.caputmundi.mixin;

import io.github.orlisan.caputmundi.accessors.PilaCountAccessor;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Avatar;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Avatar.class)
public class AvatarMixin implements PilaCountAccessor {

    @Unique
    private static final EntityDataAccessor<Integer> pilaCount = SynchedEntityData.defineId(Avatar.class, EntityDataSerializers.INT);

    @Override
    public int caput_mundi$getPilaCount() {
        return ((Avatar) (Object) this).getEntityData().get(pilaCount);
    }

    @Override
    public void caput_mundi$setPilaCount(int pilaCount) {
        ((Avatar) (Object) this).getEntityData().set(this.pilaCount, pilaCount);
    }

    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    protected void addMydefineSynchedData(final SynchedEntityData.Builder entityData, CallbackInfo ci) {
        entityData.define(pilaCount, 0);
    }
}
