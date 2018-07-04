package net.earthcomputer.enchcrack.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.earthcomputer.enchcrack.EnchantmentCracker;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@Mixin(ItemTool.class)
public abstract class MixinItemTool extends Item {

	@Inject(method = "onBlockDestroyed", at = @At("HEAD"))
	public void onOnBlockDestroyed(ItemStack stack, World world, IBlockState state, BlockPos pos,
			EntityLivingBase entity, CallbackInfoReturnable<Boolean> ci) {
		if (world.isRemote && state.getBlockHardness(world, pos) != 0) {
			EnchantmentCracker.toolDamageCheck(stack, 1);
		}
	}

}
