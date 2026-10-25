package io.github.orlisan.caputmundi.client.mixin;

import io.github.orlisan.caputmundi.accessors.ArmPoseAccessor;
import io.github.orlisan.caputmundi.accessors.SwingAnimationAccessor;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidModel.class)
public abstract class HumanoidModelMixin {
    @Shadow
    public ModelPart getArm(HumanoidArm arm) {
        return null;
    }

    @Shadow
    protected abstract void poseBlockingArm(ModelPart arm, boolean right);

    @Shadow
    @Final
    public ModelPart rightArm;

    @Shadow
    @Final
    public ModelPart leftArm;

    @Inject(method = "poseRightArm", at = @At("TAIL"))
    void poseRightBraccio(HumanoidRenderState state, CallbackInfo ci) {
        //Muove l'altro braccio
        if (((ArmPoseAccessor) state).caput_mundi$isScutumLeft()) {
            this.rightArm.zRot = (float) rad(-10);
            this.rightArm.xRot = (float) rad(15);
        }/*else if(((ArmPoseAccessor) state).caput_mundi$isScutumLeft()) {
            //Posa tridente
            this.rightArm.xRot = this.rightArm.xRot * 0.5F - (float)Math.PI;
            this.rightArm.yRot = 0.0F;
        }*/
    }

    @Unique
    double rad(double deg) {
        return Math.toRadians(deg);
    }

    @Inject(method = "poseLeftArm", at = @At("TAIL"))
    void poseLeftBraccio(HumanoidRenderState state, CallbackInfo ci) {
        //Muove l'altro braccio
        if (((ArmPoseAccessor) state).caput_mundi$isScutumRight()) {
            this.leftArm.zRot = (float) rad(10);
            this.leftArm.xRot = (float) rad(-15);
        }/*else if(((ArmPoseAccessor) state).caput_mundi$isScutumLeft()) {
            //Posa tqridente
            this.leftArm.xRot = this.leftArm.xRot * 0.5F - (float)Math.PI;
            this.leftArm.yRot = 0.0F;
        }*/
    }

    @Inject(method = "setupAttackAnimation", at = @At("HEAD"))

    protected void setupAttackAnimation(final HumanoidRenderState state, CallbackInfo ci) {
        float attackTime = state.swingAnimation;
        LivingEntity.SwingDescription currentSwing = state.currentSwing;
        if (!(attackTime <= 0.0F))
            if (currentSwing != null) {
                ModelPart attackArm = this.getArm(state.mainArm);
                if ((Object) currentSwing.animation() instanceof SwingAnimationAccessor ac)
                    if (ac.caput_mundi$isGladius()) {
                        /*1*/
                        float v = 0.8f;
                        attackArm.xRot = attackArm.xRot + (
                                v - attackTime * 2 * v);
                        //  attackArm.yRot += 2.0F;
                    } else if (ac.caput_mundi$isPugio()) {
                        //       float maxAlt = -1f;
                        int i = 7;
                        attackArm.xRot = (float) (attackArm.xRot - (attackTime > 0.5 ? (0.5 - (attackTime - 0.5)) * i : attackTime * i));

                    }
            }
    }
}
