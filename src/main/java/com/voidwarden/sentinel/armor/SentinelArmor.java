package com.voidwarden.sentinel.armor;

import java.util.Map;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.EquipmentAssets;

/** Independent Sentinel armor set; does not replace Voidwarden equipment. */
public final class SentinelArmor {
    public static final String MOD_ID = "voidwarden";
    public static final TagKey<Item> REPAIRS =
            TagKey.create(Registries.ITEM, id("repairs_sentinel_armor"));
    public static final ResourceKey<EquipmentAsset> ASSET =
            ResourceKey.create(EquipmentAssets.ROOT_ID, id("sentinel"));
    public static final ArmorMaterial MATERIAL = new ArmorMaterial(
            37,
            Map.of(ArmorType.BOOTS, 4, ArmorType.LEGGINGS, 7, ArmorType.CHESTPLATE, 9, ArmorType.HELMET, 4),
            20, SoundEvents.ARMOR_EQUIP_NETHERITE, 4.0f, 0.15f, REPAIRS, ASSET);

    public static Item HELMET, CHESTPLATE, LEGGINGS, BOOTS;

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }

    private static Item armor(String name, ArmorType type, int durability) {
        Item item = new Item(new Item.Properties()
                .setId(ResourceKey.create(Registries.ITEM, id(name)))
                .fireResistant()
                .humanoidArmor(MATERIAL, type)
                .durability(durability)
                .rarity(Rarity.EPIC));
        return Registry.register(BuiltInRegistries.ITEM, id(name), item);
    }

    public static void init() {
        HELMET = armor("sentinel_helmet", ArmorType.HELMET, 507);
        CHESTPLATE = armor("sentinel_chestplate", ArmorType.CHESTPLATE, 692);
        LEGGINGS = armor("sentinel_leggings", ArmorType.LEGGINGS, 655);
        BOOTS = armor("sentinel_boots", ArmorType.BOOTS, 581);
    }

    public static boolean isSentinel(ItemStack stack) {
        return stack.is(HELMET) || stack.is(CHESTPLATE)
                || stack.is(LEGGINGS) || stack.is(BOOTS);
    }

    private SentinelArmor() {}
}