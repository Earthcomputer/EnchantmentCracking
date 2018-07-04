package net.earthcomputer.enchcrack;

import java.util.List;
import java.util.Map;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.util.math.MathHelper;

public class GuiSlotEnchantment extends GuiSlot {

	private List<EnchantmentInstance> enchantments;
	private Map<EnchantmentInstance, GuiButton> wantedButtons;
	private Map<EnchantmentInstance, GuiButton> unwantedButtons;
	private Map<EnchantmentInstance, GuiButton> dontCareButtons;

	public GuiSlotEnchantment(Minecraft mc, int width, int height, int top, int bottom, int slotHeight, int left,
			List<EnchantmentInstance> enchantments, Map<EnchantmentInstance, GuiButton> wantedButtons,
			Map<EnchantmentInstance, GuiButton> unwantedButtons,
			Map<EnchantmentInstance, GuiButton> dontCareButtons) {
		super(mc, width, height, top, bottom, slotHeight);
		setSlotXBoundsFromLeft(left);
		this.enchantments = enchantments;
		this.wantedButtons = wantedButtons;
		this.unwantedButtons = unwantedButtons;
		this.dontCareButtons = dontCareButtons;
	}

	@Override
	protected int getSize() {
		return enchantments.size();
	}

	@Override
	public void handleMouseInput() {
		if (this.isMouseYWithinSlotBounds(this.mouseY)) {
			if (Mouse.isButtonDown(0) && this.getEnabled()) {
				if (this.initialClickY == -1) {
					boolean flag1 = true;

					if (this.mouseY >= this.top && this.mouseY <= this.bottom) {
						int j2 = left + (this.width - this.getListWidth()) / 2;
						int k2 = left + (this.width + this.getListWidth()) / 2;
						int l2 = this.mouseY - this.top - this.headerPadding + (int) this.amountScrolled - 4;
						int i1 = l2 / this.slotHeight;

						if (i1 < this.getSize() && this.mouseX >= j2 && this.mouseX <= k2 && i1 >= 0 && l2 >= 0) {
							boolean flag = i1 == this.selectedElement
									&& Minecraft.getSystemTime() - this.lastClicked < 250L;
							this.elementClicked(i1, flag, this.mouseX, this.mouseY);
							this.selectedElement = i1;
							this.lastClicked = Minecraft.getSystemTime();
						} else if (this.mouseX >= j2 && this.mouseX <= k2 && l2 < 0) {
							this.clickedHeader(this.mouseX - j2,
									this.mouseY - this.top + (int) this.amountScrolled - 4);
							flag1 = false;
						}

						int i3 = this.getScrollBarX();
						int j1 = i3 + 6;

						if (this.mouseX >= i3 && this.mouseX <= j1) {
							this.scrollMultiplier = -1.0F;
							int k1 = this.getMaxScroll();

							if (k1 < 1) {
								k1 = 1;
							}

							int l1 = (int) ((float) ((this.bottom - this.top) * (this.bottom - this.top))
									/ (float) this.getContentHeight());
							l1 = MathHelper.clamp(l1, 32, this.bottom - this.top - 8);
							this.scrollMultiplier /= (float) (this.bottom - this.top - l1) / (float) k1;
						} else {
							this.scrollMultiplier = 1.0F;
						}

						if (flag1) {
							this.initialClickY = this.mouseY;
						} else {
							this.initialClickY = -2;
						}
					} else {
						this.initialClickY = -2;
					}
				} else if (this.initialClickY >= 0) {
					this.amountScrolled -= (float) (this.mouseY - this.initialClickY) * this.scrollMultiplier;
					this.initialClickY = this.mouseY;
				}
			} else {
				this.initialClickY = -1;
			}

			int i2 = Mouse.getEventDWheel();

			if (i2 != 0) {
				if (i2 > 0) {
					i2 = -1;
				} else if (i2 < 0) {
					i2 = 1;
				}

				this.amountScrolled += (float) (i2 * this.slotHeight / 2);
			}
		}
	}

	@Override
	protected void elementClicked(int slotIndex, boolean isDoubleClick, int mouseX, int mouseY) {
		EnchantmentInstance ench = enchantments.get(slotIndex);
		GuiButton wantedButton = wantedButtons.get(ench);
		GuiButton unwantedButton = unwantedButtons.get(ench);
		GuiButton dontCareButton = dontCareButtons.get(ench);
		SoundHandler sh = Minecraft.getMinecraft().getSoundHandler();
		if (wantedButton.isMouseOver()) {
			wantedButton.enabled = false;
			unwantedButton.enabled = true;
			dontCareButton.enabled = true;
			wantedButton.playPressSound(sh);
		}
		if (unwantedButton.isMouseOver()) {
			wantedButton.enabled = true;
			unwantedButton.enabled = false;
			dontCareButton.enabled = true;
			unwantedButton.playPressSound(sh);
		}
		if (dontCareButton.isMouseOver()) {
			wantedButton.enabled = true;
			unwantedButton.enabled = true;
			dontCareButton.enabled = false;
			dontCareButton.playPressSound(sh);
		}
	}

	@Override
	protected boolean isSelected(int slotIndex) {
		return false;
	}

	@Override
	protected void drawBackground() {
	}

	@Override
	public void drawScreen(int mouseXIn, int mouseYIn, float partialTicks) {
		GL11.glEnable(GL11.GL_SCISSOR_TEST);
		Minecraft mc = Minecraft.getMinecraft();
		GuiScreen gui = mc.currentScreen;
		GL11.glScissor(left * mc.displayWidth / gui.width, top * mc.displayHeight / gui.height,
				width * mc.displayWidth / gui.width, height * mc.displayHeight / gui.height);
		super.drawScreen(mouseXIn, mouseYIn, partialTicks);
		GL11.glDisable(GL11.GL_SCISSOR_TEST);
	}

	@Override
	protected void drawSlot(int slotIndex, int xPos, int yPos, int heightIn, int mouseXIn, int mouseYIn,
			float partialTicks) {
		EnchantmentInstance ench = enchantments.get(slotIndex);
		Minecraft.getMinecraft().fontRenderer.drawString(ench.enchantment.getTranslatedName(ench.enchantmentLevel),
				xPos + 5, yPos + 5, 0xffffff);

		GuiButton wantedButton = wantedButtons.get(ench);
		wantedButton.x = xPos + width - 66;
		wantedButton.y = yPos + 16;
		GuiButton unwantedButton = unwantedButtons.get(ench);
		unwantedButton.x = xPos + width - 46;
		unwantedButton.y = yPos + 16;
		GuiButton dontCareButton = dontCareButtons.get(ench);
		dontCareButton.x = xPos + width - 26;
		dontCareButton.y = yPos + 16;
		wantedButton.drawButton(mc, mouseXIn, mouseYIn, partialTicks);
		unwantedButton.drawButton(mc, mouseXIn, mouseYIn, partialTicks);
		dontCareButton.drawButton(mc, mouseXIn, mouseYIn, partialTicks);
	}

	@Override
	public int getScrollBarX() {
		return left + width - 10;
	}

	@Override
	public int getListWidth() {
		return width;
	}

	@Override
	public void overlayBackground(int startY, int endY, int startAlpha, int endAlpha) {
	}

}