package net.earthcomputer.enchcrack.mixin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.earthcomputer.enchcrack.EnchantmentCracker;
import net.earthcomputer.enchcrack.EnchantmentInstance;
import net.earthcomputer.enchcrack.GuiSlotEnchantment;
import net.earthcomputer.enchcrack.EnchantmentCracker.EnchantManipulationStatus;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiEnchantment;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerEnchantment;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

@Mixin(GuiEnchantment.class)
public abstract class MixinGuiEnchantment extends GuiContainer {

	public MixinGuiEnchantment(Container inventorySlots) {
		super(inventorySlots);
	}

	private GuiButton addInfoButton;
	private GuiSlotEnchantment list;
	private List<EnchantmentInstance> enchantments = new ArrayList<>();
	private Map<EnchantmentInstance, GuiButton> wantedButtons = new HashMap<>();
	private Map<EnchantmentInstance, GuiButton> unwantedButtons = new HashMap<>();
	private Map<EnchantmentInstance, GuiButton> dontCareButtons = new HashMap<>();
	private GuiButton manipulateButton;

	@Inject(method = "drawGuiContainerBackgroundLayer", at = @At("RETURN"))
	public void drawOverlay(float partialTicks, int mouseX, int mouseY, CallbackInfo ci) {
		List<EnchantmentData> wanted = new ArrayList<>();
		List<EnchantmentData> unwanted = new ArrayList<>();
		for (EnchantmentData ench : enchantments) {
			if (!wantedButtons.get(ench).enabled) {
				wanted.add(ench);
			} else if (!unwantedButtons.get(ench).enabled) {
				unwanted.add(ench);
			}
		}
		EnchantmentCracker.drawEnchantmentGUIOverlay(this, wanted, unwanted);
	}

	@Inject(method = "mouseClicked", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/PlayerControllerMP;sendEnchantPacket(II)V"))
	public void onEnchantedItem(int mouseX, int mouseY, int mouseButton, CallbackInfo ci) {
		EnchantmentCracker.onEnchantedItem();
	}

	@Inject(method = "drawScreen", at = @At("RETURN"))
	public void onDrawScreen(int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
		if (EnchantmentCracker.isCracked())
			list.drawScreen(mouseX, mouseY, partialTicks);
	}

	@Override
	public void handleMouseInput() throws IOException {
		super.handleMouseInput();
		if (EnchantmentCracker.isCracked())
			list.handleMouseInput();
	}

	@Override
	public void initGui() {
		super.initGui();
		addInfoButton = addButton(
				new GuiButton(0, width / 2 + xSize / 2 + 10, height / 2 - ySize / 2 - 25, width / 2 - xSize / 2 - 20,
						20, I18n.format("enchCrack.addInfo")));
		addInfoButton.enabled = false;
		manipulateButton = addButton(
				new GuiButton(1, width / 2 + xSize / 2 + 10, height / 2 + ySize / 2 + 5, width / 2 - xSize / 2 - 20,
						20, I18n.format("enchCrack.manipulate")));
		manipulateButton.enabled = false;
		list = new GuiSlotEnchantment(Minecraft.getMinecraft(), width / 2 - xSize / 2, ySize,
				height / 2 - ySize / 2, height / 2 + ySize / 2, 30, width / 2 + xSize / 2 + 5, enchantments,
				wantedButtons, unwantedButtons, dontCareButtons);
	}

	@Override
	public void handleMouseClick(Slot slot, int slotId, int mouseButton, ClickType clickType) {
		super.handleMouseClick(slot, slotId, mouseButton, clickType);
		ItemStack stack = inventorySlots.getSlot(0).getStack();
		addInfoButton.enabled = !stack.isEmpty() && !stack.isItemEnchanted()
				&& stack.getItem().getItemEnchantability() > 0;
		manipulateButton.enabled = addInfoButton.enabled && EnchantmentCracker.isCracked();
		recalculateEnchantments();
	}

	@Override
	public void actionPerformed(GuiButton button) {
		switch (button.id) {
		case 0:
			RayTraceResult objMouseOver = Minecraft.getMinecraft().objectMouseOver;
			if (objMouseOver.typeOfHit == RayTraceResult.Type.BLOCK) {
				EnchantmentCracker.addEnchantmentSeedInfo(Minecraft.getMinecraft().world, objMouseOver.getBlockPos(),
						(ContainerEnchantment) inventorySlots);
				manipulateButton.enabled = inventorySlots.getSlot(0).getHasStack() && EnchantmentCracker.isCracked();
			}
			break;
		case 1:
			Predicate<List<EnchantmentData>> predicate = l -> true;
			for (EnchantmentInstance ench : enchantments) {
				if (!wantedButtons.get(ench).enabled) {
					predicate = predicate.and(l -> l.contains(ench));
				} else if (!unwantedButtons.get(ench).enabled) {
					predicate = predicate.and(l -> !l.contains(ench));
				}
			}
			EnchantManipulationStatus status = EnchantmentCracker
					.manipulateEnchantments(inventorySlots.getSlot(0).getStack().getItem(), predicate);
			if (status != EnchantManipulationStatus.OK) {
				ITextComponent message = new TextComponentTranslation(status.getTranslation());
				message.getStyle().setColor(TextFormatting.RED);
				Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(message);
			}
			break;
		}
	}

	private void recalculateEnchantments() {
		enchantments.clear();

		ItemStack stack = inventorySlots.getSlot(0).getStack();
		int enchantability = stack.getItem().getItemEnchantability();
		if (stack.isEmpty()) {
			return;
		}
		if (enchantability == 0) {
			return;
		}
		if (!EnchantmentHelper.getEnchantments(stack).isEmpty()) {
			return;
		}

		int minLevel = 2;
		int maxLevel = 31 + enchantability / 4 + enchantability / 4;
		maxLevel = Math.round(maxLevel + maxLevel * 0.15f);

		for (Enchantment enchantment : Enchantment.REGISTRY) {
			if (!enchantment.isTreasureEnchantment()
					&& (enchantment.type.canEnchantItem(stack.getItem()) || stack.getItem() == Items.BOOK)) {
				for (int enchLvl = enchantment.getMinLevel(); enchLvl <= enchantment.getMaxLevel(); enchLvl++) {
					if (maxLevel >= enchantment.getMinEnchantability(enchLvl)
							&& minLevel <= enchantment.getMaxEnchantability(enchLvl)) {
						EnchantmentInstance ench = new EnchantmentInstance(enchantment, enchLvl);
						enchantments.add(ench);
						if (!wantedButtons.containsKey(ench)) {
							wantedButtons.put(ench, new GuiButton(0, 0, 0, 10, 10, "Y"));
							unwantedButtons.put(ench, new GuiButton(0, 0, 0, 10, 10, "N"));
							GuiButton dontCareButton = new GuiButton(0, 0, 0, 10, 10, "DC");
							dontCareButton.enabled = false;
							dontCareButtons.put(ench, dontCareButton);
						}
					}
				}
			}
		}
	}

}
