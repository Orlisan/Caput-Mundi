package io.github.orlisan.caputmundi.client.mixin;

import io.github.orlisan.caputmundi.accessors.ArmPoseAccessor;
import io.github.orlisan.caputmundi.client.renderer.layer.PilumLayer;
import io.github.orlisan.caputmundi.accessors.PilaCountAccessor;
import io.github.orlisan.caputmundi.items.CaputMundiItems;
import net.minecraft.client.entity.ClientAvatarEntity;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Avatar;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AvatarRenderer.class)
public abstract class AvatarRenderMixin<AvatarlikeEntity extends Avatar & ClientAvatarEntity> extends LivingEntityRenderer<AvatarlikeEntity, AvatarRenderState, PlayerModel> {

    @Shadow
    public abstract boolean isEntityUpsideDown(AvatarlikeEntity mob);

    public AvatarRenderMixin(EntityRendererProvider.Context context, PlayerModel model, float shadow) {
        super(context, model, shadow);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    public void addPilumLayer(final EntityRendererProvider.Context context, final boolean slimSteve, CallbackInfo ci) {
        this.addLayer(new PilumLayer<>(this, context));
    }

    @Inject(method = "extractRenderState*", at = @At("TAIL"))
    public void mettiPilaEUse(@Coerce Entity entity, @Coerce EntityRenderState state, float v, CallbackInfo ci) {
        ((PilaCountAccessor) state).caput_mundi$setPilaCount(((PilaCountAccessor) entity).caput_mundi$getPilaCount());
        if (entity instanceof Avatar player)
            for (InteractionHand hand : InteractionHand.values()) {
                ItemStack s = player.getItemInHand(hand);
                if (s.is(CaputMundiItems.SCUTUM) && shouldMakeThat(s, player, hand)) {
                    HumanoidArm arm = hand == InteractionHand.MAIN_HAND ? player.getMainArm() : player.getMainArm().getOpposite();
                    if (arm == HumanoidArm.RIGHT) ((ArmPoseAccessor) state).caput_mundi$setIsScutumRight(true);
                    else ((ArmPoseAccessor) state).caput_mundi$setIsScutumLeft(true);
                }
            }
    }

    @Unique
    boolean shouldMakeThat(ItemStack stack, Avatar player, InteractionHand hand) {
        Integer i = stack.get(CaputMundiItems.PLUMBATA_COUNT);
        return player.isUsingItem() && i != null && i > 0 && player.getUsedItemHand() == hand &&
               player.getItemInHand(hand == InteractionHand.MAIN_HAND ? InteractionHand.OFF_HAND :
                       InteractionHand.MAIN_HAND).isEmpty();
    }
}
