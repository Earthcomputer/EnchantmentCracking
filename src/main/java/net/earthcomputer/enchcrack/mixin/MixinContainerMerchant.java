package net.earthcomputer.enchcrack.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.earthcomputer.enchcrack.EnchantmentCracker;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerMerchant;
import net.minecraft.inventory.InventoryMerchant;

@Mixin(ContainerMerchant.class)
public abstract class MixinContainerMerchant extends Container {

	@Shadow
	private InventoryMerchant merchantInventory;

	@Inject(method = "onContainerClosed", at = @At("HEAD"))
	public void onOnContainerClosed(EntityPlayer player, CallbackInfo ci) {
		for (int slot = 0; slot < 2; slot++) {
			if (!merchantInventory.getStackInSlot(slot).isEmpty()) {
				EnchantmentCracker.dropItemCheck();
				break;
			}
		}
	}

}
