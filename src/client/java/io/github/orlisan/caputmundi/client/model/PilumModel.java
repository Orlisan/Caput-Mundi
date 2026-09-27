package io.github.orlisan.caputmundi.client.model;

import io.github.orlisan.caputmundi.client.renderer.layer.PilumRenderState;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.rendertype.RenderTypes;

public class PilumModel extends EntityModel<PilumRenderState> {
    public PilumModel(ModelPart root) {
        super(root, RenderTypes::entityCutoutCull);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        partdefinition.addOrReplaceChild("cube_0",
                CubeListBuilder.create().texOffs(0, 2)
                        .addBox(-1.0F, 0.0F, -1.0F, 1.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(0.5F, 7.3F, -0.7F,
                        (float) Math.toRadians(45.0), 0.0F, 0.0F));

        partdefinition.addOrReplaceChild("cube_1",
                CubeListBuilder.create().texOffs(0, 6)
                        .addBox(-1.0F, 0.0F, -1.0F, 1.0F, 1.0F, 2.0F),
                PartPose.offsetAndRotation(0.5F, 8.0F, 0.0F,
                        (float) Math.toRadians(45.0), 0.0F, 0.0F));

        partdefinition.addOrReplaceChild("cube_2",
                CubeListBuilder.create().texOffs(4, 2)
                        .addBox(-0.5F, -0.99497F, -0.75F, 1.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(-0.175F, 8.18033F, -0.02322F,
                        (float) Math.toRadians(-135.0), (float) Math.toRadians(90.0), (float) Math.toRadians(180.0)));

        partdefinition.addOrReplaceChild("cube_3",
                CubeListBuilder.create().texOffs(0, 10)
                        .addBox(-0.5F, -0.00503F, -0.75F, 1.0F, 1.0F, 2.0F),
                PartPose.offsetAndRotation(-0.175F, 8.18033F, -0.02322F,
                        (float) Math.toRadians(-135.0), (float) Math.toRadians(90.0), (float) Math.toRadians(180.0)));

        partdefinition.addOrReplaceChild("cube_4",
                CubeListBuilder.create().texOffs(27, 14)
                        .addBox(0.0F, -13.0F, 0.0F, 0.5F, 0.5F, 0.5F),
                PartPose.offset(-0.25F, 5.0F, -0.25F));

        partdefinition.addOrReplaceChild("cube_5",
                CubeListBuilder.create().texOffs(27, 16)
                        .addBox(-0.1F, -13.0F, 0.0F, 0.75F, 0.75F, 0.75F),
                PartPose.offset(-0.275F, 5.5F, -0.375F));

        partdefinition.addOrReplaceChild("cube_6",
                CubeListBuilder.create().texOffs(2, 14)
                        .addBox(-0.5F, -12.5F, -0.5F, 1.0F, 15.0F, 1.0F),
                PartPose.offset(0.0F, 5.75F, 0.0F));

        partdefinition.addOrReplaceChild("cube_7",
                CubeListBuilder.create().texOffs(27, 14)
                        .addBox(-0.375F, -0.375F, -0.375F, 0.25F, 10.0F, 0.25F),
                PartPose.offset(0.25F, 9.45F, 0.25F));

        partdefinition.addOrReplaceChild("cube_8",
                CubeListBuilder.create().texOffs(27, 17)
                        .addBox(-0.5056F, -0.25F, -0.75F, 0.55F, 0.25F, 0.55F),
                PartPose.offsetAndRotation(0.25F, 19.7F, 0.125F,
                        (float) Math.toRadians(-45.0), 0.0F, 0.0F));

        partdefinition.addOrReplaceChild("cube_9",
                CubeListBuilder.create().texOffs(27, 19)
                        .addBox(-0.0001F, 0.0F, 0.0F, 0.55F, 0.25F, 0.55F),
                PartPose.offset(-0.25F, 19.0F, -0.25F));

        partdefinition.addOrReplaceChild("cube_10",
                CubeListBuilder.create().texOffs(27, 21)
                        .addBox(-0.5F, -0.25F, 0.45F, 0.55F, 0.25F, 0.55F),
                PartPose.offsetAndRotation(0.25F, 19.9F, -0.3F,
                        (float) Math.toRadians(45.0), 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 32, 32);
    }
}
