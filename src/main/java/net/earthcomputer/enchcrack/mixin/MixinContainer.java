package net.earthcomputer.enchcrack.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.earthcomputer.enchcrack.EnchantmentCracker;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraft.item.ItemStack;

@Mixin(Container.class)
public class MixinContainer {

	@Redirect(method = "onContainerClosed", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/EntityPlayer;dropItem(Lnet/minecraft/item/ItemStack;Z)Lnet/minecraft/entity/item/EntityItem;"))
	public EntityItem redirectDropItem(EntityPlayer player, ItemStack stack, boolean unused) {
		if (player instanceof EntityPlayerMP || !((Object) this instanceof ContainerPlayer) || !EnchantmentCracker.isEnchantManipulating())
			return player.dropItem(stack, unused);
		else
			return null;
	}
	
}
