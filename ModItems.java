package com.voidwarden;

import java.util.Map;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.EquipmentAssets;
import net.minecraft.core.Registry;

public class ModItems {
    // Netherite: helmet 407, chest 592, legs 555, boots 481 durability, 3/8/6/3 armor.
    // Voidwarden: +100 durability on every piece, +1 armor per piece, toughness 4, KB resist 0.15.
    public static final TagKey<Item> REPAIRS = TagKey.create(Registries.ITEM, VoidWarden.id("repairs_voidwarden_armor"));
    public static final ResourceKey<EquipmentAsset> ASSET =
            ResourceKey.create(EquipmentAssets.ROOT_ID, VoidWarden.id("voidwarden"));

    public static final ArmorMaterial MATERIAL = new ArmorMaterial(
            37,
            Map.of(ArmorType.BOOTS, 4, ArmorType.LEGGINGS, 7, ArmorType.CHESTPLATE, 9, ArmorType.HELMET, 4),
            20, SoundEvents.ARMOR_EQUIP_NETHERITE, 4.0f, 0.15f, REPAIRS, ASSET);

    public static Item HELMET, CHESTPLATE, LEGGINGS, BOOTS, INGOT, NUGGET;

    private static Item.Properties props(String name) {
        return new Item.Properties()
                .setId(ResourceKey.create(Registries.ITEM, VoidWarden.id(name)))
                .fireResistant();
    }
    private static Item reg(String name, Item item) {
        return Registry.register(BuiltInRegistries.ITEM, VoidWarden.id(name), item);
    }
    private static Item armor(String name, ArmorType type, int durability) {
        return reg(name, new Item(props(name).humanoidArmor(MATERIAL, type)
                .durability(durability).rarity(net.minecraft.world.item.Rarity.EPIC)));
    }

    public static void init() {
        NUGGET = reg("voidwarden_nugget", new Item(props("voidwarden_nugget")));
        INGOT = reg("voidwarden_ingot", new Item(props("voidwarden_ingot")));
        HELMET = armor("voidwarden_helmet", ArmorType.HELMET, 507);
        CHESTPLATE = armor("voidwarden_chestplate", ArmorType.CHESTPLATE, 692);
        LEGGINGS = armor("voidwarden_leggings", ArmorType.LEGGINGS, 655);
        BOOTS = armor("voidwarden_boots", ArmorType.BOOTS, 581);
    }

    public static boolean isVoidArmor(ItemStack s) {
        return s.is(HELMET) || s.is(CHESTPLATE) || s.is(LEGGINGS) || s.is(BOOTS);
    }
}
