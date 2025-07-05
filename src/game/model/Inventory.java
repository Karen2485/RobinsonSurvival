package game.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Класс Inventory - хранит предметы игрока и их количество.
 * Использует объекты Item в качестве ключей.
 */
public class Inventory {

    private Map<Item, Integer> items;

    public Inventory() {
        items = new HashMap<>();
    }

    /**
     * Добавляет предмет в инвентарь.
     * Игнорирует вызовы с количеством <= 0.
     */
    public void addItem(Item item, int quantity) {
        if (quantity <= 0) return;
        items.put(item, items.getOrDefault(item, 0) + quantity);
    }

    /**
     * Удаляет предмет из инвентаря.
     * Если количество <= 0 — удаляет предмет.
     */
    public void removeItem(Item item, int quantity) {
        if (items.containsKey(item)) {
            int current = items.get(item);
            int updated = current - quantity;
            if (updated > 0) {
                items.put(item, updated);
            } else {
                items.remove(item);
            }
        }
    }

    /**
     * Получает количество указанного предмета.
     */
    public int getItemCount(Item item) {
        return items.getOrDefault(item, 0);
    }

    /**
     * Показывает содержимое инвентаря в консоли.
     */
    public void showInventory() {
        System.out.println("=== Inventory ===");
        if (items.isEmpty()) {
            System.out.println("Inventory is empty.");
        } else {
            for (Map.Entry<Item, Integer> entry : items.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }
        }
    }

    /**
     * Возвращает карту предметов (нужно для сохранения).
     */
    public Map<Item, Integer> getItems() {
        return items;
    }

    /**
     * Полностью заменяет содержимое инвентаря (используется при загрузке).
     * Создает копию карты для безопасности.
     */
    public void setItems(Map<Item, Integer> newItems) {
        this.items = new HashMap<>(newItems);
    }
}