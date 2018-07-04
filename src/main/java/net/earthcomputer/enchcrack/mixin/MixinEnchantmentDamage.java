package net.earthcomputer.enchcrack.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.earthcomputer.enchcrack.EnchantmentCracker;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentDamage;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;

@Mixin(EnchantmentDamage.class)
public abstract class MixinEnchantmentDamage extends Enchantment {

	protected MixinEnchantmentDamage(Rarity rarityIn, EnumEnchantmentType typeIn, EntityEquipmentSlot[] slots) {
		super(rarityIn, typeIn, slots);
	}

	@Inject(method = "onEntityDamaged", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/EntityLivingBase;addPotionEffect(Lnet/minecraft/potion/PotionEffect;)V"))
	public void baneOfArthropodsHook(EntityLivingBase user, Entity target, int level, CallbackInfo ci) {
		if (user instanceof EntityPlayerSP)
			EnchantmentCracker.resetCracker("baneOfArthropods");
	}

}
