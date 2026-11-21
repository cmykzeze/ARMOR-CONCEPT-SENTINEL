package com.voidwarden.client;

import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityRenderLayerRegistrationCallback;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;

public final class SentinelArmorClient {
    private SentinelArmorClient() {}
    public static void init() {
        LivingEntityRenderLayerRegistrationCallback.EVENT.register((type, renderer, helper, context) -> {
            if (renderer instanceof AvatarRenderer<?> avatarRenderer) helper.register(new SentinelArmorLayer(avatarRenderer));
        });
    }
}
