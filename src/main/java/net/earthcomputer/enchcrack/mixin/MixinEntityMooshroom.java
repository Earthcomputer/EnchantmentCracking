package net.earthcomputer.enchcrack.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.earthcomputer.enchcrack.EnchantmentCracker;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.passive.EntityMooshroom;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

@Mixin(EntityMooshroom.class)
public abstract class MixinEntityMooshroom {

	@Inject(method = "processInteract", at = @At("HEAD") )
	public void onProcessInteract(EntityPlayer player, EnumHand hand, CallbackInfoReturnable<Boolean> ci) {
		if (((Entity) (Object) this).world.isRemote) {
			ItemStack heldItem = player.getHeldItem(hand);
			if (heldItem.getItem() == Items.SHEARS && ((EntityAgeable) (Object) this).getGrowingAge() >= 0)
				EnchantmentCracker.toolDamageCheck(heldItem, 1);
		}
	}

}
