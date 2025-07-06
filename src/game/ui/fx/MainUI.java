package game.ui.fx;

import game.logic.ItemRegistry;
import game.logic.StartingItems;
import game.model.Inventory;
import game.model.Player;
import game.utils.GameSaveManager;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainUI extends Application {

    private Player player;
    private Inventory inventory;
    private int daysSurvived;
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.player = new Player();
        this.inventory = new Inventory();
        this.daysSurvived = 0;

        primaryStage.setTitle("Robinson Survival");

        // Кнопки действий
        Button btnNewGame = new Button("🆕 New Game");
        Button btnLoadGame = new Button("🔄 Load Game");
        Button btnSaveGame = new Button("💾 Save Game");
        Button btnInventory = new Button("📦 View Inventory");
        Button btnStatus = new Button("❤️ Player Status");
        Button btnEat = new Button("🍞 Eat Food");
        Button btnExit = new Button("❌ Exit Game");

        // Обработчики
        btnNewGame.setOnAction(e -> startNewGame());
        btnLoadGame.setOnAction(e -> loadGame());
        btnSaveGame.setOnAction(e -> saveGame());
        btnInventory.setOnAction(e -> showInventory());
        btnStatus.setOnAction(e -> showStatus());
        btnEat.setOnAction(e -> eatFood());
        btnExit.setOnAction(e -> primaryStage.close());

        // Разметка
        VBox layout = new VBox(10,
                btnNewGame,
                btnLoadGame,
                btnSaveGame,
                btnInventory,
                btnStatus,
                btnEat,
                btnExit
        );
        layout.setStyle("-fx-padding: 20;");
        Scene scene = new Scene(layout, 360, 400);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void startNewGame() {
        this.player = new Player();
        this.inventory = new Inventory();
        this.daysSurvived = 0;
        StartingItems.addInitialItems(inventory);
        showMessage("New game started.");
    }

    private void loadGame() {
        this.player = new Player();
        this.inventory = new Inventory();
        int[] daysHolder = new int[1];

        boolean loaded = GameSaveManager.loadGame(player, inventory, daysHolder);
        if (loaded) {
            this.daysSurvived = daysHolder[0];
            showMessage("Game loaded successfully!");
        } else {
            showMessage("No saved game found.");
        }
    }

    private void saveGame() {
        GameSaveManager.saveGame(player, inventory, daysSurvived);
        showMessage("Game saved.");
    }

    private void showInventory() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Inventory");
        alert.setHeaderText("Your Items:");
        alert.setContentText(inventory.toString());
        alert.showAndWait();
    }

    private void showStatus() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Player Status");
        alert.setHeaderText("Current Status:");
        alert.setContentText(
                "Health: " + player.getHealth() + "\n" +
                        "Hunger: " + player.getHunger() + "\n" +
                        "Alive: " + player.isAlive() + "\n" +
                        "Days Survived: " + daysSurvived
        );
        alert.showAndWait();
    }

    private void eatFood() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        if (inventory.getItemCount(ItemRegistry.BREAD) > 0) {
            inventory.removeItem(ItemRegistry.BREAD, 1);
            player.setHunger(player.getHunger() + 20);
            alert.setContentText("You ate bread. Hunger +20");
        } else if (inventory.getItemCount(ItemRegistry.WATER) > 0) {
            inventory.removeItem(ItemRegistry.WATER, 1);
            player.setHunger(player.getHunger() + 10);
            alert.setContentText("You drank water. Hunger +10");
        } else {
            alert.setContentText("You have no food or water.");
        }
        alert.showAndWait();
    }

    private void showMessage(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Message");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}