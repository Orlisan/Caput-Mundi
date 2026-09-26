package io.github.orlisan.caputmundi.client.mixin;

import io.github.orlisan.caputmundi.PilaCountAccessor;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
@Mixin(AvatarRenderState.class)
public class AvatarRenderStateMixin implements PilaCountAccessor {
    @Override
    public int getPila$Count() {
        return pilaCount;
    }

    @Override
    public void setPila$Count(int pilaCount) {
        this.pilaCount = pilaCount;
    }
    @Unique
    public int pilaCount = 0;
}
