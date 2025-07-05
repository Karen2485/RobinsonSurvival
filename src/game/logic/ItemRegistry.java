package game.logic;

import game.model.Item;

/**
 * Реестр всех игровых предметов.
 */
public class ItemRegistry {

    public static final Item BREAD = new Item("Bread", "Simple bread for survival.");
    public static final Item WATER = new Item("Water", "Clean drinking water.");
    public static final Item KNIFE = new Item("Knife", "Sharp tool for cutting.");
    public static final Item ROPE = new Item("Rope", "Strong rope for building.");
    public static final Item EMPTY_BAG = new Item("Empty Bag", "Can be used to carry supplies.");
    public static final Item AXE = new Item("Axe", "Useful for chopping wood.");
    public static final Item MEDKIT = new Item("Medkit", "Restores health when used.");

    public static final Item BERRIES = new Item("Berries", "Wild berries found on the island.");
    public static final Item WOOD = new Item("Wood", "Used for crafting and making fire.");
}