package io.github.orlisan.caputmundi.client.renderer;

import com.geckolib.animation.state.BoneSnapshot;
import com.geckolib.model.GeoModel;
import com.geckolib.renderer.GeoEntityRenderer;
import com.geckolib.renderer.base.GeoRenderState;
import com.geckolib.renderer.base.GeoRenderer;
import com.geckolib.renderer.base.RenderPassInfo;
import com.geckolib.renderer.layer.GeoRenderLayer;
import io.github.orlisan.caputmundi.CaputMundiConstants;
import io.github.orlisan.caputmundi.client.model.AquilaModel;
import io.github.orlisan.caputmundi.entities.AquilaEntity;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;

public class AquilaRenderer extends GeoEntityRenderer<@NotNull AquilaEntity, @NotNull AquilaRenderState> {
    public AquilaRenderer(EntityRendererProvider.Context context) {
        super(context, new AquilaModel());
        this.withRenderLayer(new CollettoRenderLayer(this));
    }

    @Override
    public @NotNull AquilaRenderState createRenderState(@NotNull AquilaEntity animatable, @Nullable Void relatedObject) {
        return new AquilaRenderState();
    }
}

class AquilaRenderState extends EntityRenderState {
}

class CollettoRenderLayer extends GeoRenderLayer<@NotNull AquilaEntity, Void, @NotNull AquilaRenderState> {
    public CollettoRenderLayer(GeoRenderer<@NotNull AquilaEntity, Void, @NotNull AquilaRenderState> renderer) {
        super(renderer);
    }

    @Override
    public void preRender(RenderPassInfo<@NotNull AquilaRenderState> renderPassInfo, SubmitNodeCollector renderTasks) {
        super.preRender(renderPassInfo, renderTasks);
        AquilaRenderState state = renderPassInfo.renderState();
        renderPassInfo.addBoneUpdater((info, snapshots) ->
                snapshots.get("collare").ifPresent(snapshot -> snapshot.skipRender(
                        Boolean.FALSE.equals(state.getGeckolibData(CaputMundiConstants.HAS_COLLAR)))));
        //     this.getDefaultBakedModel(state).getBone("collare").ifPresent(bone -> BoneSnapshot.create(bone).skipRender(Boolean.FALSE.equals(state.getGeckolibData(AquilaDataTickets.HAS_COLLAR))).apply());
    }
}