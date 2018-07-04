package net.earthcomputer.enchcrack.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.earthcomputer.enchcrack.EnchantmentCracker;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemCarrotOnAStick;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

@Mixin(ItemCarrotOnAStick.class)
public abstract class MixinItemCarrotOnAStick extends Item {

	@Inject(method = "onItemRightClick", at = @At("HEAD"))
	public void onOnItemRightClick(World world, EntityPlayer player, EnumHand hand,
			CallbackInfoReturnable<ActionResult<ItemStack>> ci) {
		if (world.isRemote) {
			EnchantmentCracker.toolDamageCheck(player.getHeldItem(hand), 7);
		}
	}

}
