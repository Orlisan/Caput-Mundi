package io.github.orlisan.caputmundi.client.model;

import com.geckolib.model.GeoModel;
import com.geckolib.renderer.base.GeoRenderState;
import io.github.orlisan.caputmundi.CaputMundi;
import io.github.orlisan.caputmundi.entities.AquilaEntity;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

public class AquilaModel extends GeoModel<@NotNull AquilaEntity> {
    @Override
    public @NotNull Identifier getModelResource(@NotNull GeoRenderState geoRenderState) {
        return i("aquila");
    }

    Identifier i(String path) {
        return Identifier.fromNamespaceAndPath(CaputMundi.MOD_ID, path);
    }

    @Override
    public @NotNull Identifier getTextureResource(@NotNull GeoRenderState geoRenderState) {
        return i("textures/entity/aquila.png");
    }

    @Override
    public @NotNull Identifier getAnimationResource(AquilaEntity aquilaEntity) {
        return i("aquila");
    }
}
