package net.earthcomputer.enchcrack.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.earthcomputer.enchcrack.EnchantmentCracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;

@Mixin(Minecraft.class)
public abstract class MixinMinecraft {

	@Inject(method = "loadWorld(Lnet/minecraft/client/multiplayer/WorldClient;Ljava/lang/String;)V", at = @At("HEAD"))
	public void onUnloadWorld(WorldClient world, String loadingMessage, CallbackInfo ci) {
		EnchantmentCracker.resetCracker("recreatePlayer");
	}

}
