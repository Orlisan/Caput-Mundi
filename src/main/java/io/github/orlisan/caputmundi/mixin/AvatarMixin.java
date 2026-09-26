package io.github.orlisan.caputmundi.mixin;

import io.github.orlisan.caputmundi.PilaCountAccessor;
import net.minecraft.world.entity.Avatar;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Avatar.class)
public class AvatarMixin implements PilaCountAccessor {
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
