package net.earthcomputer.enchcrack.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.earthcomputer.enchcrack.EnchantmentCracker;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

@Mixin(EntitySnowman.class)
public abstract class MixinEntitySnowman extends EntityGolem {

	public MixinEntitySnowman(World world) {
		super(world);
	}

	@Inject(method = "processInteract", at = @At("HEAD"))
	public void onProcessInteract(EntityPlayer player, EnumHand hand, CallbackInfoReturnable<Boolean> ci) {
		if (world.isRemote) {
			ItemStack stack = player.getHeldItem(hand);
			if (stack.getItem() == Items.SHEARS && ((EntitySnowman) (Object) this).isPumpkinEquipped()) {
				EnchantmentCracker.toolDamageCheck(stack, 1);
			}
		}
	}

}
