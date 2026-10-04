package io.github.orlisan.caputmundi.client.mixin;

import io.github.orlisan.caputmundi.accessors.PilaCountAccessor;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
@Mixin(AvatarRenderState.class)
public class AvatarRenderStateMixin implements PilaCountAccessor {
    @Override
    public int caput_mundi$getPilaCount() {
        return pilaCount;
    }

    @Override
    public void caput_mundi$setPilaCount(int pilaCount) {
       // if(this.pilaCount != pilaCount) IO.println("Pila count cambiato!, vecchio: "+this.pilaCount+" nuovo: "+pilaCount);
        this.pilaCount = pilaCount;
    }
    @Unique
    public int pilaCount = 0;
}
