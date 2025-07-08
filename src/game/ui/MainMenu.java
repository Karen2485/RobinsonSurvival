package game.ui;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.InputStream;

public class MainMenu {

    public static void show(Stage stage) {
        VBox root = new VBox(20);
        root.setStyle("-fx-background-color: #1e1e1e; -fx-alignment: center;");

        Button newGameButton = createIconButton("ui/new_game.png");
        Button loadGameButton = createIconButton("ui/load_game.png");
        Button exitButton = createIconButton("ui/exit_game.png");

        newGameButton.setOnAction(e -> {
            // TODO: Переход к основному интерфейсу игры
            // Например: MainUI.show(stage);
            System.out.println("New Game clicked");
        });

        loadGameButton.setOnAction(e -> {
            // TODO: Загрузка сохранения и переход к интерфейсу
            System.out.println("Load Game clicked");
        });

        exitButton.setOnAction(e -> stage.close());

        root.getChildren().addAll(newGameButton, loadGameButton, exitButton);

        Scene scene = new Scene(root, 800, 600);
        stage.setScene(scene);
        stage.setTitle("Robinson Survival - Main Menu");
        stage.show();
    }

    private static Button createIconButton(String resourcePath) {
        InputStream is = MainMenu.class.getClassLoader().getResourceAsStream(resourcePath);
        if (is == null) {
            System.err.println("Icon not found: " + resourcePath);
            return new Button(resourcePath);  // fallback text button
        }
        Image image = new Image(is);
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(200);
        imageView.setFitHeight(80);
        Button button = new Button("", imageView);
        button.setStyle("-fx-background-color: transparent;");
        return button;
    }
}