package game.intro;

import game.ui.MainMenu;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;

import java.net.URL;

public class IntroVideoPlayer extends Application {

    private MediaPlayer mediaPlayer;
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        // Загружаем видео из ресурсов (папка resources)
        URL videoURL = getClass().getResource("/intro.mp4");
        if (videoURL == null) {
            System.out.println("Video file not found! Skipping intro...");
            startMainMenu();
            return;
        }

        String videoPath = videoURL.toExternalForm();

        Media media = new Media(videoPath);
        mediaPlayer = new MediaPlayer(media);
        MediaView mediaView = new MediaView(mediaPlayer);

        // Настраиваем сцену с видео
        StackPane root = new StackPane(mediaView);
        Scene scene = new Scene(root, 800, 600);

        // Добавляем возможность пропустить видео нажатием клавиши
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.SPACE || event.getCode() == KeyCode.ESCAPE) {
                skipIntro();
            }
        });

        // Фокус на сцену для обработки нажатий клавиш
        root.setFocusTraversable(true);
        Platform.runLater(() -> root.requestFocus());

        primaryStage.setTitle("Robinson Survival - Intro");
        primaryStage.setScene(scene);
        primaryStage.show();

        mediaPlayer.setAutoPlay(true);

        // После окончания видео запускаем главное меню
        mediaPlayer.setOnEndOfMedia(() -> {
            startMainMenu();
        });

        // Обработка ошибок воспроизведения
        mediaPlayer.setOnError(() -> {
            System.out.println("Error playing video. Starting main menu...");
            startMainMenu();
        });
    }

    /**
     * Пропускает интро и сразу запускает главное меню
     */
    private void skipIntro() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
        startMainMenu();
    }

    /**
     * Закрывает окно с видео и запускает графическое главное меню
     */
    private void startMainMenu() {
        // Останавливаем проигрывание видео
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }

        // Запускаем графическое меню в том же JavaFX потоке
        Platform.runLater(() -> {
            try {
                // Создаем новый экземпляр MainUI
                game.ui.fx.MainUI mainUI = new game.ui.fx.MainUI();

                // Переиспользуем тот же Stage или создаем новый
                Stage newStage = new Stage();
                mainUI.start(newStage);

                // Закрываем старое окно с видео
                if (primaryStage != null) {
                    primaryStage.close();
                }

                System.out.println("🎮 Welcome to Robinson Survival GUI! 🏝️");

            } catch (Exception e) {
                System.out.println("Error starting GUI. Falling back to console menu...");
                e.printStackTrace();

                // Fallback на консольное меню если GUI не запустилось
                Platform.exit();
                Thread consoleThread = new Thread(() -> {
                    MainMenu consoleMenu = new MainMenu();
                    consoleMenu.show();
                });
                consoleThread.setDaemon(false);
                consoleThread.start();
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}