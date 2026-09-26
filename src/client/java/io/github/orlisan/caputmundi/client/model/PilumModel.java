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
                CubeListBuilder.create().texOffs(4, 3)
                        .addBox(-1.0F, 0.0F, -1.0F, 1.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(0.5F, 7.3F, -0.7F,
                        (float) Math.toRadians(45.0), 0.0F, 0.0F));

        partdefinition.addOrReplaceChild("cube_1",
                CubeListBuilder.create().texOffs(4, 0)
                        .addBox(-1.0F, 0.0F, -1.0F, 1.0F, 1.0F, 2.0F),
                PartPose.offsetAndRotation(0.5F, 8.0F, 0.0F,
                        (float) Math.toRadians(45.0), 0.0F, 0.0F));

        partdefinition.addOrReplaceChild("cube_2",
                CubeListBuilder.create().texOffs(4, 3)
                        .addBox(-0.5F, -0.99497F, -0.75F, 1.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(-0.175F, 8.18033F, -0.02322F,
                        (float) Math.toRadians(-135.0), (float) Math.toRadians(90.0), (float) Math.toRadians(180.0)));

        partdefinition.addOrReplaceChild("cube_3",
                CubeListBuilder.create().texOffs(4, 0)
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
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-0.5F, -12.5F, -0.5F, 1.0F, 15.0F, 1.0F),
                PartPose.offset(0.0F, 5.75F, 0.0F));

        partdefinition.addOrReplaceChild("cube_7",
                CubeListBuilder.create().texOffs(27, 20)
                        .addBox(-0.375F, -0.375F, -0.375F, 0.75F, 10.0F, 0.75F),
                PartPose.offset(0.0F, 9.45F, 0.0F));

        partdefinition.addOrReplaceChild("cube_8",
                CubeListBuilder.create().texOffs(27, 22)
                        .addBox(-0.0001F, 0.0F, 0.0F, 1.5F, 0.5F, 1.5F),
                PartPose.offset(-0.75F, 19.0F, -0.75F));

        partdefinition.addOrReplaceChild("cube_9",
                CubeListBuilder.create().texOffs(27, 18)
                        .addBox(-0.0001F, 0.0F, 0.0F, 1.48F, 0.5F, 0.5F),
                PartPose.offset(-0.725F, 19.25F, -0.25F));

        partdefinition.addOrReplaceChild("cube_10",
                CubeListBuilder.create().texOffs(27, 25)
                        .addBox(-0.75F, -0.25F, -0.75F, 1.5F, 0.5F, 1.5F),
                PartPose.offsetAndRotation(0.0F, 19.7F, 0.375F,
                        (float) Math.toRadians(45.0), 0.0F, 0.0F));

        partdefinition.addOrReplaceChild("cube_11",
                CubeListBuilder.create().texOffs(27, 28)
                        .addBox(-0.7556F, -0.25F, -0.75F, 1.5F, 0.5F, 1.5F),
                PartPose.offsetAndRotation(0.0F, 19.7F, -0.35F,
                        (float) Math.toRadians(-45.0), 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 32, 32);
    }
}
