package net.earthcomputer.enchcrack.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.earthcomputer.enchcrack.EnchantmentCracker;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

@Mixin(EntityCreeper.class)
public abstract class MixinEntityCreeper extends EntityMob {

	public MixinEntityCreeper(World world) {
		super(world);
	}

	@Inject(method = "processInteract", at = @At(value = "FIELD", target = "Lnet/minecraft/world/World;isRemote:Z"))
	public void onProcessInteract(EntityPlayer player, EnumHand hand, CallbackInfoReturnable<Boolean> ci) {
		if (world.isRemote) {
			EnchantmentCracker.toolDamageCheck(player.getHeldItem(hand), 1);
		}
	}

}
