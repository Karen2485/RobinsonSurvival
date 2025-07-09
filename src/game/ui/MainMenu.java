package game.ui;

import javafx.animation.ScaleTransition;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.InputStream;
import java.net.URL;

public class MainMenu {

    private static MediaPlayer backgroundMusicPlayer;
    private static AudioClip buttonClickSound;

    public static void show(Stage stage) {
        // ---------- НАСТРОЙКА КОНТЕЙНЕРОВ ----------
        HBox buttonContainer = new HBox(30);
        buttonContainer.setStyle("-fx-alignment: center;");
        buttonContainer.setMaxWidth(700);

        VBox root = new VBox(40);
        root.setStyle("-fx-alignment: center;");
        root.setMaxWidth(800);

        // ---------- ЗАГРУЗКА ИКОНКИ-КНОПОК ----------
        ImageView[] newIcon = new ImageView[1];
        Button[] newButton = new Button[1];
        ImageView[] loadIcon = new ImageView[1];
        Button[] loadButton = new Button[1];
        ImageView[] exitIcon = new ImageView[1];
        Button[] exitButton = new Button[1];

        buttonContainer.getChildren().addAll(
                createIconButton("ui/new_game.png", newIcon, newButton),
                createIconButton("ui/load_game.png", loadIcon, loadButton),
                createIconButton("ui/exit_game.png", exitIcon, exitButton)
        );

        // ---------- ЛОГОТИП ----------
        ImageView logoView = loadLogo("ui/logo.png");
        StackPane logoContainer = new StackPane(logoView);
        logoContainer.setMaxWidth(buttonContainer.getMaxWidth());

        // ---------- СЦЕНА И ФОН ----------
        BackgroundImage backgroundImage = loadBackgroundImage("ui/background.jpg");
        Background background = backgroundImage != null
                ? new Background(backgroundImage)
                : new Background(new BackgroundFill(Color.web("#1e1e1e"), null, null));

        root.setBackground(background);
        root.getChildren().addAll(logoContainer, buttonContainer);

        Scene scene = new Scene(root, 800, 700);
        stage.setScene(scene);
        stage.setTitle("Robinson Survival - Main Menu");
        stage.show();

        // ---------- СВЯЗЬ ШИРИНЫ И ВЫСОТЫ ----------
        Runnable updateSizes = () -> {
            double buttonsWidth = buttonContainer.getWidth();
            if (buttonsWidth == 0) buttonsWidth = buttonContainer.getMaxWidth();

            double iconSize = Math.min((buttonsWidth - 60) / 3, 200);  // ограничиваем ширину иконки
            if (newIcon[0] != null) newIcon[0].setFitWidth(iconSize);
            if (loadIcon[0] != null) loadIcon[0].setFitWidth(iconSize);
            if (exitIcon[0] != null) exitIcon[0].setFitWidth(iconSize);

            // фиксируем высоту логотипа равной высоте иконок
            logoView.setFitWidth(buttonsWidth);
            logoView.setFitHeight(iconSize + 100);
        };

        scene.widthProperty().addListener((obs, oldV, newV) -> updateSizes.run());
        scene.heightProperty().addListener((obs, oldV, newV) -> updateSizes.run());
        root.layout();
        updateSizes.run();

        // ---------- ЗВУК ----------
        loadButtonClickSound("audio/button_click.wav");
        playBackgroundMusic("audio/background_music.mp3");

        // ---------- ОБРАБОТКА НАЖАТИЙ ----------
        newButton[0].setOnAction(e -> {
            playButtonClickSound();
            System.out.println("New Game clicked");
        });

        loadButton[0].setOnAction(e -> {
            playButtonClickSound();
            System.out.println("Load Game clicked");
        });

        exitButton[0].setOnAction(e -> {
            playButtonClickSound();
            stage.close();
        });
    }

    private static ImageView loadLogo(String resourcePath) {
        InputStream is = MainMenu.class.getClassLoader().getResourceAsStream(resourcePath);
        if (is != null) {
            Image logo = new Image(is);
            return new ImageView(logo);
        } else {
            System.err.println("Logo not found: " + resourcePath);
            return new ImageView();
        }
    }

    private static BackgroundImage loadBackgroundImage(String resourcePath) {
        InputStream is = MainMenu.class.getClassLoader().getResourceAsStream(resourcePath);
        if (is != null) {
            Image image = new Image(is);
            return new BackgroundImage(
                    image,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER,
                    new BackgroundSize(100, 100, true, true, false, true)
            );
        } else {
            System.err.println("Background image not found: " + resourcePath);
            return null;
        }
    }

    private static Button createIconButton(String resourcePath, ImageView[] outImageView, Button[] outButton) {
        InputStream is = MainMenu.class.getClassLoader().getResourceAsStream(resourcePath);
        if (is == null) {
            System.err.println("Icon not found: " + resourcePath);
            return new Button(resourcePath);
        }

        Image image = new Image(is);
        ImageView imageView = new ImageView(image);
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(120); // начальное значение

        Button button = new Button("", imageView);
        button.setStyle("-fx-background-color: transparent;");

        DropShadow hoverShadow = new DropShadow(15, Color.rgb(0, 0, 0, 0.6));
        hoverShadow.setOffsetY(5);

        ScaleTransition enlarge = new ScaleTransition(Duration.millis(200), button);
        enlarge.setToX(1.05);
        enlarge.setToY(1.05);

        ScaleTransition shrink = new ScaleTransition(Duration.millis(200), button);
        shrink.setToX(1.0);
        shrink.setToY(1.0);

        button.setOnMouseEntered(e -> {
            button.setEffect(hoverShadow);
            enlarge.playFromStart();
        });

        button.setOnMouseExited(e -> {
            button.setEffect(null);
            shrink.playFromStart();
        });

        if (outImageView != null && outImageView.length > 0) outImageView[0] = imageView;
        if (outButton != null && outButton.length > 0) outButton[0] = button;

        return button;
    }

    private static void loadButtonClickSound(String resourcePath) {
        try {
            URL soundUrl = MainMenu.class.getClassLoader().getResource(resourcePath);
            if (soundUrl == null) {
                System.err.println("Button click sound not found: " + resourcePath);
                return;
            }
            buttonClickSound = new AudioClip(soundUrl.toString());
        } catch (Exception e) {
            System.err.println("Failed to load button click sound: " + e.getMessage());
        }
    }

    private static void playButtonClickSound() {
        if (buttonClickSound != null) {
            buttonClickSound.play();
        }
    }

    private static void playBackgroundMusic(String resourcePath) {
        try {
            if (backgroundMusicPlayer != null) backgroundMusicPlayer.stop();
            URL musicUrl = MainMenu.class.getClassLoader().getResource(resourcePath);
            if (musicUrl == null) {
                System.err.println("Background music not found: " + resourcePath);
                return;
            }
            Media media = new Media(musicUrl.toString());
            backgroundMusicPlayer = new MediaPlayer(media);
            backgroundMusicPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            backgroundMusicPlayer.setVolume(0.3);
            backgroundMusicPlayer.play();
        } catch (Exception e) {
            System.err.println("Failed to play background music: " + e.getMessage());
        }
    }
}
