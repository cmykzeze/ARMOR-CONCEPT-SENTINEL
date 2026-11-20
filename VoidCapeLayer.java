package com.voidwarden.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.voidwarden.ModItems;
import com.voidwarden.VoidWarden;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;

/**
 * Asymmetric, semi-long cape hanging off the LEFT shoulder. Four chained segments
 * sway with phase-shifted sine waves and player movement.
 */
public class VoidCapeLayer extends RenderLayer<AvatarRenderState, PlayerModel> {
    private static final Identifier TEX = VoidWarden.id("textures/entity/cape/voidwarden_cape.png");
    private static final int SEGS = 4;
    private final ModelPart root;
    private final ModelPart[] seg = new ModelPart[SEGS];

    public VoidCapeLayer(RenderLayerParent<AvatarRenderState, PlayerModel> parent) {
        super(parent);
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition cur = mesh.getRoot();
        for (int i = 0; i < SEGS; i++) {
            cur = cur.addOrReplaceChild("s" + i,
                    CubeListBuilder.create().texOffs(0, i * 8).addBox(-1f, 0f, -1f, 7f, 6f, 1f),
                    PartPose.offset(0f, i == 0 ? 0f : 6f, 0f));
        }
        root = LayerDefinition.create(mesh, 64, 64).bakeRoot();
        ModelPart p = root;
        for (int i = 0; i < SEGS; i++) {
            p = p.getChild("s" + i);
            seg[i] = p;
        }
    }

    @Override
    public void submit(PoseStack pose, SubmitNodeCollector collector, int light, AvatarRenderState state, float yRot, float xRot) {
        if (!state.chestEquipment.is(ModItems.CHESTPLATE)) return;
        float t = state.ageInTicks;
        float walk = Math.min(1f, state.walkAnimationSpeed);
        for (int i = 0; i < SEGS; i++) {
            float k = (i + 1) / (float) SEGS;
            seg[i].xRot = (i == 0 ? 0.10f : 0.05f)
                    + (float) Math.sin(t * 0.13f + i * 0.8f) * 0.10f * k
                    + walk * 0.28f * k + (state.isCrouching ? 0.12f : 0f);
            seg[i].zRot = (float) Math.sin(t * 0.09f + i * 1.1f + 1.3f) * 0.09f * k - walk * 0.05f * k;
            seg[i].yRot = (float) Math.sin(t * 0.07f + i * 0.6f) * 0.05f * k;
        }
        pose.pushPose();
        pose.translate(0.0f, state.isCrouching ? 0.14f : 0.0f, 0.125f);
        collector.submitModelPart(root, pose, RenderTypes.entityCutout(TEX), light, OverlayTexture.NO_OVERLAY, null);
        pose.popPose();
    }
}
