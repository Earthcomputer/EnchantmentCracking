package net.earthcomputer.enchcrack.mixin;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.earthcomputer.enchcrack.EnchantmentCracker;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.CPacketRecipePlacement;

@Mixin(PlayerControllerMP.class)
public class MixinPlayerControllerMP {

	@Inject(method = "handleRecipePlacement", at = @At("HEAD"))
	public void onHandleRecipePlacement(int containerId, List<CPacketRecipePlacement.ItemMove> fromGrid,
			List<CPacketRecipePlacement.ItemMove> toGrid, EntityPlayer player, CallbackInfo ci) {
		if (fromGrid.stream().anyMatch(it -> it.destSlot == -1))
			EnchantmentCracker.dropItemCheck();
	}

}
