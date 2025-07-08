package game.ui;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.InputStream;

public class MainMenu {

    public static void show(Stage stage) {
        VBox buttonContainer = new VBox(30);
        buttonContainer.setStyle("-fx-alignment: center;");
        buttonContainer.setMaxWidth(300);
        buttonContainer.setFillWidth(false);

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

        newButton[0].setOnAction(e -> System.out.println("New Game clicked"));
        loadButton[0].setOnAction(e -> System.out.println("Load Game clicked"));
        exitButton[0].setOnAction(e -> stage.close());

        BackgroundImage backgroundImage = loadBackgroundImage("ui/background.jpg");
        Background background = backgroundImage != null
                ? new Background(backgroundImage)
                : new Background(new BackgroundFill(Color.web("#1e1e1e"), null, null));

        StackPane root = new StackPane();
        root.setPrefSize(800, 600);
        root.setBackground(background);
        root.getChildren().add(buttonContainer);

        Scene scene = new Scene(root, 900, 700);
        stage.setScene(scene);
        stage.setTitle("Robinson Survival - Main Menu");
        stage.show();

        FadeTransition fadeIn = new FadeTransition(Duration.millis(800), buttonContainer);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();

        Runnable updateIcons = () -> {
            double width = scene.getWidth();
            double height = scene.getHeight();
            double baseSize = Math.min(width, height);
            double iconSize = Math.min(baseSize / 3, 180);

            if (newIcon[0] != null) newIcon[0].setFitWidth(iconSize);
            if (loadIcon[0] != null) loadIcon[0].setFitWidth(iconSize);
            if (exitIcon[0] != null) exitIcon[0].setFitWidth(iconSize);
        };

        scene.widthProperty().addListener((obs, oldVal, newVal) -> updateIcons.run());
        scene.heightProperty().addListener((obs, oldVal, newVal) -> updateIcons.run());

        updateIcons.run();
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

        DropShadow hoverShadow = new DropShadow();
        hoverShadow.setRadius(15);
        hoverShadow.setOffsetY(5);
        hoverShadow.setColor(Color.rgb(0, 0, 0, 0.6));

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
}
