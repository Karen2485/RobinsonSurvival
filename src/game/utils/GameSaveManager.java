package game.utils;

import game.logic.ItemRegistry;
import game.model.Inventory;
import game.model.Item;
import game.model.Player;

import java.io.*;
import java.util.Map;

/**
 * Отвечает за сохранение и загрузку прогресса игры.
 */
public class GameSaveManager {

    private static final String SAVE_FILE = "game_save.txt";

    /**
     * Сохраняет текущий прогресс игрока и инвентарь.
     * Возвращает true при успешном сохранении, иначе false.
     */
    public static boolean saveGame(Player player, Inventory inventory, int daysSurvived) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(SAVE_FILE))) {
            writer.println("health=" + player.getHealth());
            writer.println("hunger=" + player.getHunger());
            writer.println("alive=" + player.isAlive());
            writer.println("daysSurvived=" + daysSurvived);

            writer.println("inventory:");
            for (Map.Entry<Item, Integer> entry : inventory.getItems().entrySet()) {
                writer.println(entry.getKey().getName() + "=" + entry.getValue());
            }

            System.out.println("Game saved successfully!");
            return true;
        } catch (IOException e) {
            System.out.println("Error saving game: " + e.getMessage());
            return false;
        }
    }

    /**
     * Загружает прогресс игры с валидацией данных.
     */
    public static boolean loadGame(Player player, Inventory inventory, int[] daysSurvivedHolder) {
        File file = new File(SAVE_FILE);
        if (!file.exists()) {
            System.out.println("No saved game found.");
            return false;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(SAVE_FILE))) {
            String line;
            boolean inInventory = false;

            while ((line = reader.readLine()) != null) {
                if (line.equals("inventory:")) {
                    inInventory = true;
                    continue;
                }

                if (!inInventory) {
                    String[] parts = line.split("=", 2);
                    if (parts.length < 2) continue;

                    try {
                        switch (parts[0]) {
                            case "health" -> {
                                int health = Integer.parseInt(parts[1]);
                                player.setHealth(Math.max(0, Math.min(100, health)));
                            }
                            case "hunger" -> {
                                int hunger = Integer.parseInt(parts[1]);
                                player.setHunger(Math.max(0, Math.min(100, hunger)));
                            }
                            case "alive" -> player.setAlive(Boolean.parseBoolean(parts[1]));
                            case "daysSurvived" -> {
                                int days = Integer.parseInt(parts[1]);
                                daysSurvivedHolder[0] = Math.max(0, days);
                            }
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid number format for: " + parts[0] + "=" + parts[1]);
                    }
                } else {
                    String[] parts = line.split("=", 2);
                    if (parts.length < 2) continue;

                    try {
                        Item loadedItem = ItemRegistry.getItemByName(parts[0]);
                        if (loadedItem != null) {
                            int quantity = Integer.parseInt(parts[1]);
                            if (quantity > 0) {
                                inventory.addItem(loadedItem, quantity);
                            }
                        } else {
                            System.out.println("Unknown item found in save: " + parts[0]);
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid quantity for item: " + parts[0]);
                    }
                }
            }

            System.out.println("Game loaded successfully!");
            return true;
        } catch (IOException e) {
            System.out.println("Error loading game: " + e.getMessage());
            return false;
        }
    }
}
