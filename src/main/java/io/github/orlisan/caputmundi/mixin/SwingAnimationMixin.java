package io.github.orlisan.caputmundi.mixin;

import io.github.orlisan.caputmundi.accessors.SwingAnimationAccessor;
import net.minecraft.world.item.component.SwingAnimation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(SwingAnimation.class)
public class SwingAnimationMixin implements SwingAnimationAccessor {
    @Override
    public boolean caput_mundi$isGladius() {
        return isGladius;
    }
    @Unique
    boolean isGladius = false;

    @Unique
    boolean isPugio = false;
    @Override
    public void caput_mundi$setGladius(boolean isGladius) {
        this.isGladius = isGladius;
    }

    @Override
    public boolean caput_mundi$isPugio() {
        return isPugio;
    }

    @Override
    public void caput_mundi$setPugio(boolean isPugio) {
        this.isPugio = isPugio;
    }
}
