package io.github.orlisan.caputmundi.client.model;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.entity.state.EntityRenderState;

public class PlumbataModel extends EntityModel<EntityRenderState> {

    public PlumbataModel(ModelPart root) {
        super(root);
        ModelPart p = root.getChild("plumbata");
        p.xScale = 0.125F;
        p.yScale = 0.125F;
        p.zScale = 0.125F;
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild("plumbata",
                CubeListBuilder.create()
                        .texOffs(0, 0).addBox(-8.0F, -56.0F, -8.0F, 16.0F, 16.0F, 16.0F)
                        .texOffs(64, 0).addBox(-4.0F, -64.0F, -4.0F, 8.0F, 8.0F, 8.0F)
                        .texOffs(96, 0).addBox(-2.0F, -82.0F, -2.2F, 4.0F, 2.0F, 4.0F)
                        .texOffs(112, 0).addBox(-1.0F, -84.0F, -1.2F, 2.0F, 2.0F, 2.0F)
                        .texOffs(120, 0).addBox(-0.4F, -85.0F, -0.8F, 1.0F, 1.0F, 1.0F)
                        .texOffs(64, 16).addBox(-2.0F, -40.0F, -2.0F, 4.0F, 32.0F, 4.0F)
                        .texOffs(80, 16).addBox(-1.0F, -80.0F, -1.2F, 2.0F, 16.0F, 2.0F)
                        .texOffs(88, 16).addBox(-6.0F, -18.0F, -1.0F, 12.0F, 8.0F, 2.0F)
                        .texOffs(88, 26).addBox(-1.0F, -18.0F, -6.0F, 2.0F, 8.0F, 12.0F),
                PartPose.offset(0.0F, 24.0F, 0.0F));

        return LayerDefinition.create(mesh, 128, 64);
    }
}