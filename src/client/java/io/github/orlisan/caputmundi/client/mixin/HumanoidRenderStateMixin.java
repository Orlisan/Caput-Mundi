package io.github.orlisan.caputmundi.client.mixin;

import io.github.orlisan.caputmundi.accessors.ArmPoseAccessor;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(HumanoidRenderState.class)
public class HumanoidRenderStateMixin implements ArmPoseAccessor {

    @Unique
    boolean scutumRight = false;
    @Unique
    boolean scutumLeft = false;


    @Override
    public boolean caput_mundi$isScutumRight() {
        return scutumRight;
    }

    @Override
    public void caput_mundi$setIsScutumRight(boolean flag) {
        this.scutumRight = flag;
    }

    @Override
    public boolean caput_mundi$isScutumLeft() {
        return scutumLeft;
    }

    @Override
    public void caput_mundi$setIsScutumLeft(boolean flag) {
        scutumLeft = flag;
    }

}
