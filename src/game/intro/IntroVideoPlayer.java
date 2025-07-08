package game.intro;

import game.ui.MainMenu;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;

import java.net.URL;

/**
 * Класс IntroVideoPlayer отвечает за показ вступительного видеоролика
 * (шторм, кораблекрушение, выброс на берег и сундук с предметами).
 * После окончания видео — отображается главное меню.
 */
public class IntroVideoPlayer {

    private MediaPlayer mediaPlayer;
    private Stage primaryStage;

    /**
     * Запускает вступительное видео
     */
    public void play(Stage primaryStage) {
        this.primaryStage = primaryStage;

        URL videoUrl = getClass().getClassLoader().getResource("intro.mp4");
        if (videoUrl == null) {
            System.err.println("❌ Intro video not found! Skipping to Main Menu...");
            MainMenu.show(primaryStage);
            return;
        }

        Media media = new Media(videoUrl.toExternalForm());
        mediaPlayer = new MediaPlayer(media);
        MediaView mediaView = new MediaView(mediaPlayer);

        StackPane root = new StackPane(mediaView);
        Scene scene = new Scene(root, 1280, 720);

        // Пропустить видео по нажатию ESC или ПРОБЕЛ
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE || event.getCode() == KeyCode.SPACE) {
                startMainMenu();
            }
        });

        mediaPlayer.setOnEndOfMedia(this::startMainMenu);

        primaryStage.setScene(scene);
        primaryStage.setFullScreen(true);
        primaryStage.show();
        mediaPlayer.play();
    }

    /**
     * Завершает видео и запускает главное меню
     */
    private void startMainMenu() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }

        Platform.runLater(() -> {
            try {
                primaryStage.setFullScreen(false);
                MainMenu.show(primaryStage);
                System.out.println("🎮 Welcome to Robinson Survival - Main Menu! 🏝️");

            } catch (Exception e) {
                System.out.println("⚠️ Error starting Main Menu.");
                e.printStackTrace();
                Platform.exit();

                // Если хочешь fallback на консоль — раскомментируй ↓
                /*
                Thread fallback = new Thread(() -> {
                    game.ui.MainMenu legacy = new game.ui.MainMenu();
                    legacy.showConsole(); // ТОЛЬКО если ты реализуешь этот метод
                });
                fallback.setDaemon(false);
                fallback.start();
                */
            }
        });
    }
}