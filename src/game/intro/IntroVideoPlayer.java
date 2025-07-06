package game.intro;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;

import java.net.URL;

public class IntroVideoPlayer extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Загружаем видео из ресурсов (папка resources)
        URL videoURL = getClass().getResource("/intro.mp4");
        if (videoURL == null) {
            System.out.println("Video file not found!");
            return;
        }

        String videoPath = videoURL.toExternalForm();

        Media media = new Media(videoPath);
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        MediaView mediaView = new MediaView(mediaPlayer);

        // Настраиваем сцену с видео
        StackPane root = new StackPane(mediaView);
        Scene scene = new Scene(root, 800, 600);

        primaryStage.setTitle("Robinson Survival - Intro");
        primaryStage.setScene(scene);
        primaryStage.show();

        mediaPlayer.setAutoPlay(true);

        // Закрываем окно после окончания видео
        mediaPlayer.setOnEndOfMedia(() -> primaryStage.close());
    }

    public static void main(String[] args) {
        launch(args);
    }
}