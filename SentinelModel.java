package com.voidwarden.client;

import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

/** Generated Sentinel armor geometry: crown, layered plates, tabard, greaves and articulated cape. */
final class SentinelModel {
    static final int TEX_W = 128;
    static final int TEX_H = 39;
    static final int CAPE_SEGS = 5;

    static LayerDefinition create() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild("head", CubeListBuilder.create()
                .texOffs(0, 0).addBox(-5f, -9.5f, -5f, 10f, 1f, 10f, new CubeDeformation(0.35f))
                .texOffs(104, 11).addBox(-1.5f, -17f, -5.5f, 3f, 7f, 1f)
                .texOffs(82, 34).addBox(-0.5f, -13.5f, -6.1f, 1f, 2f, 1f)
                .texOffs(114, 21).addBox(-4f, -14f, -5.2f, 2f, 4f, 1f)
                .texOffs(120, 21).addBox(2f, -14f, -5.2f, 2f, 4f, 1f)
                .texOffs(58, 21).addBox(-5.2f, -15f, -5.2f, 1f, 5f, 1f)
                .texOffs(62, 21).addBox(4.2f, -15f, -5.2f, 1f, 5f, 1f)
                .texOffs(66, 21).addBox(-5.2f, -15f, 4.2f, 1f, 5f, 1f)
                .texOffs(70, 21).addBox(4.2f, -15f, 4.2f, 1f, 5f, 1f)
                .texOffs(0, 29).addBox(-3.5f, -14f, 4.2f, 2f, 4f, 1f)
                .texOffs(6, 29).addBox(1.5f, -14f, 4.2f, 2f, 4f, 1f)
                .texOffs(12, 29).addBox(-5.6f, -14f, -0.5f, 1f, 4f, 1f)
                .texOffs(16, 29).addBox(4.6f, -14f, -0.5f, 1f, 4f, 1f),
                PartPose.offset(0f, 0f, 0f));

        root.addOrReplaceChild("chest", CubeListBuilder.create()
                .texOffs(40, 0).addBox(-3f, 0.5f, -3.5f, 6f, 10f, 1f)
                .texOffs(24, 11).addBox(-3f, 0f, 3.1f, 6f, 8f, 1f)
                .texOffs(86, 34).addBox(-1f, 3f, 4f, 2f, 2f, 1f),
                PartPose.offset(0f, 0f, 0f));

        root.addOrReplaceChild("arm_r", CubeListBuilder.create()
                .texOffs(38, 11).addBox(-4.5f, -4f, -3.5f, 6f, 2f, 7f, new CubeDeformation(0.2f))
                .texOffs(54, 0).addBox(-5f, -2f, -3.5f, 5f, 3f, 7f, new CubeDeformation(0.3f))
                .texOffs(0, 21).addBox(-5f, 1f, -3f, 5f, 2f, 6f, new CubeDeformation(0.3f))
                .texOffs(40, 34).addBox(-5.9f, -1f, -1f, 1f, 2f, 2f)
                .texOffs(102, 0).addBox(-4f, 5f, -3f, 6f, 4f, 6f, new CubeDeformation(0.35f))
                .texOffs(98, 34).addBox(-4.9f, 6.5f, -0.5f, 1f, 1f, 1f),
                PartPose.offset(0f, 0f, 0f));

        root.addOrReplaceChild("arm_l", CubeListBuilder.create()
                .texOffs(64, 11).addBox(-1.5f, -4f, -3.5f, 6f, 2f, 7f, new CubeDeformation(0.2f))
                .texOffs(78, 0).addBox(0f, -2f, -3.5f, 5f, 3f, 7f, new CubeDeformation(0.3f))
                .texOffs(22, 21).addBox(0f, 1f, -3f, 5f, 2f, 6f, new CubeDeformation(0.3f))
                .texOffs(46, 34).addBox(4.9f, -1f, -1f, 1f, 2f, 2f)
                .texOffs(0, 11).addBox(-2f, 5f, -3f, 6f, 4f, 6f, new CubeDeformation(0.35f))
                .texOffs(102, 34).addBox(3.9f, 6.5f, -0.5f, 1f, 1f, 1f),
                PartPose.offset(0f, 0f, 0f));

        root.addOrReplaceChild("belt", CubeListBuilder.create()
                .texOffs(74, 21).addBox(-4f, 9f, -2f, 8f, 2f, 4f, new CubeDeformation(1.3f))
                .texOffs(52, 34).addBox(-2f, 8.5f, -4f, 4f, 3f, 1f)
                .texOffs(92, 34).addBox(-1f, 9f, -4.7f, 2f, 2f, 1f)
                .texOffs(98, 21).addBox(-5.9f, 8f, -1.5f, 1f, 3f, 3f, new CubeDeformation(0.2f))
                .texOffs(106, 21).addBox(4.9f, 8f, -1.5f, 1f, 3f, 3f, new CubeDeformation(0.2f)),
                PartPose.offset(0f, 0f, 0f));

        root.addOrReplaceChild("cloth_f", CubeListBuilder.create()
                .texOffs(90, 11).addBox(-3f, 0f, -3.5f, 6f, 8f, 1f),
                PartPose.offset(0f, 12f, 0f));
        root.addOrReplaceChild("cloth_b", CubeListBuilder.create()
                .texOffs(44, 21).addBox(-3f, 0f, 2.5f, 6f, 7f, 1f),
                PartPose.offset(0f, 12f, 0f));

        root.addOrReplaceChild("leg_r", CubeListBuilder.create()
                .texOffs(62, 34).addBox(-2f, 4f, -3.2f, 4f, 3f, 1f)
                .texOffs(106, 34).addBox(-0.5f, 5f, -3.9f, 1f, 1f, 1f),
                PartPose.offset(0f, 0f, 0f));
        root.addOrReplaceChild("leg_l", CubeListBuilder.create()
                .texOffs(72, 34).addBox(-2f, 4f, -3.2f, 4f, 3f, 1f)
                .texOffs(110, 34).addBox(-0.5f, 5f, -3.9f, 1f, 1f, 1f),
                PartPose.offset(0f, 0f, 0f));

        root.addOrReplaceChild("boot_r", CubeListBuilder.create()
                .texOffs(20, 29).addBox(-2f, 7f, -2f, 4f, 1f, 4f, new CubeDeformation(1.15f)),
                PartPose.offset(0f, 0f, 0f));
        root.addOrReplaceChild("boot_l", CubeListBuilder.create()
                .texOffs(36, 29).addBox(-2f, 7f, -2f, 4f, 1f, 4f, new CubeDeformation(1.15f)),
                PartPose.offset(0f, 0f, 0f));

        PartDefinition cape = root;
        cape = cape.addOrReplaceChild("cape_0", CubeListBuilder.create()
                .texOffs(52, 29).addBox(0f, 0f, 0f, 9f, 4f, 1f),
                PartPose.offset(-2f, -1f, 4.2f));
        cape = cape.addOrReplaceChild("cape_1", CubeListBuilder.create()
                .texOffs(72, 29).addBox(0f, 0f, 0f, 9f, 4f, 1f),
                PartPose.offset(0f, 4f, 0f));
        cape = cape.addOrReplaceChild("cape_2", CubeListBuilder.create()
                .texOffs(92, 29).addBox(0f, 0f, 0f, 9f, 4f, 1f),
                PartPose.offset(0f, 4f, 0f));
        cape = cape.addOrReplaceChild("cape_3", CubeListBuilder.create()
                .texOffs(0, 34).addBox(0f, 0f, 0f, 9f, 4f, 1f),
                PartPose.offset(0f, 4f, 0f));
        cape = cape.addOrReplaceChild("cape_4", CubeListBuilder.create()
                .texOffs(20, 34).addBox(0f, 0f, 0f, 9f, 4f, 1f),
                PartPose.offset(0f, 4f, 0f));

        return LayerDefinition.create(mesh, TEX_W, TEX_H);
    }
}
