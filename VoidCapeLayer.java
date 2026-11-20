package com.voidwarden.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.voidwarden.ModItems;
import com.voidwarden.VoidWarden;
import java.util.Map;
import java.util.WeakHashMap;
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
 * Four articulated cloth panels attached asymmetrically to the left shoulder.
 *
 * Each avatar render state owns a tiny damped-spring simulation. Motion is
 * integrated in bounded substeps, so the result is stable across frame rates
 * and one player's cape can never affect another player's cape.
 */
public class VoidCapeLayer extends RenderLayer<AvatarRenderState, PlayerModel> {
    private static final Identifier TEX = VoidWarden.id("textures/entity/cape/voidwarden_cape.png");
    private static final int SEGS = 4;
    private static final float MAX_FRAME_STEP = 0.10f;
    private static final float SUBSTEP = 1.0f / 60.0f;
    private final ModelPart root;
    private final ModelPart[] seg = new ModelPart[SEGS];
    private final Map<AvatarRenderState, CapeMotion> motionByAvatar = new WeakHashMap<>();

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
    public void submit(PoseStack pose, SubmitNodeCollector collector, int light,
                       AvatarRenderState state, float yRot, float xRot) {
        if (!state.chestEquipment.is(ModItems.CHESTPLATE)) return;

        float age = state.ageInTicks;
        float walk = Math.max(0f, Math.min(1f, state.walkAnimationSpeed));
        CapeMotion motion = motionByAvatar.computeIfAbsent(state, ignored -> new CapeMotion());
        motion.advance(age, yRot, walk, state.isCrouching);

        for (int i = 0; i < SEGS; i++) {
            seg[i].xRot = motion.pitch[i];
            seg[i].yRot = motion.yaw[i];
            seg[i].zRot = motion.roll[i];
        }

        pose.pushPose();
        // Keep the cape behind the torso and lift it slightly while crouching.
        pose.translate(0.0f, state.isCrouching ? 0.14f : 0.0f, 0.125f);
        collector.submitModelPart(root, pose, RenderTypes.entityCutout(TEX),
                light, OverlayTexture.NO_OVERLAY, null);
        pose.popPose();
    }

    private static final class CapeMotion {
        private final float[] pitch = new float[SEGS];
        private final float[] pitchVelocity = new float[SEGS];
        private final float[] yaw = new float[SEGS];
        private final float[] yawVelocity = new float[SEGS];
        private final float[] roll = new float[SEGS];
        private final float[] rollVelocity = new float[SEGS];
        private float lastAge = Float.NaN;
        private float lastYaw;
        private boolean initialized;

        void advance(float age, float bodyYawDegrees, float walk, boolean crouching) {
            if (!initialized) {
                initialized = true;
                lastAge = age;
                lastYaw = bodyYawDegrees;
                return;
            }

            float elapsed = age - lastAge;
            if (elapsed <= 0f) return;
            float dt = Math.min(elapsed, MAX_FRAME_STEP);
            float turnRadians = (float) Math.toRadians(wrapDegrees(bodyYawDegrees - lastYaw));
            float turnRate = turnRadians / Math.max(dt, 1.0e-4f);
            lastAge = age;
            lastYaw = bodyYawDegrees;

            float remaining = dt;
            while (remaining > 0f) {
                float step = Math.min(remaining, SUBSTEP);
                simulate(step, age, walk, crouching, turnRate);
                remaining -= step;
            }
        }

        private void simulate(float dt, float age, float walk, boolean crouching, float turnRate) {
            float turn = clamp(-turnRate * 0.045f, -0.34f, 0.34f);
            for (int i = 0; i < SEGS; i++) {
                float k = (i + 1f) / SEGS;
                float phase = age * 0.075f - i * 0.58f;
                // The lower panels lag farther behind the torso, like real cloth.
                float pitchTarget = 0.045f + walk * 0.54f * k
                        + (crouching ? 0.10f * k : 0f) + 0.025f * (float) Math.sin(phase);
                float yawTarget = turn * k + 0.018f * (float) Math.sin(phase * 0.8f);
                float rollTarget = -turn * 0.30f * k
                        + 0.025f * (float) Math.sin(phase + 1.1f);

                // Spring stiffness and damping are deliberately conservative.
                pitchVelocity[i] = spring(pitch[i], pitchVelocity[i], pitchTarget, 25f, 8.5f, 0.075f * k, dt);
                yawVelocity[i] = spring(yaw[i], yawVelocity[i], yawTarget, 21f, 8.0f, 0f, dt);
                rollVelocity[i] = spring(roll[i], rollVelocity[i], rollTarget, 19f, 7.5f, 0f, dt);

                pitch[i] += pitchVelocity[i] * dt;
                yaw[i] += yawVelocity[i] * dt;
                roll[i] += rollVelocity[i] * dt;

                pitch[i] = clamp(pitch[i], -0.20f, 1.05f);
                yaw[i] = clamp(yaw[i], -0.48f, 0.48f);
                roll[i] = clamp(roll[i], -0.42f, 0.42f);
            }
        }

        private static float spring(float position, float velocity, float target,
                                    float stiffness, float damping, float gravity, float dt) {
            float acceleration = (target - position) * stiffness - velocity * damping + gravity;
            return velocity + acceleration * dt;
        }

        private static float wrapDegrees(float degrees) {
            degrees %= 360f;
            if (degrees > 180f) degrees -= 360f;
            if (degrees < -180f) degrees += 360f;
            return degrees;
        }

        private static float clamp(float value, float min, float max) {
            return Math.max(min, Math.min(max, value));
        }
    }
}
