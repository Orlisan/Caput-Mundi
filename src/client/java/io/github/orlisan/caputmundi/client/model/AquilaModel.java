package io.github.orlisan.caputmundi.client.model;

import com.geckolib.model.GeoModel;
import com.geckolib.renderer.base.GeoRenderState;
import io.github.orlisan.caputmundi.CaputMundi;
import io.github.orlisan.caputmundi.CaputMundiConstants;
import io.github.orlisan.caputmundi.entities.AquilaEntity;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;

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
    public void addAdditionalStateData(@NotNull AquilaEntity animatable, @Nullable Object relatedObject, GeoRenderState renderState) {
        renderState.addGeckolibData(CaputMundiConstants.HAS_COLLAR, animatable.hasCollar());
        renderState.addGeckolibData(CaputMundiConstants.HAS_ARMOR, animatable.hasArmor());
        super.addAdditionalStateData(animatable, relatedObject, renderState);
    }

    @Override
    public @NotNull Identifier getAnimationResource(AquilaEntity aquilaEntity) {
        return i("aquila");
    }
}
