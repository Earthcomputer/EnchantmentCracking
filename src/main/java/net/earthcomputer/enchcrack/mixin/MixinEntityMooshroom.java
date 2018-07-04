package net.earthcomputer.enchcrack.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.earthcomputer.enchcrack.EnchantmentCracker;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityMooshroom;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

@Mixin(EntityMooshroom.class)
public abstract class MixinEntityMooshroom extends EntityCow {

	public MixinEntityMooshroom(World world) {
		super(world);
	}

	@Inject(method = "processInteract", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/passive/EntityMooshroom;setDead()V"))
	//@Inject(method = "processInteract", at = @At("HEAD"))
	public void onProcessInteract(EntityPlayer player, EnumHand hand, CallbackInfoReturnable<Boolean> ci) {
		if (world.isRemote) {
			EnchantmentCracker.toolDamageCheck(player.getHeldItem(hand), 1);
		}
	}

}
