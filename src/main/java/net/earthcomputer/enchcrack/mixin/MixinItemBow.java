package net.earthcomputer.enchcrack.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.earthcomputer.enchcrack.EnchantmentCracker;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

@Mixin(ItemBow.class)
public abstract class MixinItemBow extends Item {

	@Inject(method = "onPlayerStoppedUsing", at = @At(value = "FIELD", target = "Lnet/minecraft/world/World;isRemote:Z"))
	public void onOnPlayerStoppedUsing(ItemStack stack, World world, EntityLivingBase entity, int timeLeft,
			CallbackInfo ci) {
		if (world.isRemote) {
			EnchantmentCracker.toolDamageCheck(stack, 1);
		}
	}

}
