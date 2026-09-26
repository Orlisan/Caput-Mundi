package io.github.orlisan.caputmundi.client.renderer.layer;

import io.github.orlisan.caputmundi.CaputMundi;
import io.github.orlisan.caputmundi.client.model.CaputMundiModelLayers;
import io.github.orlisan.caputmundi.client.model.PilumModel;
import io.github.orlisan.caputmundi.PilaCountAccessor;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.StuckInBodyLayer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;

public class PilumLayer<M extends PlayerModel> extends StuckInBodyLayer<M, PilumRenderState> {
    public PilumLayer(final LivingEntityRenderer<?, AvatarRenderState, M> renderer, final EntityRendererProvider.Context context) {
        super(renderer, new PilumModel(context.bakeLayer(CaputMundiModelLayers.PILUM)), new PilumRenderState(), Identifier.fromNamespaceAndPath(CaputMundi.MOD_ID, "textures/item/pilum_item.png"), PlacementStyle.IN_CUBE);
    }

    @Override
    protected int numStuck(@NonNull AvatarRenderState state) {
        return ((PilaCountAccessor) state).getPila$Count();
    }
}

