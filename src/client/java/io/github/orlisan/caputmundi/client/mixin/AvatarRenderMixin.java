package io.github.orlisan.caputmundi.client.mixin;

import io.github.orlisan.caputmundi.client.renderer.layer.PilumLayer;
import io.github.orlisan.caputmundi.PilaCountAccessor;
import net.minecraft.client.entity.ClientAvatarEntity;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.world.entity.Avatar;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AvatarRenderer.class)
public abstract class AvatarRenderMixin<AvatarlikeEntity extends Avatar & ClientAvatarEntity> extends LivingEntityRenderer<AvatarlikeEntity, AvatarRenderState, PlayerModel> {

    public AvatarRenderMixin(EntityRendererProvider.Context context, PlayerModel model, float shadow) {
        super(context, model, shadow);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    public void addPilumLayer(final EntityRendererProvider.Context context, final boolean slimSteve, CallbackInfo ci) {
        this.addLayer(new PilumLayer<>(this, context));
    }

    @Inject(method = "extractRenderState*", at = @At("TAIL"))
    public void mettiPila(final AvatarlikeEntity entity, final AvatarRenderState state, final float partialTicks, CallbackInfo ci) {
        ((PilaCountAccessor) state).setPila$Count(((PilaCountAccessor) entity).getPila$Count());
    }
}
