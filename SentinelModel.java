package com.voidwarden.client;

import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

/** Generated Sentinel crown, layered armor, tabard, greaves and five-part cape geometry. */
final class SentinelModel {
    static final int CAPE_SEGS=5;
    static LayerDefinition create(){
        MeshDefinition mesh=new MeshDefinition(); PartDefinition r=mesh.getRoot();
        r.addOrReplaceChild("head",CubeListBuilder.create()
            .texOffs(0,0).addBox(-5,-9.5f,-5,10,1,10,new CubeDeformation(.35f))
            .texOffs(104,11).addBox(-1.5f,-17,-5.5f,3,7,1)
            .texOffs(114,21).addBox(-4,-14,-5.2f,2,4,1).texOffs(120,21).addBox(2,-14,-5.2f,2,4,1)
            .texOffs(58,21).addBox(-5.2f,-15,-5.2f,1,5,1).texOffs(62,21).addBox(4.2f,-15,-5.2f,1,5,1)
            .texOffs(66,21).addBox(-5.2f,-15,4.2f,1,5,1).texOffs(70,21).addBox(4.2f,-15,4.2f,1,5,1),PartPose.ZERO);
        r.addOrReplaceChild("chest",CubeListBuilder.create().texOffs(40,0).addBox(-3,.5f,-3.5f,6,10,1).texOffs(24,11).addBox(-3,0,3.1f,6,8,1).texOffs(86,34).addBox(-1,3,4,2,2,1),PartPose.ZERO);
        r.addOrReplaceChild("arm_r",CubeListBuilder.create().texOffs(38,11).addBox(-4.5f,-4,-3.5f,6,2,7,new CubeDeformation(.2f)).texOffs(54,0).addBox(-5,-2,-3.5f,5,3,7,new CubeDeformation(.3f)).texOffs(0,21).addBox(-5,1,-3,5,2,6,new CubeDeformation(.3f)).texOffs(102,0).addBox(-4,5,-3,6,4,6,new CubeDeformation(.35f)),PartPose.ZERO);
        r.addOrReplaceChild("arm_l",CubeListBuilder.create().texOffs(64,11).addBox(-1.5f,-4,-3.5f,6,2,7,new CubeDeformation(.2f)).texOffs(78,0).addBox(0,-2,-3.5f,5,3,7,new CubeDeformation(.3f)).texOffs(22,21).addBox(0,1,-3,5,2,6,new CubeDeformation(.3f)).texOffs(0,11).addBox(-2,5,-3,6,4,6,new CubeDeformation(.35f)),PartPose.ZERO);
        r.addOrReplaceChild("belt",CubeListBuilder.create().texOffs(74,21).addBox(-4,9,-2,8,2,4,new CubeDeformation(1.3f)).texOffs(52,34).addBox(-2,8.5f,-4,4,3,1).texOffs(92,34).addBox(-1,9,-4.7f,2,2,1),PartPose.ZERO);
        r.addOrReplaceChild("cloth_f",CubeListBuilder.create().texOffs(90,11).addBox(-3,0,-3.5f,6,8,1),PartPose.offset(0,12,0));
        r.addOrReplaceChild("cloth_b",CubeListBuilder.create().texOffs(44,21).addBox(-3,0,2.5f,6,7,1),PartPose.offset(0,12,0));
        r.addOrReplaceChild("leg_r",CubeListBuilder.create().texOffs(62,34).addBox(-2,4,-3.2f,4,3,1),PartPose.ZERO);
        r.addOrReplaceChild("leg_l",CubeListBuilder.create().texOffs(72,34).addBox(-2,4,-3.2f,4,3,1),PartPose.ZERO);
        r.addOrReplaceChild("boot_r",CubeListBuilder.create().texOffs(20,29).addBox(-2,7,-2,4,1,4,new CubeDeformation(1.15f)),PartPose.ZERO);
        r.addOrReplaceChild("boot_l",CubeListBuilder.create().texOffs(36,29).addBox(-2,7,-2,4,1,4,new CubeDeformation(1.15f)),PartPose.ZERO);
        PartDefinition cape=r;
        int[][] uv={{52,29},{72,29},{92,29},{0,34},{20,34}};
        for(int i=0;i<CAPE_SEGS;i++) cape=cape.addOrReplaceChild("cape_"+i,CubeListBuilder.create().texOffs(uv[i][0],uv[i][1]).addBox(0,0,0,9,4,1),PartPose.offset(i==0?-2:0,i==0?-1:4,i==0?4.2f:0));
        return LayerDefinition.create(mesh,128,39);
    }
}
