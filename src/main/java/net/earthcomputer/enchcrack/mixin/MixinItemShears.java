package net.earthcomputer.enchcrack.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.earthcomputer.enchcrack.EnchantmentCracker;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@Mixin(ItemShears.class)
public abstract class MixinItemShears extends Item {

	@Inject(method = "onBlockDestroyed", at = @At(value = "FIELD", target = "Lnet/minecraft/world/World;isRemote:Z"))
	public void onOnBlockDestroyed(ItemStack stack, World world, IBlockState state, BlockPos pos,
			EntityLivingBase entity, CallbackInfoReturnable<Boolean> ci) {
		if (world.isRemote) {
			EnchantmentCracker.toolDamageCheck(stack, 1);
		}
	}

}
