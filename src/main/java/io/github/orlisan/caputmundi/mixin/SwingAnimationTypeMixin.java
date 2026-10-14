package io.github.orlisan.caputmundi.mixin;

import io.github.orlisan.caputmundi.accessors.SwingAnimationAccessor;
import net.minecraft.world.item.SwingAnimationType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(SwingAnimationType.class)
public class SwingAnimationTypeMixin implements SwingAnimationAccessor {
    @Override
    public boolean caput_mundi$isGladius() {
        return isGladius;
    }
    @Unique
    boolean isGladius = false;
    @Override
    public void caput_mundi$setGladius(boolean isGladius) {
        this.isGladius = isGladius;
    }
}
