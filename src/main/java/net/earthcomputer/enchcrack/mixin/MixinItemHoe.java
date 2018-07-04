package net.earthcomputer.enchcrack.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.earthcomputer.enchcrack.EnchantmentCracker;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@Mixin(ItemHoe.class)
public abstract class MixinItemHoe extends Item {

	@Inject(method = "setBlock", at = @At("HEAD"))
	public void onSetBlock(ItemStack stack, EntityPlayer player, World world, BlockPos pos, IBlockState state,
			CallbackInfo ci) {
		EnchantmentCracker.toolDamageCheck(stack, 1);
	}

}
