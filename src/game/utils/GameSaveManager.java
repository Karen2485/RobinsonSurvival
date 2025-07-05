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
     * Сохраняет текущий прогресс игрока и инвентарь
     */
    public static void saveGame(Player player, Inventory inventory, int daysSurvived) {
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
        } catch (IOException e) {
            System.out.println("Error saving game: " + e.getMessage());
        }
    }

    /**
     * Загружает прогресс игры
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
                    String[] parts = line.split("=");
                    if (parts.length < 2) continue;  // Безопасность при ошибках формата
                    switch (parts[0]) {
                        case "health" -> player.setHealth(Integer.parseInt(parts[1]));
                        case "hunger" -> player.setHunger(Integer.parseInt(parts[1]));
                        case "alive" -> player.setAlive(Boolean.parseBoolean(parts[1]));
                        case "daysSurvived" -> daysSurvivedHolder[0] = Integer.parseInt(parts[1]);
                    }
                } else {
                    String[] parts = line.split("=");
                    if (parts.length < 2) continue;
                    Item loadedItem = ItemRegistry.getItemByName(parts[0]);
                    if (loadedItem != null) {
                        inventory.addItem(loadedItem, Integer.parseInt(parts[1]));
                    } else {
                        System.out.println("Unknown item found in save: " + parts[0]);
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