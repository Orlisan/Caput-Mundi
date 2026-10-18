package io.github.orlisan.caputmundi.client.mixin;

import io.github.orlisan.caputmundi.accessors.SwingAnimationAccessor;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidModel.class)
public class HumanoidModelMixin{
    @Shadow
    public ModelPart getArm(HumanoidArm arm){return null;}

        @Inject(method="setupAttackAnimation", at=@At("HEAD"))

        protected void setupAttackAnimation(final HumanoidRenderState state, CallbackInfo ci) {
            float attackTime = state.swingAnimation;
            LivingEntity.SwingDescription currentSwing = state.currentSwing;
            if (!(attackTime <= 0.0F) && currentSwing != null && (Object) currentSwing.animation().type() instanceof SwingAnimationAccessor ac && ac.caput_mundi$isGladius()) {
                ModelPart attackArm = this.getArm(state.mainArm);
                attackArm.xRot += 0.2f;
            }
    }
}
