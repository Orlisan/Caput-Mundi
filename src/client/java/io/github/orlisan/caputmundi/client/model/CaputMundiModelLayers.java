package io.github.orlisan.caputmundi.client.model;

import com.google.common.collect.Sets;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.resources.Identifier;

import java.util.Set;

public class CaputMundiModelLayers {
    private static ModelLayerLocation register(final String model) {
        return register(model, "main");
    }
    private static final Set<ModelLayerLocation> ALL_MODELS = Sets.newHashSet();
    private static ModelLayerLocation register(final String model, final String layer) {
        ModelLayerLocation result = createLocation(model, layer);
        if (!ALL_MODELS.add(result)) {
            throw new IllegalStateException("Duplicate registration for " + result);
        } else {
            return result;
        }

    }


    private static ModelLayerLocation createLocation(final String model, final String layer) {
        return new ModelLayerLocation(Identifier.withDefaultNamespace(model), layer);
    }

    public static final ModelLayerLocation PILUM = register("pilum");
}
