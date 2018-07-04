package net.earthcomputer.enchcrack.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.earthcomputer.enchcrack.EnchantmentCracker;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

@Mixin(targets = "net.minecraft.inventory.ContainerRepair$2")
public abstract class MixinContainerRepairOutputSlot extends Slot {

	public MixinContainerRepairOutputSlot(IInventory inventoryIn, int index, int xPosition, int yPosition) {
		super(inventoryIn, index, xPosition, yPosition);
	}

	@Inject(method = "onTake("
			+ "Lnet/minecraft/entity/player/EntityPlayer;"
			+ "Lnet/minecraft/item/ItemStack;"
			+ ")Lnet/minecraft/item/ItemStack;", at = @At("HEAD"))
	public void onOnTake(EntityPlayer player, ItemStack stack, CallbackInfo ci) {
		if (!player.capabilities.isCreativeMode) {
			EnchantmentCracker.resetCracker("anvil");
		}
	}

}
