package net.earthcomputer.enchcrack.mixin;

import java.util.Random;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.earthcomputer.enchcrack.EnchantmentCracker;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;

@Mixin(ItemStack.class)
public abstract class MixinItemStack {

	@Inject(method = "attemptDamageItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/enchantment/EnchantmentDurability;negateDamage("
			+ "Lnet/minecraft/item/ItemStack;"
			+ "I"
			+ "Ljava/util/Random;"
			+ ")Z"))
	public void onAttemptDamageItem(int amount, Random rand, EntityPlayerMP damager,
			CallbackInfoReturnable<Boolean> ci) {
		if (rand == Minecraft.getMinecraft().player.getRNG()) {
			EnchantmentCracker.toolDamageCheck((ItemStack) (Object) this, amount);
		}
	}

}
