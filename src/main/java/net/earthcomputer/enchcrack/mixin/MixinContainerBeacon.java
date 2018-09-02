package net.earthcomputer.enchcrack.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.earthcomputer.enchcrack.EnchantmentCracker;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerBeacon;

@Mixin(ContainerBeacon.class)
public abstract class MixinContainerBeacon extends Container {

	@Inject(method = "onContainerClosed", at = @At("HEAD"))
	public void onOnContainerClosed(EntityPlayer player, CallbackInfo ci) {
		if (player instanceof EntityPlayerSP) {
			if (this.inventorySlots.get(0).getStack().getCount() > 1) {
				EnchantmentCracker.dropItemCheck();
			}
		}
	}

}
