package game.ui;

import game.logic.StartingItems;
import game.model.Inventory;
import game.model.Player;
import game.logic.SurvivalStatus;
import game.ui.fx.MainUI;
import game.utils.GameSaveManager;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.*;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.InputStream;
import java.net.URL;

public class MainMenu {

    private static MediaPlayer backgroundMusicPlayer;
    private static AudioClip buttonClickSound;

    public static void show(Stage stage) {
        HBox buttonContainer = new HBox(30);
        buttonContainer.setStyle("-fx-alignment: center;");

        VBox root = new VBox(40);
        root.setStyle("-fx-alignment: center;");

        // ИКОНКИ
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

        // ЛОГО
        ImageView logoView = loadLogo("ui/logo.png");
        StackPane logoContainer = new StackPane(logoView);
        logoContainer.setMaxWidth(Double.MAX_VALUE);

        // ФОН
        BackgroundImage backgroundImage = loadBackgroundImage("ui/background.jpg");
        Background background = backgroundImage != null
                ? new Background(backgroundImage)
                : new Background(new BackgroundFill(Color.web("#1e1e1e"), null, null));

        root.setBackground(background);
        root.getChildren().addAll(logoContainer, buttonContainer);

        StackPane mainStack = new StackPane(root);

        // Чёрный оверлей для плавного появления
        Rectangle blackOverlay = new Rectangle();
        blackOverlay.setFill(Color.BLACK);
        blackOverlay.setOpacity(1.0);
        mainStack.getChildren().add(blackOverlay);

        // Получаем размеры экрана для полноэкранного запуска
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

        // Изначальная сцена — под размеры экрана
        Scene scene = new Scene(mainStack, screenBounds.getWidth(), screenBounds.getHeight());

        // Привязываем размеры оверлея к размерам сцены
        blackOverlay.widthProperty().bind(scene.widthProperty());
        blackOverlay.heightProperty().bind(scene.heightProperty());

        stage.setScene(scene);
        stage.setTitle("Robinson Survival - Main Menu");

        // Показываем окно
        stage.show();

        // Включаем полноэкранный режим с отключением выхода по ESC
        stage.setFullScreen(true);
        stage.setFullScreenExitHint("");
        stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);

        // Плавный переход
        FadeTransition fadeIn = new FadeTransition(Duration.millis(200), blackOverlay);
        fadeIn.setFromValue(1.0);
        fadeIn.setToValue(0.0);
        fadeIn.setOnFinished(e -> mainStack.getChildren().remove(blackOverlay));
        fadeIn.play();

        // Обновление размеров иконок и логотипа
        Runnable updateSizes = () -> {
            double buttonsWidth = buttonContainer.getWidth();
            if (buttonsWidth == 0) buttonsWidth = screenBounds.getWidth() * 0.7;  // запасное значение 70% экрана

            double iconSize = Math.min((buttonsWidth - 60) / 3, 200);
            if (newIcon[0] != null) newIcon[0].setFitWidth(iconSize);
            if (loadIcon[0] != null) loadIcon[0].setFitWidth(iconSize);
            if (exitIcon[0] != null) exitIcon[0].setFitWidth(iconSize);

            // Лого шириной по ширине контейнера с иконками (минус отступ)
            logoView.setFitWidth(buttonsWidth - 200);
            // Высоту подбираем пропорционально иконкам, плюс отступ
            logoView.setFitHeight(iconSize + 200);
        };

        // Обновлять при изменении размеров окна
        scene.widthProperty().addListener((obs, oldV, newV) -> updateSizes.run());
        scene.heightProperty().addListener((obs, oldV, newV) -> updateSizes.run());

        root.layout();
        updateSizes.run();

        // Звуки
        loadButtonClickSound("audio/button_click.wav");
        playBackgroundMusic("audio/background_music.mp3");

        // Действия кнопок с запуском игрового поля и звуками
        newButton[0].setOnAction(e -> {
            playButtonClickSound();
            backgroundMusicPlayer.stop();

            Player player = new Player();
            Inventory inventory = new Inventory();
            StartingItems.addInitialItems(inventory);
            SurvivalStatus status = new SurvivalStatus();

            MainUI mainUI = new MainUI(player, inventory, status);

            Scene gameScene = new Scene(mainUI.getRoot(), screenBounds.getWidth(), screenBounds.getHeight());
            stage.setScene(gameScene);

            stage.setFullScreen(true);
            stage.setFullScreenExitHint("");
            stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        });

        loadButton[0].setOnAction(e -> {
            playButtonClickSound();
            backgroundMusicPlayer.stop();

            Player player = new Player();
            Inventory inventory = new Inventory();
            SurvivalStatus status = new SurvivalStatus();

            int[] daysSurvivedHolder = new int[1];
            boolean loaded = GameSaveManager.loadGame(player, inventory, daysSurvivedHolder);
            if (loaded) {
                status.setDaysSurvived(daysSurvivedHolder[0]);
            } else {
                status.setDaysSurvived(0);
            }

            MainUI mainUI = new MainUI(player, inventory, status);

            Scene gameScene = new Scene(mainUI.getRoot(), screenBounds.getWidth(), screenBounds.getHeight());
            stage.setScene(gameScene);

            stage.setFullScreen(true);
            stage.setFullScreenExitHint("");
            stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        });

        exitButton[0].setOnAction(e -> {
            playButtonClickSound();
            stage.close();
        });
    }

    private static ImageView loadLogo(String resourcePath) {
        InputStream is = MainMenu.class.getClassLoader().getResourceAsStream(resourcePath);
        if (is != null) {
            return new ImageView(new Image(is));
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
        imageView.setFitWidth(120);

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
            if (soundUrl != null) {
                buttonClickSound = new AudioClip(soundUrl.toString());
            } else {
                System.err.println("Button click sound not found: " + resourcePath);
            }
        } catch (Exception e) {
            System.err.println("Failed to load button click sound: " + e.getMessage());
        }
    }

    private static void playButtonClickSound() {
        if (buttonClickSound != null) buttonClickSound.play();
    }

    private static void playBackgroundMusic(String resourcePath) {
        try {
            if (backgroundMusicPlayer != null) backgroundMusicPlayer.stop();
            URL musicUrl = MainMenu.class.getClassLoader().getResource(resourcePath);
            if (musicUrl != null) {
                Media media = new Media(musicUrl.toString());
                backgroundMusicPlayer = new MediaPlayer(media);
                backgroundMusicPlayer.setCycleCount(MediaPlayer.INDEFINITE);
                backgroundMusicPlayer.setVolume(0.3);
                backgroundMusicPlayer.play();
            } else {
                System.err.println("Background music not found: " + resourcePath);
            }
        } catch (Exception e) {
            System.err.println("Failed to play background music: " + e.getMessage());
        }
    }
}
