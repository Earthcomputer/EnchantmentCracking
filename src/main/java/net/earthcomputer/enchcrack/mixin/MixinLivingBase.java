package net.earthcomputer.enchcrack.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.earthcomputer.enchcrack.EnchantmentCracker;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

@Mixin(EntityLivingBase.class)
public abstract class MixinLivingBase extends Entity {

	public MixinLivingBase(World world) {
		super(world);
	}

	@Inject(method = "attackEntityFrom", at = @At("HEAD"))
	public void onAttackEntityFrom(DamageSource source, float amount, CallbackInfoReturnable<Boolean> ci) {
		if (world.isRemote && (Object) this instanceof EntityPlayer)
			EnchantmentCracker.resetCracker("playerHurt");
	}

	@Inject(method = "updateItemUse", at = @At("HEAD"))
	public void onUpdateItemUse(ItemStack stack, int eatingParticleCount, CallbackInfo ci) {
		if (world.isRemote && (Object) this instanceof EntityPlayer) {
			EnumAction action = stack.getItemUseAction();
			if (action == EnumAction.EAT || action == EnumAction.DRINK) {
				EnchantmentCracker.resetCracker("eat");
			}
		}
	}

}
