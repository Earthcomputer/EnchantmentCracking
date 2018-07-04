package net.earthcomputer.enchcrack.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.mojang.authlib.GameProfile;

import net.earthcomputer.enchcrack.EnchantmentCracker;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

@Mixin(EntityPlayerSP.class)
public abstract class MixinPlayerSP extends EntityPlayer {

	public MixinPlayerSP(World world, GameProfile gameProfile) {
		super(world, gameProfile);
	}

	@Inject(method = "dropItem", at = @At("HEAD"))
	public void onDropItem(boolean dropAll, CallbackInfoReturnable<EntityItem> ci) {
		EnchantmentCracker.dropItemCheck();
	}

	@Inject(method = "dropItemAndGetStack", at = @At("HEAD"))
	public void onDropItemAndGetStack(EntityItem item, CallbackInfoReturnable<ItemStack> ci) {
		EnchantmentCracker.dropItemCheck();
	}

}
