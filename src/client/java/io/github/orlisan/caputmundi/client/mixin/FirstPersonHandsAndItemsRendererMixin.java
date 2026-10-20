package io.github.orlisan.caputmundi.client.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import io.github.orlisan.caputmundi.items.CaputMundiItems;
import net.minecraft.client.renderer.FirstPersonHandsAndItemsRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.state.level.FirstPersonHandsAndItemsRenderState;
import net.minecraft.client.renderer.state.level.PlayerRenderState;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FirstPersonHandsAndItemsRenderer.class)
public class FirstPersonHandsAndItemsRendererMixin {
    @Inject(at = @At("HEAD"), method = "submitArmWithItem")
    public void addaAnimazioniPrimaPersonaRomane(PlayerRenderState playerState, FirstPersonHandsAndItemsRenderState state, float partialTicks, float xRot, InteractionHand hand, float attackTime, ItemStack itemStack, float inverseArmHeight, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, CallbackInfo ci) {
        if (!state.isScoping) {
            AvatarRenderState avatarRenderState = playerState.avatarRenderState;
            poseStack.pushPose();
            if (avatarRenderState != null && !itemStack.has(DataComponents.MAP_ID)
                && (!avatarRenderState.isUsingItem || !(state.useItemRemainingTicks > 0) || !(avatarRenderState.useItemHand == hand)) && !avatarRenderState.isAutoSpinAttack && attackTime > 0) {
                if (itemStack.is(CaputMundiItems.GLADIUS)) {
                    //    float ySwingPosition = 0.2F * Mth.sin(Mth.sqrt(attackTime) * ((float)Math.PI * 2F));
                    //     poseStack.translate(2*attackTime, ySwingPosition, 0);
                    poseStack.rotateDegrees(Axis.XP, -30 - attackTime * 60);
                    //  poseStack.rotate(Axis.XN, -30);
                  //     applyItemArmAttackTransform(poseStack, arm, attackTime);
                }else if(itemStack.is(CaputMundiItems.PUGIO)) {
                    int i = 277;
                    poseStack.rotateDegrees(Axis.XP,
                            attackTime > 0.5 ? (float) ((0.5 - (attackTime - 0.5)) * i) : attackTime * i);


                }
            }
        }
    }

    @Shadow
    private void applyItemArmAttackTransform(PoseStack poseStack, HumanoidArm arm, float attackValue) {
    }

    @Inject(at = @At("RETURN"), method = "submitArmWithItem")
    public void popPosa(PlayerRenderState playerState, FirstPersonHandsAndItemsRenderState state, float partialTicks, float xRot, InteractionHand hand, float attack, ItemStack itemStack, float inverseArmHeight, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, CallbackInfo ci) {
        poseStack.popPose();
    }
}
