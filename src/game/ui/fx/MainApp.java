package game.ui.fx;

import game.logic.StartingItems;
import game.logic.SurvivalStatus;
import game.model.Inventory;
import game.model.Player;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * MainApp — отдельный JavaFX Application класс для запуска игры
 * минуя интро, напрямую в GUI.
 */
public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Создаём базовые игровые объекты
        Player player = new Player();
        Inventory inventory = new Inventory();
        StartingItems.addInitialItems(inventory);
        SurvivalStatus status = new SurvivalStatus();

        // Создаём основной UI
        MainUI mainUI = new MainUI(player, inventory, status);
        Scene scene = new Scene(mainUI.getRoot(), 1280, 720);

        // Настраиваем сцену
        primaryStage.setTitle("Robinson Survival — GUI");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}