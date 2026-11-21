package com.voidwarden.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.voidwarden.ModItems;
import java.util.LinkedHashMap;
import java.util.Map;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;

/** 3D Sentinel armor ornaments and articulated spring-driven cape. */
public final class SentinelArmorLayer extends RenderLayer<AvatarRenderState, PlayerModel> {
    private static final Identifier TEX = Identifier.fromNamespaceAndPath("voidwarden", "textures/entity/sentinel/sentinel_gear.png");
    private static final int N = SentinelModel.CAPE_SEGS;
    private final ModelPart head, chest, rightArm, leftArm, belt, front, back, rightLeg, leftLeg, rightBoot, leftBoot;
    private final ModelPart[] cape = new ModelPart[N];
    private final Map<Integer, Motion> motions = new LinkedHashMap<>(16, .75f, true) {
        @Override protected boolean removeEldestEntry(Map.Entry<Integer, Motion> e) { return size() > 128; }
    };

    public SentinelArmorLayer(RenderLayerParent<AvatarRenderState, PlayerModel> parent) {
        super(parent);
        ModelPart root = SentinelModel.create().bakeRoot();
        head=root.getChild("head"); chest=root.getChild("chest");
        rightArm=root.getChild("arm_r"); leftArm=root.getChild("arm_l");
        belt=root.getChild("belt"); front=root.getChild("cloth_f"); back=root.getChild("cloth_b");
        rightLeg=root.getChild("leg_r"); leftLeg=root.getChild("leg_l");
        rightBoot=root.getChild("boot_r"); leftBoot=root.getChild("boot_l");
        ModelPart p=root;
        for(int i=0;i<N;i++){ p=p.getChild("cape_"+i); cape[i]=p; }
    }

    @Override public void submit(PoseStack pose, SubmitNodeCollector out, int light, AvatarRenderState s, float yaw, float pitch) {
        boolean h=s.headEquipment.is(ModItems.HELMET), c=s.chestEquipment.is(ModItems.CHESTPLATE);
        boolean l=s.legsEquipment.is(ModItems.LEGGINGS), b=s.feetEquipment.is(ModItems.BOOTS);
        if(!(h||c||l||b)) return;
        PlayerModel m=getParentModel();
        if(h) draw(pose,out,light,m.head,head);
        if(c){
            draw(pose,out,light,m.body,chest);
            draw(pose,out,light,m.rightArm,rightArm); draw(pose,out,light,m.leftArm,leftArm);
            drawCape(pose,out,light,s,yaw,m.body);
        }
        if(l){
            float swing=Math.min(0f,Math.min(m.rightLeg.xRot,m.leftLeg.xRot));
            float kick=Math.max(0f,Math.max(m.rightLeg.xRot,m.leftLeg.xRot));
            front.xRot=clamp(swing*.75f,-1.1f,0f)+(s.isCrouching?-.35f:0f);
            back.xRot=clamp(kick*.55f,0f,.8f)+(s.isCrouching?.25f:0f);
            draw(pose,out,light,m.body,belt); draw(pose,out,light,m.body,front); draw(pose,out,light,m.body,back);
            draw(pose,out,light,m.rightLeg,rightLeg); draw(pose,out,light,m.leftLeg,leftLeg);
        }
        if(b){ draw(pose,out,light,m.rightLeg,rightBoot); draw(pose,out,light,m.leftLeg,leftBoot); }
    }
    private void draw(PoseStack p,SubmitNodeCollector o,int light,ModelPart anchor,ModelPart part){
        p.pushPose(); anchor.translateAndRotate(p);
        o.submitModelPart(part,p,RenderTypes.entityCutout(TEX),light,OverlayTexture.NO_OVERLAY,null); p.popPose();
    }
    private void drawCape(PoseStack p,SubmitNodeCollector o,int light,AvatarRenderState s,float yaw,ModelPart body){
        Motion m=motions.computeIfAbsent(s.id,k->new Motion());
        m.tick(s.ageInTicks,yaw,s.walkAnimationSpeed,s.isCrouching,s.capeFlap,s.capeLean,s.capeLean2,s.isFallFlying,s.fallFlyingScale(),s.isVisuallySwimming);
        for(int i=0;i<N;i++){cape[i].xRot=m.x[i];cape[i].yRot=m.y[i];cape[i].zRot=m.z[i];}
        p.pushPose(); body.translateAndRotate(p);
        o.submitModelPart(cape[0],p,RenderTypes.entityCutout(TEX),light,OverlayTexture.NO_OVERLAY,null); p.popPose();
    }
    private static float clamp(float v,float a,float b){return Math.max(a,Math.min(b,v));}
    private static final class Motion {
        final float[] x=new float[N],y=new float[N],z=new float[N],vx=new float[N],vy=new float[N],vz=new float[N];
        float age=Float.NaN,lastYaw; boolean init;
        void tick(float now,float yaw,float walk,boolean crouch,float flap,float lean,float lean2,boolean flying,float flight,boolean swim){
            if(!init){init=true;age=now;lastYaw=yaw;return;}
            float elapsed=now-age;if(elapsed<=0)return;
            float dt=Math.min(elapsed/20f,.1f), turn=(float)Math.toRadians(wrap(yaw-lastYaw))/Math.max(dt,1e-4f);
            age=now;lastYaw=yaw;
            for(float left=dt;left>0;){float step=Math.min(left,1f/60f);step(now,step,walk,crouch,turn,flap,lean,lean2,flying,flight,swim);left-=step;}
        }
        void step(float t,float dt,float walk,boolean crouch,float turn,float flap,float lean,float lean2,boolean flying,float flight,boolean swim){
            for(int i=0;i<N;i++){float k=(i+1f)/N,ph=t*.075f-i*.58f;
                float tx=.045f+walk*.3f*k+clamp(lean*.0025f,0,.38f)*k+clamp(flap*.0045f,-.08f,.18f)*k+(flying?Math.max(.12f,flight*.34f)*k:0)+(swim?.14f*k:0)+(crouch?.1f*k:0)+.025f*(float)Math.sin(ph);
                float ty=clamp(-turn*.045f,-.34f,.34f)*k+clamp(lean2*.01f,-.22f,.22f)*k+.018f*(float)Math.sin(ph*.8f);
                float tz=clamp(-turn*.045f,-.34f,.34f)*-.3f*k+clamp(lean2*.01f,-.22f,.22f)*.65f*k+.025f*(float)Math.sin(ph+1.1f);
                vx[i]+=((tx-x[i])*25-vx[i]*8.5f+.075f*k)*dt; vy[i]+=((ty-y[i])*21-vy[i]*8)*dt; vz[i]+=((tz-z[i])*19-vz[i]*7.5f)*dt;
                x[i]=clamp(x[i]+vx[i]*dt,-.2f,1.05f);y[i]=clamp(y[i]+vy[i]*dt,-.48f,.48f);z[i]=clamp(z[i]+vz[i]*dt,-.42f,.42f);
            }
        }
        static float wrap(float d){d%=360;if(d>180)d-=360;if(d< -180)d+=360;return d;}
    }
}
