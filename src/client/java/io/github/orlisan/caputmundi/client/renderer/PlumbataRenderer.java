package io.github.orlisan.caputmundi.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.orlisan.caputmundi.CaputMundi;
import io.github.orlisan.caputmundi.client.model.CaputMundiModelLayers;
import io.github.orlisan.caputmundi.client.model.PlumbataModel;
import io.github.orlisan.caputmundi.client.renderer.layer.PilumRenderState;
import io.github.orlisan.caputmundi.entities.PlumbataLanciata;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import org.joml.Matrix4f;
import org.jspecify.annotations.NonNull;

public  class PlumbataRenderer extends EntityRenderer<PlumbataLanciata,PilumRenderState> {
    private final PlumbataModel model;

    public PlumbataRenderer(final EntityRendererProvider.Context context) {
        super(context);
        this.model = new PlumbataModel(context.bakeLayer(CaputMundiModelLayers.PLUMBATA));
    }

    @Override
    public @NonNull PilumRenderState createRenderState() {
        return new PilumRenderState();
    }

    protected Identifier getTextureLocation() {
        return Identifier.fromNamespaceAndPath(CaputMundi.MOD_ID, "textures/entity/projectiles/plumbata.png");
    }
    @Override
    public void submit(final PilumRenderState state, final PoseStack poseStack, final SubmitNodeCollector submitNodeCollector, final @NonNull CameraRenderState camera) {
        poseStack.pushPose();
        poseStack.mulPose(new Matrix4f().rotateY((float) Math.toRadians(state.yRot - 90.0F)));
        poseStack.mulPose(new Matrix4f().rotateZ((float) Math.toRadians(state.xRot)));
        poseStack.mulPose(new Matrix4f().rotateZ((float) Math.toRadians(-90.0F)));
        poseStack.scale(0.125f, 0.125f, 0.125f);
        submitNodeCollector.submitModel(this.model, state, poseStack, this.getTextureLocation(), state.lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor);
        poseStack.popPose();
        super.submit(state, poseStack, submitNodeCollector, camera);
    }

    @Override
    public void extractRenderState(@NonNull PlumbataLanciata entity, @NonNull PilumRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.xRot = entity.getXRot(partialTicks)+180;
        state.yRot = entity.getYRot(partialTicks);
    }
}
