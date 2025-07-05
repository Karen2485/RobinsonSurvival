package game.logic;

import game.model.Item;

import java.util.HashMap;
import java.util.Map;

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

    // Вспомогательная карта для быстрого поиска предметов по имени
    private static final Map<String, Item> itemMap = new HashMap<>();

    static {
        // Заполняем карту
        itemMap.put(BREAD.getName(), BREAD);
        itemMap.put(WATER.getName(), WATER);
        itemMap.put(KNIFE.getName(), KNIFE);
        itemMap.put(ROPE.getName(), ROPE);
        itemMap.put(EMPTY_BAG.getName(), EMPTY_BAG);
        itemMap.put(AXE.getName(), AXE);
        itemMap.put(MEDKIT.getName(), MEDKIT);
        itemMap.put(BERRIES.getName(), BERRIES);
        itemMap.put(WOOD.getName(), WOOD);
    }

    /**
     * Возвращает предмет по имени или null, если не найден.
     */
    public static Item getItemByName(String name) {
        return itemMap.get(name);
    }
}