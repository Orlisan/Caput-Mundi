package io.github.orlisan.caputmundi.client.renderer;

import com.geckolib.renderer.GeoArmorRenderer;
import com.geckolib.renderer.GeoEntityRenderer;
import io.github.orlisan.caputmundi.client.model.AquilaModel;
import io.github.orlisan.caputmundi.client.model.LegionariusModel;
import io.github.orlisan.caputmundi.entities.AquilaEntity;
import io.github.orlisan.caputmundi.entities.Legionarius;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;

public class LegionariusRenderer extends GeoEntityRenderer<@NotNull Legionarius, LegionariusRenderer.LegionariusRenderState> {
        public LegionariusRenderer(EntityRendererProvider.Context context) {
            super(context, new LegionariusModel());
        }
        @Override
        public @NotNull LegionariusRenderState createRenderState(@NotNull Legionarius animatable, @Nullable Void relatedObject) {
            return new LegionariusRenderState();
        }
        public static class LegionariusRenderState extends EntityRenderState {}
}
