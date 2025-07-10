package game.ui.fx;

import game.logic.ItemRegistry;
import game.logic.StartingItems;
import game.logic.IslandEventManager;
import game.model.Inventory;
import game.model.Player;
import game.logic.SurvivalStatus;
import game.utils.GameSaveManager;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class MainUI {

    private Player player;
    private Inventory inventory;
    private SurvivalStatus status;
    private IslandEventManager eventManager;
    private Label statusLabel;
    private VBox root;

    public MainUI(Player player, Inventory inventory, SurvivalStatus status) {
        this.player = player;
        this.inventory = inventory;
        this.status = status;
        this.eventManager = new IslandEventManager();
        this.statusLabel = new Label();
        this.root = createMainLayout();
        updateStatusLabel();
    }

    public VBox getRoot() {
        return root;
    }

    private VBox createMainLayout() {
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: #2c3e50;");

        Label title = new Label("\ud83c\udfdd\ufe0f Robinson Survival");
        title.setStyle("-fx-font-size: 24px; -fx-text-fill: #ecf0f1; -fx-font-weight: bold;");

        statusLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #ecf0f1; -fx-background-color: #34495e; -fx-padding: 10px; -fx-border-radius: 5px;");

        GridPane gameButtons = createGameButtons();
        GridPane systemButtons = createSystemButtons();

        layout.getChildren().addAll(title, statusLabel,
                new Label("\ud83c\udfae Game Actions:") {{ setStyle("-fx-text-fill: #ecf0f1; -fx-font-weight: bold;"); }},
                gameButtons,
                new Label("\ud83d\udcc2 System Actions:") {{ setStyle("-fx-text-fill: #ecf0f1; -fx-font-weight: bold;"); }},
                systemButtons);

        return layout;
    }

    private GridPane createGameButtons() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        Button btnInventory = createStyledButton("\ud83d\udce6 View Inventory", "#3498db");
        Button btnStatus = createStyledButton("\u2764\ufe0f Detailed Status", "#e74c3c");
        Button btnEat = createStyledButton("\ud83c\udf5e Eat Food", "#f39c12");
        Button btnMedkit = createStyledButton("\ud83c\udfe5 Use Medkit", "#2ecc71");
        Button btnExplore = createStyledButton("\ud83d\uddcc\ufe0f Explore Island", "#9b59b6");
        Button btnEndDay = createStyledButton("\ud83c\udf05 End Day", "#34495e");

        btnInventory.setOnAction(e -> showInventory());
        btnStatus.setOnAction(e -> showDetailedStatus());
        btnEat.setOnAction(e -> eatFood());
        btnMedkit.setOnAction(e -> useMedkit());
        btnExplore.setOnAction(e -> exploreIsland());
        btnEndDay.setOnAction(e -> endDay());

        grid.add(btnInventory, 0, 0);
        grid.add(btnStatus, 1, 0);
        grid.add(btnEat, 0, 1);
        grid.add(btnMedkit, 1, 1);
        grid.add(btnExplore, 0, 2);
        grid.add(btnEndDay, 1, 2);

        return grid;
    }

    private GridPane createSystemButtons() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        Button btnSaveGame = createStyledButton("\ud83d\udcc2 Save Game", "#3498db");
        Button btnExit = createStyledButton("\u274c Exit Game", "#e74c3c");

        btnSaveGame.setOnAction(e -> saveGame());
        btnExit.setOnAction(e -> root.getScene().getWindow().hide());

        grid.add(btnSaveGame, 0, 0);
        grid.add(btnExit, 1, 0);

        return grid;
    }

    private Button createStyledButton(String text, String color) {
        Button button = new Button(text);
        button.setStyle(String.format(
                "-fx-background-color: %s; -fx-text-fill: white; -fx-font-size: 12px; " +
                        "-fx-padding: 10px 15px; -fx-border-radius: 5px; -fx-background-radius: 5px; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 2, 0, 0, 1);", color));
        button.setPrefWidth(180);

        button.setOnMouseEntered(e -> button.setStyle(button.getStyle() + "-fx-opacity: 0.8;"));
        button.setOnMouseExited(e -> button.setStyle(button.getStyle().replace("-fx-opacity: 0.8;", "")));

        return button;
    }

    private void updateStatusLabel() {
        String statusText = String.format(
                "\ud83c\udfdd\ufe0f Day %d | \u2764\ufe0f Health: %d/100 | \ud83c\udf7d\ufe0f Hunger: %d/100 | Status: %s",
                status.getDaysSurvived(), player.getHealth(), player.getHunger(),
                player.isAlive() ? "Alive" : "Dead"
        );
        statusLabel.setText(statusText);

        if (!player.isAlive()) {
            statusLabel.setStyle(statusLabel.getStyle() + "-fx-background-color: #e74c3c;");
        } else if (player.getHealth() <= 20 || player.getHunger() <= 20) {
            statusLabel.setStyle(statusLabel.getStyle() + "-fx-background-color: #f39c12;");
        } else {
            statusLabel.setStyle(statusLabel.getStyle() + "-fx-background-color: #34495e;");
        }
    }

    private void showInventory() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("\ud83d\udce6 Inventory");
        alert.setHeaderText("Your Current Items:");

        String inventoryText = inventory.toString();
        if (inventoryText.contains("empty")) {
            inventoryText = "Your inventory is empty.\nYou need to find supplies to survive!";
        }

        alert.setContentText(inventoryText);
        alert.showAndWait();
    }

    private void showDetailedStatus() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("\u2764\ufe0f Player Status");
        alert.setHeaderText("Detailed Survival Status:");

        StringBuilder statusMsg = new StringBuilder();
        statusMsg.append("\ud83c\udfdd\ufe0f Days Survived: ").append(status.getDaysSurvived()).append("\n");
        statusMsg.append("\u2764\ufe0f Health: ").append(player.getHealth()).append("/100\n");
        statusMsg.append("\ud83c\udf7d\ufe0f Hunger: ").append(player.getHunger()).append("/100\n");
        statusMsg.append("\ud83d\udc80 Status: ").append(player.isAlive() ? "Alive" : "Dead").append("\n\n");

        if (player.getHealth() <= 20) statusMsg.append("\u26a0\ufe0f WARNING: Critical health!\n");
        if (player.getHunger() <= 20) statusMsg.append("\u26a0\ufe0f WARNING: Starving!\n");
        if (player.getHealth() > 80 && player.getHunger() > 80) statusMsg.append("\u2705 You're in good condition!\n");

        alert.setContentText(statusMsg.toString());
        alert.showAndWait();
    }

    private void eatFood() {
        if (!player.isAlive()) {
            showMessage("\ud83d\udc80 Cannot Act", "You are dead and cannot eat.");
            return;
        }

        boolean foodConsumed = false;
        String message = "";

        if (inventory.getItemCount(ItemRegistry.BREAD) > 0) {
            inventory.removeItem(ItemRegistry.BREAD, 1);
            int oldHunger = player.getHunger();
            player.setHunger(Math.min(player.getHunger() + 20, 100));
            int restored = player.getHunger() - oldHunger;
            message = "\ud83c\udf5e You ate bread.\nHunger restored by " + restored + " points.";
            foodConsumed = true;
        } else if (inventory.getItemCount(ItemRegistry.BERRIES) > 0) {
            inventory.removeItem(ItemRegistry.BERRIES, 1);
            int oldHunger = player.getHunger();
            player.setHunger(Math.min(player.getHunger() + 15, 100));
            int restored = player.getHunger() - oldHunger;
            message = "\ud83e\udd50 You ate wild berries.\nHunger restored by " + restored + " points.";
            foodConsumed = true;
        } else if (inventory.getItemCount(ItemRegistry.WATER) > 0) {
            inventory.removeItem(ItemRegistry.WATER, 1);
            int oldHunger = player.getHunger();
            player.setHunger(Math.min(player.getHunger() + 10, 100));
            int restored = player.getHunger() - oldHunger;
            message = "\ud83d\udca7 You drank water.\nHunger restored by " + restored + " points.\n(You need solid food!)";
            foodConsumed = true;
        } else {
            message = "\u274c No food available!\nYou need to find food or water to survive.";
        }

        updateStatusLabel();
        showMessage(foodConsumed ? "\ud83c\udf7d\ufe0f Food Consumed" : "\ud83c\udf7d\ufe0f No Food", message);
    }

    private void useMedkit() {
        if (!player.isAlive()) {
            showMessage("\ud83d\udc80 Cannot Act", "You are dead and cannot use medkit.");
            return;
        }

        if (inventory.getItemCount(ItemRegistry.MEDKIT) > 0) {
            inventory.removeItem(ItemRegistry.MEDKIT, 1);
            int oldHealth = player.getHealth();
            player.setHealth(Math.min(player.getHealth() + 30, 100));
            int restored = player.getHealth() - oldHealth;
            updateStatusLabel();
            showMessage("\ud83c\udfe5 Medkit Used", "You used a medkit.\nHealth restored by " + restored + " points.");
        } else {
            showMessage("\u274c No Medkit", "You don't have any medkits in your inventory.");
        }
    }

    private void exploreIsland() {
        if (!player.isAlive()) {
            showMessage("\ud83d\udc80 Cannot Act", "You are dead and cannot explore.");
            return;
        }

        showMessage("\ud83d\uddcc\ufe0f Exploring...", "You venture into the unknown parts of the island...");
        eventManager.explore(player, inventory);
        updateStatusLabel();
        checkGameOver();
    }

    private void endDay() {
        if (!player.isAlive()) {
            showMessage("\ud83d\udc80 Cannot Act", "You are dead. Game over.");
            return;
        }

        status.setDaysSurvived(status.getDaysSurvived() + 1);
        player.setHunger(Math.max(0, player.getHunger() - 15));

        if (player.getHunger() <= 0) {
            player.setHealth(Math.max(0, player.getHealth() - 20));
            showMessage("\ud83c\udf05 Day " + status.getDaysSurvived() + " Ended",
                    "You are starving!\nYou lost 20 health points.");
        } else if (player.getHunger() <= 20) {
            player.setHealth(Math.max(0, player.getHealth() - 10));
            showMessage("\ud83c\udf05 Day " + status.getDaysSurvived() + " Ended",
                    "You are very hungry!\nYou lost 10 health points.");
        } else {
            showMessage("\ud83c\udf05 Day " + status.getDaysSurvived() + " Ended",
                    "Another day survived on the island.\nHealth: " + player.getHealth() +
                            ", Hunger: " + player.getHunger());
        }

        updateStatusLabel();
        checkGameOver();
    }

    private void checkGameOver() {
        if (player.getHealth() <= 0) {
            player.setAlive(false);
            updateStatusLabel();

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("\ud83d\udc80 GAME OVER");
            alert.setHeaderText("You have died on the island!");
            alert.setContentText("Days survived: " + status.getDaysSurvived() + "\n\n" +
                    (player.getHunger() <= 0 ? "Cause: Starvation" : "Cause: Injuries and exhaustion") +
                    "\n\nStart a new game to try again!");
            alert.showAndWait();
        }
    }

    private void showMessage(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Метод для сохранения игры — вызывает GameSaveManager.saveGame с текущим состоянием
    private void saveGame() {
        try {
            boolean success = GameSaveManager.saveGame(player, inventory, status.getDaysSurvived());
            if (success) {
                showMessage("Save Game", "Game saved successfully!");
            } else {
                showMessage("Save Game", "Failed to save the game.");
            }
        } catch (Exception e) {
            showMessage("Save Game", "Error while saving: " + e.getMessage());
        }
    }
}
