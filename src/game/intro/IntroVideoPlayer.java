package game.intro;

import game.ui.MainMenu;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;

public class IntroVideoPlayer {

    private MediaPlayer mediaPlayer;
    private MediaView mediaView;

    public void play(Stage primaryStage) {
        URL videoUrl = getClass().getClassLoader().getResource("intro/intro.mp4");
        if (videoUrl == null) {
            System.err.println("❌ Intro video not found! Skipping to Main Menu...");
            MainMenu.show(primaryStage);
            return;
        }

        Media media = new Media(videoUrl.toExternalForm());
        mediaPlayer = new MediaPlayer(media);

        mediaView = new MediaView(mediaPlayer);
        mediaView.setPreserveRatio(false); // растягиваем без сохранения пропорций

        StackPane root = new StackPane(mediaView);
        root.setStyle("-fx-background-color: black;");

        // Получаем размеры экрана
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

        // Создаём сцену на весь экран
        Scene scene = new Scene(root, screenBounds.getWidth(), screenBounds.getHeight());

        // Привязываем размеры видео к размерам сцены (экрана)
        mediaView.fitWidthProperty().bind(scene.widthProperty());
        mediaView.fitHeightProperty().bind(scene.heightProperty());

        // Обработка пропуска видео по ESC или SPACE
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE || event.getCode() == KeyCode.SPACE) {
                skipAndFadeOut(primaryStage, root);
            }
        });

        // После окончания видео — плавно затемнить и перейти в меню
        mediaPlayer.setOnEndOfMedia(() -> {
            if (mediaPlayer != null) mediaPlayer.stop();
            if (mediaView != null) mediaView.setVisible(false);
            fadeOutAndStartMenu(primaryStage, root, scene);
        });

        // Устанавливаем сцену, настраиваем полноэкранный режим
        primaryStage.setScene(scene);
        primaryStage.setFullScreen(true);
        primaryStage.setFullScreenExitHint(""); // убрать подсказку выхода из fullscreen
        primaryStage.show();

        mediaPlayer.play();
    }

    private void skipAndFadeOut(Stage stage, StackPane root) {
        if (mediaPlayer != null) mediaPlayer.stop();
        if (mediaView != null) mediaView.setVisible(false);
        fadeOutAndStartMenu(stage, root, root.getScene());
    }

    private void fadeOutAndStartMenu(Stage stage, StackPane root, Scene scene) {
        Rectangle overlay = new Rectangle();
        overlay.setFill(Color.BLACK);
        overlay.setOpacity(0);

        overlay.widthProperty().bind(scene.widthProperty());
        overlay.heightProperty().bind(scene.heightProperty());

        root.getChildren().add(overlay);

        FadeTransition fade = new FadeTransition(Duration.millis(100), overlay);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.setOnFinished(e -> {
            root.getChildren().remove(overlay);
            Platform.runLater(() -> {
                try {
                    stage.setFullScreen(false); // выходим из fullscreen перед меню
                    MainMenu.show(stage);
                    System.out.println("🎮 Welcome to Robinson Survival - Main Menu! 🏝️");
                } catch (Exception ex) {
                    System.err.println("⚠️ Error starting Main Menu.");
                    ex.printStackTrace();
                    Platform.exit();
                }
            });
        });
        fade.play();
    }
}
