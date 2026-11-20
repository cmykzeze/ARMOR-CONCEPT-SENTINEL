package com.voidwarden.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityRenderLayerRegistrationCallback;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;

public class VoidWardenClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        LivingEntityRenderLayerRegistrationCallback.EVENT.register((type, renderer, helper, context) -> {
            if (renderer instanceof AvatarRenderer<?> avatarRenderer) {
                helper.register(new VoidCapeLayer(avatarRenderer));
            }
        });
    }
}
