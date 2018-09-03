package net.earthcomputer.enchcrack.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.earthcomputer.enchcrack.EnchantmentCracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;

@Mixin(GuiContainer.class)
public class MixinGuiContainer extends GuiScreen {

	@Redirect(method = "keyTyped", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/EntityPlayerSP;closeScreen()V") )
	public void onCloseScreen(EntityPlayerSP player) {
		if ((Object) this instanceof GuiInventory && EnchantmentCracker.isEnchantManipulating()) {
			player.openContainer = player.inventoryContainer;
			Minecraft.getMinecraft().displayGuiScreen(null);
		} else {
			player.closeScreen();
		}
	}

}
