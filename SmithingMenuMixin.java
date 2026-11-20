package com.voidwarden.mixin;

import com.voidwarden.ModItems;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.SmithingMenu;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Smithing slots: 0 template, 1 base, 2 addition, 3 result. Blocks upgrading ENCHANTED netherite. */
@Mixin(SmithingMenu.class)
public class SmithingMenuMixin {
    @Inject(method = "createResult", at = @At("TAIL"))
    private void voidwarden$noEnchanted(CallbackInfo ci) {
        AbstractContainerMenu menu = (AbstractContainerMenu) (Object) this;
        ItemStack base = menu.getSlot(1).getItem();
        ItemStack result = menu.getSlot(3).getItem();
        if (ModItems.isVoidArmor(result) && base.isEnchanted()) {
            menu.getSlot(3).set(ItemStack.EMPTY);
        }
    }
}
