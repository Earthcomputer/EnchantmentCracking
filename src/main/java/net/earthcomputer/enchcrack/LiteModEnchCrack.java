package net.earthcomputer.enchcrack;

import java.io.File;

import com.mumfrey.liteloader.LiteMod;
import com.mumfrey.liteloader.OutboundChatListener;
import com.mumfrey.liteloader.Tickable;

import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.CPacketChatMessage;

public class LiteModEnchCrack implements LiteMod, OutboundChatListener, Tickable {

	@Override
	public String getName() {
		return "enchantment_cracking";
	}

	@Override
	public String getVersion() {
		return "1.0";
	}

	@Override
	public void init(File configPath) {
	}

	@Override
	public void upgradeSettings(String version, File configPath, File oldConfigPath) {
	}

	@Override
	public void onSendChatMessage(CPacketChatMessage packet, String message) {
		if (message.startsWith("/") && message.substring(1).trim().startsWith("give")) {
			EnchantmentCracker.resetCracker("give");
		}
	}

	@Override
	public void onTick(Minecraft minecraft, float partialTicks, boolean inGame, boolean clock) {
		if (inGame && clock)
			EnchantmentCracker.onTick();
	}

}
