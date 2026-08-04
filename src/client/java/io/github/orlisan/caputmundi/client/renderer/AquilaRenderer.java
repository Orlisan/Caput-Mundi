package io.github.orlisan.caputmundi.client.renderer;

import com.geckolib.model.GeoModel;
import com.geckolib.renderer.GeoEntityRenderer;
import io.github.orlisan.caputmundi.client.model.AquilaModel;
import io.github.orlisan.caputmundi.entities.AquilaEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import org.jetbrains.annotations.NotNull;
public class AquilaRenderer extends GeoEntityRenderer<@NotNull AquilaEntity, @NotNull AquilaRenderState> {
    public AquilaRenderer(EntityRendererProvider.Context context) {
        super(context, new AquilaModel());
    }
}
class AquilaRenderState extends EntityRenderState {}
