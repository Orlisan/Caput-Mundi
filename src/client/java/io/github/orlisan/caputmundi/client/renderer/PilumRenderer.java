package io.github.orlisan.caputmundi.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import io.github.orlisan.caputmundi.CaputMundi;
import io.github.orlisan.caputmundi.client.model.CaputMundiModelLayers;
import io.github.orlisan.caputmundi.client.model.PilumModel;
import io.github.orlisan.caputmundi.client.renderer.layer.PilumRenderState;
import io.github.orlisan.caputmundi.entities.PilumLanciato;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;

public  class PilumRenderer extends EntityRenderer<PilumLanciato,PilumRenderState> {
    private final PilumModel model;

    public PilumRenderer(final EntityRendererProvider.Context context) {
        super(context);
        this.model = new PilumModel(context.bakeLayer(CaputMundiModelLayers.PILUM));
    }

    @Override
    public @NonNull PilumRenderState createRenderState() {
        return new PilumRenderState();
    }

    protected Identifier getTextureLocation(PilumRenderState state) {
        return Identifier.fromNamespaceAndPath(CaputMundi.MOD_ID, "textures/item/pilum_item.png");
    }
    @Override
    public void submit(final PilumRenderState state, final PoseStack poseStack, final SubmitNodeCollector submitNodeCollector, final @NonNull CameraRenderState camera) {
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(state.yRot - 90.0F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(state.xRot));
        submitNodeCollector.submitModel(this.model, state, poseStack, this.getTextureLocation(state), state.lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor, null);
        poseStack.popPose();
        super.submit(state, poseStack, submitNodeCollector, camera);
    }

    @Override
    public void extractRenderState(@NonNull PilumLanciato entity, @NonNull PilumRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.xRot = entity.getXRot(partialTicks);
        state.yRot = entity.getYRot(partialTicks);
    }
}
