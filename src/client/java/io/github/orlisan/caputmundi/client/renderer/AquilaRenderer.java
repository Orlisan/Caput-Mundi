package io.github.orlisan.caputmundi.client.renderer;

import com.geckolib.renderer.GeoEntityRenderer;
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
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class AquilaRenderer extends GeoEntityRenderer<@NotNull AquilaEntity, @NotNull AquilaRenderState> {
    public AquilaRenderer(EntityRendererProvider.Context context) {
        super(context, new AquilaModel());
        this.withRenderLayer(new CollettoRenderLayer(this))
                .withRenderLayer(new ArmorRenderLayer(this));
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
        renderPassInfo.addBoneUpdater((_, snapshots) ->
                snapshots.get("collare").ifPresent(snapshot -> snapshot.skipRender(
                        Boolean.FALSE.equals(state.getGeckolibData(CaputMundiConstants.HAS_COLLAR)))));
        //     this.getDefaultBakedModel(state).getBone("collare").ifPresent(bone -> BoneSnapshot.create(bone).skipRender(Boolean.FALSE.equals(state.getGeckolibData(AquilaDataTickets.HAS_ARMOR))).apply());
    }
}

class ArmorRenderLayer extends GeoRenderLayer<@NotNull AquilaEntity, Void, @NotNull AquilaRenderState> {
    public ArmorRenderLayer(GeoRenderer<@NotNull AquilaEntity, Void, @NotNull AquilaRenderState> renderer) {
        super(renderer);
    }

    @Override
    public void preRender(@NonNull RenderPassInfo<@NotNull AquilaRenderState> renderPassInfo, @NonNull SubmitNodeCollector renderTasks) {
        super.preRender(renderPassInfo, renderTasks);
        AquilaRenderState state = renderPassInfo.renderState();
        renderPassInfo.addBoneUpdater((_, snapshots) -> {
                    snapshots.get("armor_collo").ifPresent(snapshot -> snapshot.skipRender(
                            Boolean.FALSE.equals(state.getGeckolibData(CaputMundiConstants.HAS_ARMOR))));
                    snapshots.get("armor_busto").ifPresent(snapshot -> snapshot.skipRender(
                            Boolean.FALSE.equals(state.getGeckolibData(CaputMundiConstants.HAS_ARMOR))));
                    snapshots.get("armor_spalla_destra").ifPresent(snapshot -> snapshot.skipRender(
                            Boolean.FALSE.equals(state.getGeckolibData(CaputMundiConstants.HAS_ARMOR))));
                    snapshots.get("armor_spalla_sinistra").ifPresent(snapshot -> snapshot.skipRender(
                            Boolean.FALSE.equals(state.getGeckolibData(CaputMundiConstants.HAS_ARMOR))));
                    snapshots.get("armor_osso_omerodestro").ifPresent(snapshot -> snapshot.skipRender(
                            Boolean.FALSE.equals(state.getGeckolibData(CaputMundiConstants.HAS_ARMOR))));
                    snapshots.get("armor_osso_omerosinistro").ifPresent(snapshot -> snapshot.skipRender(
                            Boolean.FALSE.equals(state.getGeckolibData(CaputMundiConstants.HAS_ARMOR))));
                    snapshots.get("armor_radio_ulna_sinistra").ifPresent(snapshot -> snapshot.skipRender(
                            Boolean.FALSE.equals(state.getGeckolibData(CaputMundiConstants.HAS_ARMOR))));
                    snapshots.get("armor_radio_ulna_destra").ifPresent(snapshot -> snapshot.skipRender(
                            Boolean.FALSE.equals(state.getGeckolibData(CaputMundiConstants.HAS_ARMOR))));
                    snapshots.get("osso_omero9").ifPresent(snapshot -> snapshot.skipRender(
                            Boolean.FALSE.equals(state.getGeckolibData(CaputMundiConstants.HAS_ARMOR))));
                    snapshots.get("osso_omero10").ifPresent(snapshot -> snapshot.skipRender(
                            Boolean.FALSE.equals(state.getGeckolibData(CaputMundiConstants.HAS_ARMOR))));
                    snapshots.get("osso_omero11").ifPresent(snapshot -> snapshot.skipRender(
                            Boolean.FALSE.equals(state.getGeckolibData(CaputMundiConstants.HAS_ARMOR))));
                    snapshots.get("osso_omero12").ifPresent(snapshot -> snapshot.skipRender(
                            Boolean.FALSE.equals(state.getGeckolibData(CaputMundiConstants.HAS_ARMOR))));
                    snapshots.get("osso_omero13").ifPresent(snapshot -> snapshot.skipRender(
                            Boolean.FALSE.equals(state.getGeckolibData(CaputMundiConstants.HAS_ARMOR))));
                    snapshots.get("osso_omero14").ifPresent(snapshot -> snapshot.skipRender(
                            Boolean.FALSE.equals(state.getGeckolibData(CaputMundiConstants.HAS_ARMOR))));
                    snapshots.get("osso23").ifPresent(snapshot -> snapshot.skipRender(
                            Boolean.FALSE.equals(state.getGeckolibData(CaputMundiConstants.HAS_ARMOR))));
                    snapshots.get("osso24").ifPresent(snapshot -> snapshot.skipRender(
                            Boolean.FALSE.equals(state.getGeckolibData(CaputMundiConstants.HAS_ARMOR))));
                    snapshots.get("osso25").ifPresent(snapshot -> snapshot.skipRender(
                            Boolean.FALSE.equals(state.getGeckolibData(CaputMundiConstants.HAS_ARMOR))));
                    snapshots.get("osso26").ifPresent(snapshot -> snapshot.skipRender(
                            Boolean.FALSE.equals(state.getGeckolibData(CaputMundiConstants.HAS_ARMOR))));
                    snapshots.get("osso27").ifPresent(snapshot -> snapshot.skipRender(
                            Boolean.FALSE.equals(state.getGeckolibData(CaputMundiConstants.HAS_ARMOR))));
                    snapshots.get("osso28").ifPresent(snapshot -> snapshot.skipRender(
                            Boolean.FALSE.equals(state.getGeckolibData(CaputMundiConstants.HAS_ARMOR))));
                    snapshots.get("osso29").ifPresent(snapshot -> snapshot.skipRender(
                            Boolean.FALSE.equals(state.getGeckolibData(CaputMundiConstants.HAS_ARMOR))));
                    snapshots.get("osso30").ifPresent(snapshot -> snapshot.skipRender(
                            Boolean.FALSE.equals(state.getGeckolibData(CaputMundiConstants.HAS_ARMOR))));
                    snapshots.get("osso31").ifPresent(snapshot -> snapshot.skipRender(
                            Boolean.FALSE.equals(state.getGeckolibData(CaputMundiConstants.HAS_ARMOR))));
                    snapshots.get("osso32").ifPresent(snapshot -> snapshot.skipRender(
                            Boolean.FALSE.equals(state.getGeckolibData(CaputMundiConstants.HAS_ARMOR))));
                    snapshots.get("osso33").ifPresent(snapshot -> snapshot.skipRender(
                            Boolean.FALSE.equals(state.getGeckolibData(CaputMundiConstants.HAS_ARMOR))));
                    snapshots.get("osso34").ifPresent(snapshot -> snapshot.skipRender(
                            Boolean.FALSE.equals(state.getGeckolibData(CaputMundiConstants.HAS_ARMOR))));
                }
        );
    }
}