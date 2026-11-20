package com.voidwarden;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.LootItemKilledByPlayerCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;

public class VoidWarden implements ModInitializer {
    public static final String MOD_ID = "voidwarden";
    public static final float NUGGET_DROP_CHANCE = 0.25f; // 25% per Warden kill

    public static Identifier id(String path) { return Identifier.fromNamespaceAndPath(MOD_ID, path); }

    @Override
    public void onInitialize() {
        ModItems.init();

        // Warden drops a nugget (chance)
        LootTableEvents.MODIFY.register((key, builder, source, registries) -> {
            if (source.isBuiltin() && key.toString().contains("entities/warden")) {
                builder.withPool(LootPool.lootPool()
                        .when(LootItemKilledByPlayerCondition.killedByPlayer())
                        .when(LootItemRandomChanceCondition.randomChance(NUGGET_DROP_CHANCE))
                        .add(LootItem.lootTableItem(ModItems.NUGGET)));
            }
        });

        // Permanent Strength + Fire Resistance while wearing the FULL set
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            if (server.getTickCount() % 20 != 0) return;
            for (ServerPlayer p : server.getPlayerList().getPlayers()) {
                boolean full = ModItems.isVoidArmor(p.getItemBySlot(EquipmentSlot.HEAD))
                        && ModItems.isVoidArmor(p.getItemBySlot(EquipmentSlot.CHEST))
                        && ModItems.isVoidArmor(p.getItemBySlot(EquipmentSlot.LEGS))
                        && ModItems.isVoidArmor(p.getItemBySlot(EquipmentSlot.FEET));
                if (full) {
                    p.addEffect(new MobEffectInstance(MobEffects.STRENGTH, 300, 0, true, false, true));
                    p.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 300, 0, true, false, true));
                }
            }
        });
    }
}
