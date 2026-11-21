package com.voidwarden.client;

import net.fabricmc.api.ClientModInitializer;

public class VoidWardenClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        SentinelArmorClient.init();
    }
}
