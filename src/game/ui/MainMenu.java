package game.ui;

import game.utils.GameSaveManager;
import game.logic.IslandEventManager;
import game.logic.StartingItems;
import game.model.Inventory;
import game.model.Player;

import java.util.Scanner;

import static game.logic.ItemRegistry.*;

public class MainMenu {

    private Player player;
    private Inventory inventory;
    private int daysSurvived;
    private Scanner scanner;

    public MainMenu() {
        this.player = new Player();
        this.inventory = new Inventory();
        this.daysSurvived = 0;
        this.scanner = new Scanner(System.in);

        // Добавляем стартовые предметы
        StartingItems.addInitialItems(inventory);
    }

  /*  public MainMenu() {
        this.player = new Player();
        this.inventory = new Inventory();
        this.daysSurvived = 0;
        this.scanner = new Scanner(System.in);

        // Попробуем загрузить сохранение
        boolean loaded = GameSaveManager.loadGame(player, inventory, new int[]{0});
        if (!loaded) {
            System.out.println("Создаём новую игру...");
            StartingItems.addInitialItems(inventory);
        }
    }*/

    public void show() {
        IslandEventManager eventManager = new IslandEventManager();

        System.out.println("=== Robinson Survival - Main Menu ===");

        while (player.isAlive()) {
            System.out.println("\nChoose an action:");
            System.out.println("1. View Inventory");
            System.out.println("2. Eat Food");
            System.out.println("3. Check Player Status");
            System.out.println("4. End Day (Advance Time)");
            System.out.println("5. Explore the island");
            System.out.println("6. Exit Game");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> inventory.showInventory();
                case "2" -> eatFood();
                case "3" -> playerStatus();
                case "4" -> endDay();
                case "5" -> eventManager.explore(player, inventory);
                case "6" -> {
                    GameSaveManager.saveGame(player, inventory, daysSurvived);
                    System.out.println("Exiting game...");
                    return;
                }
                default -> System.out.println("Invalid choice. Try again.");
            }
        }

        System.out.println("Game over. You survived " + daysSurvived + " days.");
    }

    private void eatFood() {
        if (inventory.getItemCount(BREAD) > 0) {
            inventory.removeItem(BREAD, 1);
            player.setHunger(player.getHunger() + 20);
            System.out.println("You ate some bread.");
        } else if (inventory.getItemCount(WATER) > 0) {
            inventory.removeItem(WATER, 1);
            player.setHunger(player.getHunger() + 10);
            System.out.println("You drank some water.");
        } else {
            System.out.println("You have no food or water to consume.");
        }
    }

    private void playerStatus() {
        System.out.println("Health: " + player.getHealth());
        System.out.println("Hunger: " + player.getHunger());
        System.out.println("Alive: " + player.isAlive());
    }

    private void endDay() {
        daysSurvived++;
        player.setHunger(player.getHunger() - 15);

        if (player.getHunger() <= 0) {
            player.setHealth(player.getHealth() - 10);
            System.out.println("You are starving!");
        }

        System.out.println("Day ended. Days survived: " + daysSurvived);
    }
}