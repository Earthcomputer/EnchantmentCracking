package net.earthcomputer.enchcrack.mixin;

import java.util.Set;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.earthcomputer.enchcrack.EnchantmentCracker;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemTool;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@Mixin(ItemSpade.class)
public abstract class MixinItemSpade extends ItemTool {

	protected MixinItemSpade(ToolMaterial material, Set<Block> effectiveBlocks) {
		super(material, effectiveBlocks);
	}

	@Inject(method = "onItemUse", at = @At(value = "FIELD", target = "Lnet/minecraft/world/World;isRemote:Z"))
	public void onOnItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing,
			float hitX, float hitY, float hitZ, CallbackInfoReturnable<EnumActionResult> ci) {
		if (world.isRemote) {
			EnchantmentCracker.toolDamageCheck(player.getHeldItem(hand), 1);
		}
	}

}
