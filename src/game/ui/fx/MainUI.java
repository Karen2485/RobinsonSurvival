package game.ui.fx;

import game.logic.ItemRegistry;
import game.logic.StartingItems;
import game.logic.IslandEventManager;
import game.model.Inventory;
import game.model.Player;
import game.utils.GameSaveManager;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainUI extends Application {

    private Player player;
    private Inventory inventory;
    private int daysSurvived;
    private Stage primaryStage;
    private IslandEventManager eventManager;
    private Label statusLabel;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.player = new Player();
        this.inventory = new Inventory();
        this.daysSurvived = 0;
        this.eventManager = new IslandEventManager();

        primaryStage.setTitle("Robinson Survival - GUI Edition");
        primaryStage.setResizable(false);

        // Создаем интерфейс
        VBox mainLayout = createMainLayout();
        Scene scene = new Scene(mainLayout, 500, 600);

        // Применяем стили
        scene.getStylesheets().add("data:text/css," + getCSS());

        primaryStage.setScene(scene);
        primaryStage.show();

        updateStatusLabel();
    }

    private VBox createMainLayout() {
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: #2c3e50;");

        // Заголовок
        Label title = new Label("🏝️ Robinson Survival");
        title.setStyle("-fx-font-size: 24px; -fx-text-fill: #ecf0f1; -fx-font-weight: bold;");

        // Статус игрока
        statusLabel = new Label();
        statusLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #ecf0f1; -fx-background-color: #34495e; -fx-padding: 10px; -fx-border-radius: 5px;");

        // Кнопки управления игрой
        GridPane gameButtons = createGameButtons();

        // Кнопки системы
        GridPane systemButtons = createSystemButtons();

        layout.getChildren().addAll(title, statusLabel,
                new Label("🎮 Game Actions:") {{ setStyle("-fx-text-fill: #ecf0f1; -fx-font-weight: bold;"); }},
                gameButtons,
                new Label("💾 System Actions:") {{ setStyle("-fx-text-fill: #ecf0f1; -fx-font-weight: bold;"); }},
                systemButtons);

        return layout;
    }

    private GridPane createGameButtons() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        Button btnInventory = createStyledButton("📦 View Inventory", "#3498db");
        Button btnStatus = createStyledButton("❤️ Detailed Status", "#e74c3c");
        Button btnEat = createStyledButton("🍞 Eat Food", "#f39c12");
        Button btnMedkit = createStyledButton("🏥 Use Medkit", "#2ecc71");
        Button btnExplore = createStyledButton("🗺️ Explore Island", "#9b59b6");
        Button btnEndDay = createStyledButton("🌅 End Day", "#34495e");

        // Обработчики событий
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

        Button btnNewGame = createStyledButton("🆕 New Game", "#27ae60");
        Button btnLoadGame = createStyledButton("🔄 Load Game", "#f39c12");
        Button btnSaveGame = createStyledButton("💾 Save Game", "#3498db");
        Button btnExit = createStyledButton("❌ Exit Game", "#e74c3c");

        btnNewGame.setOnAction(e -> startNewGame());
        btnLoadGame.setOnAction(e -> loadGame());
        btnSaveGame.setOnAction(e -> saveGame());
        btnExit.setOnAction(e -> primaryStage.close());

        grid.add(btnNewGame, 0, 0);
        grid.add(btnLoadGame, 1, 0);
        grid.add(btnSaveGame, 0, 1);
        grid.add(btnExit, 1, 1);

        return grid;
    }

    private Button createStyledButton(String text, String color) {
        Button button = new Button(text);
        button.setStyle(String.format(
                "-fx-background-color: %s; -fx-text-fill: white; -fx-font-size: 12px; " +
                        "-fx-padding: 10px 15px; -fx-border-radius: 5px; -fx-background-radius: 5px; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 2, 0, 0, 1);", color));
        button.setPrefWidth(180);

        // Эффект при наведении
        button.setOnMouseEntered(e -> button.setStyle(button.getStyle() + "-fx-opacity: 0.8;"));
        button.setOnMouseExited(e -> button.setStyle(button.getStyle().replace("-fx-opacity: 0.8;", "")));

        return button;
    }

    private void updateStatusLabel() {
        String status = String.format(
                "🏝️ Day %d | ❤️ Health: %d/100 | 🍽️ Hunger: %d/100 | Status: %s",
                daysSurvived, player.getHealth(), player.getHunger(),
                player.isAlive() ? "Alive" : "Dead"
        );
        statusLabel.setText(status);

        // Меняем цвет в зависимости от состояния
        if (!player.isAlive()) {
            statusLabel.setStyle(statusLabel.getStyle() + "-fx-background-color: #e74c3c;");
        } else if (player.getHealth() <= 20 || player.getHunger() <= 20) {
            statusLabel.setStyle(statusLabel.getStyle() + "-fx-background-color: #f39c12;");
        } else {
            statusLabel.setStyle(statusLabel.getStyle() + "-fx-background-color: #34495e;");
        }
    }

    private void startNewGame() {
        this.player = new Player();
        this.inventory = new Inventory();
        this.daysSurvived = 0;
        StartingItems.addInitialItems(inventory);
        updateStatusLabel();
        showMessage("🎮 New Game Started!", "Welcome to Robinson Survival!\nYou've been stranded on a mysterious island.\nSurvive as long as you can!");
    }

    private void loadGame() {
        Player tempPlayer = new Player();
        Inventory tempInventory = new Inventory();
        int[] daysHolder = new int[1];

        boolean loaded = GameSaveManager.loadGame(tempPlayer, tempInventory, daysHolder);
        if (loaded) {
            this.player = tempPlayer;
            this.inventory = tempInventory;
            this.daysSurvived = daysHolder[0];
            updateStatusLabel();
            showMessage("✅ Game Loaded!", "Welcome back, survivor!\nYou've been on the island for " + daysSurvived + " days.");
        } else {
            showMessage("❌ Load Failed", "No saved game found or file is corrupted.");
        }
    }

    private void saveGame() {
        GameSaveManager.saveGame(player, inventory, daysSurvived);
        showMessage("💾 Game Saved!", "Your progress has been saved successfully.");
    }

    private void showInventory() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("📦 Inventory");
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
        alert.setTitle("❤️ Player Status");
        alert.setHeaderText("Detailed Survival Status:");

        StringBuilder status = new StringBuilder();
        status.append("🏝️ Days Survived: ").append(daysSurvived).append("\n");
        status.append("❤️ Health: ").append(player.getHealth()).append("/100\n");
        status.append("🍽️ Hunger: ").append(player.getHunger()).append("/100\n");
        status.append("💀 Status: ").append(player.isAlive() ? "Alive" : "Dead").append("\n\n");

        // Предупреждения
        if (player.getHealth() <= 20) {
            status.append("⚠️ WARNING: Critical health!\n");
        }
        if (player.getHunger() <= 20) {
            status.append("⚠️ WARNING: Starving!\n");
        }
        if (player.getHealth() > 80 && player.getHunger() > 80) {
            status.append("✅ You're in good condition!\n");
        }

        alert.setContentText(status.toString());
        alert.showAndWait();
    }

    private void eatFood() {
        if (!player.isAlive()) {
            showMessage("💀 Cannot Act", "You are dead and cannot eat.");
            return;
        }

        boolean foodConsumed = false;
        String message = "";

        if (inventory.getItemCount(ItemRegistry.BREAD) > 0) {
            inventory.removeItem(ItemRegistry.BREAD, 1);
            int oldHunger = player.getHunger();
            player.setHunger(Math.min(player.getHunger() + 20, 100));
            int restored = player.getHunger() - oldHunger;
            message = "🍞 You ate bread.\nHunger restored by " + restored + " points.";
            foodConsumed = true;
        } else if (inventory.getItemCount(ItemRegistry.BERRIES) > 0) {
            inventory.removeItem(ItemRegistry.BERRIES, 1);
            int oldHunger = player.getHunger();
            player.setHunger(Math.min(player.getHunger() + 15, 100));
            int restored = player.getHunger() - oldHunger;
            message = "🫐 You ate wild berries.\nHunger restored by " + restored + " points.";
            foodConsumed = true;
        } else if (inventory.getItemCount(ItemRegistry.WATER) > 0) {
            inventory.removeItem(ItemRegistry.WATER, 1);
            int oldHunger = player.getHunger();
            player.setHunger(Math.min(player.getHunger() + 10, 100));
            int restored = player.getHunger() - oldHunger;
            message = "💧 You drank water.\nHunger restored by " + restored + " points.\n(You need solid food!)";
            foodConsumed = true;
        } else {
            message = "❌ No food available!\nYou need to find food or water to survive.";
        }

        updateStatusLabel();
        showMessage(foodConsumed ? "🍽️ Food Consumed" : "🍽️ No Food", message);
    }

    private void useMedkit() {
        if (!player.isAlive()) {
            showMessage("💀 Cannot Act", "You are dead and cannot use medkit.");
            return;
        }

        if (inventory.getItemCount(ItemRegistry.MEDKIT) > 0) {
            inventory.removeItem(ItemRegistry.MEDKIT, 1);
            int oldHealth = player.getHealth();
            player.setHealth(Math.min(player.getHealth() + 30, 100));
            int restored = player.getHealth() - oldHealth;
            updateStatusLabel();
            showMessage("🏥 Medkit Used", "You used a medkit.\nHealth restored by " + restored + " points.");
        } else {
            showMessage("❌ No Medkit", "You don't have any medkits in your inventory.");
        }
    }

    private void exploreIsland() {
        if (!player.isAlive()) {
            showMessage("💀 Cannot Act", "You are dead and cannot explore.");
            return;
        }

        showMessage("🗺️ Exploring...", "You venture into the unknown parts of the island...");

        // Используем существующий event manager
        eventManager.explore(player, inventory);

        updateStatusLabel();
        checkGameOver();
    }

    private void endDay() {
        if (!player.isAlive()) {
            showMessage("💀 Cannot Act", "You are dead. Game over.");
            return;
        }

        daysSurvived++;

        // Уменьшаем голод
        player.setHunger(Math.max(0, player.getHunger() - 15));

        // Влияние голода на здоровье
        if (player.getHunger() <= 0) {
            player.setHealth(Math.max(0, player.getHealth() - 20));
            showMessage("🌅 Day " + daysSurvived + " Ended",
                    "You are starving!\nYou lost 20 health points.");
        } else if (player.getHunger() <= 20) {
            player.setHealth(Math.max(0, player.getHealth() - 10));
            showMessage("🌅 Day " + daysSurvived + " Ended",
                    "You are very hungry!\nYou lost 10 health points.");
        } else {
            showMessage("🌅 Day " + daysSurvived + " Ended",
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
            alert.setTitle("💀 GAME OVER");
            alert.setHeaderText("You have died on the island!");
            alert.setContentText("Days survived: " + daysSurvived + "\n\n" +
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

    private String getCSS() {
        return """
            .alert .header-panel {
                -fx-background-color: #2c3e50;
            }
            .alert .header-panel .label {
                -fx-text-fill: white;
            }
            .alert .content.label {
                -fx-text-fill: #2c3e50;
            }
            """;
    }

    public static void main(String[] args) {
        launch(args);
    }
}