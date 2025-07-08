package game.main;

import game.intro.IntroVideoPlayer;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Главная точка входа в игру Robinson Survival.
 * Запускает JavaFX-приложение и показывает интро-видео,
 * после чего открывается главное меню.
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Запускаем интро-видео
        new IntroVideoPlayer().play(primaryStage);
    }

    public static void main(String[] args) {
        launch(args); // Запуск JavaFX
    }
}