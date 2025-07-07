package game.ui;

import game.utils.GameSaveManager;
import game.logic.IslandEventManager;
import game.logic.StartingItems;
import game.logic.ItemRegistry;
import game.logic.SurvivalStatus;
import game.model.Inventory;
import game.model.Player;

import java.util.Scanner;

public class MainMenu {

    private Player player;
    private Inventory inventory;
    private int daysSurvived;
    private Scanner scanner;
    private SurvivalStatus survivalStatus;

    public MainMenu() {
        this.player = new Player();
        this.inventory = new Inventory();
        this.daysSurvived = 0;
        this.scanner = new Scanner(System.in);
        this.survivalStatus = new SurvivalStatus();

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
                int[] daysHolder = {0};
                boolean loaded = GameSaveManager.loadGame(player, inventory, daysHolder);
                if (loaded) {
                    this.daysSurvived = daysHolder[0];
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
            System.out.println("6. Use Medkit");
            System.out.println("7. Save & Exit Game");
            System.out.print("Enter your choice: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> inventory.showInventory();
                case "2" -> eatFood();
                case "3" -> playerStatus();
                case "4" -> endDay();
                case "5" -> {
                    eventManager.explore(player, inventory);
                    checkPlayerDeath();
                }
                case "6" -> useMedkit();
                case "7" -> {
                    GameSaveManager.saveGame(player, inventory, daysSurvived);
                    System.out.println("Game saved. Exiting game...");
                    return;
                }
                default -> System.out.println("Invalid choice. Please enter a number between 1 and 7.");
            }

            checkPlayerDeath();
        }
    }

    private void eatFood() {
        boolean foodFound = false;

        // Приоритет еды: хлеб -> ягоды -> вода (в крайнем случае)
        if (tryConsumeFood(ItemRegistry.BREAD, 20, "bread")) {
            foodFound = true;
        } else if (tryConsumeFood(ItemRegistry.BERRIES, 15, "berries")) {
            foodFound = true;
        } else if (tryConsumeFood(ItemRegistry.WATER, 10, "water")) {
            foodFound = true;
            System.out.println("You're drinking water to survive, but you need solid food!");
        } else {
            System.out.println("You have no food or water to consume.");
        }

        // Уведомляем систему выживания о том, нашли ли мы еду
        if (foodFound) {
            System.out.println("You feel less hungry.");
        }
    }

    private boolean tryConsumeFood(game.model.Item item, int hungerRestore, String displayName) {
        if (inventory.getItemCount(item) > 0) {
            inventory.removeItem(item, 1);
            // Ограничиваем голод максимумом в 100
            player.setHunger(Math.min(player.getHunger() + hungerRestore, 100));
            System.out.println("You consumed " + displayName + ". Hunger restored by " + hungerRestore + ".");
            return true;
        }
        return false;
    }

    private void useMedkit() {
        if (inventory.getItemCount(ItemRegistry.MEDKIT) > 0) {
            inventory.removeItem(ItemRegistry.MEDKIT, 1);
            int healthBefore = player.getHealth();
            // Ограничиваем здоровье максимумом в 100
            player.setHealth(Math.min(player.getHealth() + 30, 100));
            int healthRestored = player.getHealth() - healthBefore;
            System.out.println("You used a medkit. Health restored by " + healthRestored + ".");
        } else {
            System.out.println("You don't have any medkits.");
        }
    }

    private void playerStatus() {
        System.out.println("\n=== Player Status ===");
        System.out.println("Health: " + player.getHealth() + "/100");
        System.out.println("Hunger: " + player.getHunger() + "/100");
        System.out.println("Alive: " + (player.isAlive() ? "Yes" : "No"));
        System.out.println("Days survived: " + daysSurvived);

        // Показываем предупреждения
        if (player.getHealth() <= 20) {
            System.out.println("⚠️ WARNING: Your health is critically low!");
        }
        if (player.getHunger() <= 20) {
            System.out.println("⚠️ WARNING: You are very hungry!");
        }
    }

    private void endDay() {
        daysSurvived++;

        // Уменьшаем голод каждый день
        player.setHunger(Math.max(0, player.getHunger() - 15));

        // Проверяем голод и влияние на здоровье
        if (player.getHunger() <= 0) {
            player.setHealth(Math.max(0, player.getHealth() - 20));
            System.out.println("💀 You are starving! You lose 20 health.");
        } else if (player.getHunger() <= 20) {
            player.setHealth(Math.max(0, player.getHealth() - 10));
            System.out.println("😰 You are very hungry! You lose 10 health.");
        }

        // Проверяем выживание
        checkPlayerDeath();

        if (player.isAlive()) {
            System.out.println("🌅 Day " + daysSurvived + " completed.");
            System.out.println("Current status - Health: " + player.getHealth() + ", Hunger: " + player.getHunger());
        }
    }

    private void checkPlayerDeath() {
        if (player.getHealth() <= 0) {
            player.setAlive(false);
            System.out.println("\n💀 GAME OVER 💀");
            System.out.println("You have died after surviving " + daysSurvived + " days on the island.");

            if (player.getHunger() <= 0) {
                System.out.println("Cause of death: Starvation");
            } else {
                System.out.println("Cause of death: Injuries and exhaustion");
            }

            System.out.println("Thank you for playing Robinson Survival!");
        }
    }
}