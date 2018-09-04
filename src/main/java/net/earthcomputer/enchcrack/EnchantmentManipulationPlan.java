package net.earthcomputer.enchcrack;

public class EnchantmentManipulationPlan {

	private final Type type;
	private final int itemsToThrow;
	private final int bookshelves;
	private final int slot;

	public EnchantmentManipulationPlan(Type type, int itemsToThrow, int bookshelves, int slot) {
		this.type = type;
		this.itemsToThrow = itemsToThrow;
		this.bookshelves = bookshelves;
		this.slot = slot;
	}

	public Type getType() {
		return type;
	}

	public int getItemsToThrow() {
		return itemsToThrow;
	}

	public int getBookshelves() {
		return bookshelves;
	}

	public int getSlot() {
		return slot;
	}

	public static enum Type {
		THROW_ITEMS, THROW_NO_ITEMS, NO_DUMMY
	}

}
