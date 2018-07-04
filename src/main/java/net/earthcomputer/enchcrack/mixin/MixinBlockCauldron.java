package net.earthcomputer.enchcrack.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.earthcomputer.enchcrack.EnchantmentCracker;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCauldron;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@Mixin(BlockCauldron.class)
public abstract class MixinBlockCauldron extends Block {

	protected MixinBlockCauldron(Material material) {
		super(material);
	}

	@Inject(method = "onBlockActivated", at = @At("HEAD"))
	public void onOnBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,
			EnumFacing facing, float hitX, float hitY, float hitZ, CallbackInfoReturnable<Boolean> ci) {
		if (!world.isRemote)
			return;
		ItemStack heldItem = player.getHeldItem(hand);
		if (heldItem.isEmpty())
			return;
		int level = state.getValue(BlockCauldron.LEVEL);
		Item item = heldItem.getItem();
		if (!player.capabilities.isCreativeMode && heldItem.getCount() > 1) {
			if (player.inventory.getFirstEmptyStack() == -1) {
				if (item == Items.BUCKET && level == 3) {
					EnchantmentCracker.dropItemCheck();
				} else if (item == Items.GLASS_BOTTLE && level > 0) {
					EnchantmentCracker.dropItemCheck();
				}
			} else if (item == Items.BANNER && TileEntityBanner.getPatterns(heldItem) > 0 && level > 0) {
				ItemStack cleanedBanner = heldItem.copy();
				cleanedBanner.setCount(1);
				TileEntityBanner.removeBannerData(cleanedBanner);
				if (player.inventory.storeItemStack(cleanedBanner) == -1) {
					EnchantmentCracker.dropItemCheck();
				}
			}
		}
	}

}
