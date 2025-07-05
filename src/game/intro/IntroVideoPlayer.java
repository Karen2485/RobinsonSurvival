package game.intro;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class IntroVideoPlayer extends Application {

    @Override
    public void start(Stage primaryStage) {
        String videoPath = getClass().getResource("/intro.mp4").toExternalForm();

        Media media = new Media(videoPath);
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        MediaView mediaView = new MediaView(mediaPlayer);

        mediaPlayer.setAutoPlay(true);

        StackPane root = new StackPane(mediaView);
        Scene scene = new Scene(root, 590, 325);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Robinson Survival - Intro");
        primaryStage.show();

        mediaPlayer.setOnEndOfMedia(() -> primaryStage.close());
    }

    public static void main(String[] args) {
        launch(args);
    }
}