package game.ui;

import game.utils.GameSaveManager;
import game.logic.IslandEventManager;
import game.logic.StartingItems;
import game.logic.ItemRegistry;
import game.model.Inventory;
import game.model.Player;

import java.util.Scanner;

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

        showStartMenu();
    }

    // Меню выбора новой игры или загрузки
    private void showStartMenu() {
        while (true) {
            System.out.println("=== Robinson Survival ===");
            System.out.println("1. Start New Game");
            System.out.println("2. Continue Saved Game");
            System.out.print("Choose option (1 or 2): ");

            String input = scanner.nextLine().trim();
            if ("1".equals(input)) {
                System.out.println("Starting new game...");
                StartingItems.addInitialItems(inventory);
                break;
            } else if ("2".equals(input)) {
                boolean loaded = GameSaveManager.loadGame(player, inventory, new int[]{0});
                if (loaded) {
                    System.out.println("Loaded saved game. Welcome back!");
                    break;
                } else {
                    System.out.println("No saved game found. Starting new game...");
                    StartingItems.addInitialItems(inventory);
                    break;
                }
            } else {
                System.out.println("Invalid input. Please enter 1 or 2.");
            }
        }
    }

    public void show() {
        IslandEventManager eventManager = new IslandEventManager();

        while (player.isAlive()) {
            System.out.println("\n=== Main Menu ===");
            System.out.println("Days survived: " + daysSurvived);
            System.out.println("Choose an action:");
            System.out.println("1. View Inventory");
            System.out.println("2. Eat Food");
            System.out.println("3. Check Player Status");
            System.out.println("4. End Day (Advance Time)");
            System.out.println("5. Explore the island");
            System.out.println("6. Exit Game");
            System.out.print("Enter your choice: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> inventory.showInventory();
                case "2" -> eatFood();
                case "3" -> playerStatus();
                case "4" -> endDay();
                case "5" -> eventManager.explore(player, inventory);
                case "6" -> {
                    GameSaveManager.saveGame(player, inventory, daysSurvived);
                    System.out.println("Game saved. Exiting game...");
                    return;
                }
                default -> System.out.println("Invalid choice. Please enter a number between 1 and 6.");
            }

            if (!player.isAlive()) {
                System.out.println("\nGame over. You survived " + daysSurvived + " days.");
                break;
            }
        }
    }

    private void eatFood() {
        // Расширяем логику еды — сначала хлеб, потом консервы, потом вода, потом ягоды
        if (tryConsumeItem(ItemRegistry.BREAD, 20, "bread")) return;
        if (tryConsumeItem(ItemRegistry.MEDKIT, 0, "medkit")) return; // аптечка лечит, но не естся
        if (tryConsumeItem(ItemRegistry.WATER, 10, "water")) return;
        if (tryConsumeItem(ItemRegistry.BERRIES, 15, "berries")) return;

        System.out.println("You have no food or water to consume.");
    }

    // Вспомогательный метод для поедания/использования предмета
    private boolean tryConsumeItem(game.model.Item item, int hungerRestore, String displayName) {
        if (inventory.getItemCount(item) > 0) {
            inventory.removeItem(item, 1);
            if (hungerRestore > 0) {
                player.setHunger(Math.min(player.getHunger() + hungerRestore, 100));
                System.out.println("You consumed some " + displayName + ". Hunger restored by " + hungerRestore + ".");
            } else if (displayName.equals("medkit")) {
                player.setHealth(Math.min(player.getHealth() + 30, 100));
                System.out.println("You used a medkit. Health restored by 30.");
            }
            return true;
        }
        return false;
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

        if (player.getHealth() <= 0) {
            player.setAlive(false);
            System.out.println("You have died of starvation.");
        }

        System.out.println("Day ended. Days survived: " + daysSurvived);
    }
}