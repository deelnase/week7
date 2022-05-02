package com.example.week7;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;

public class ex624 extends Application {
    private String totalTime;

    public static void main(String[] args) {
        launch(args);
    }

    @Override

    public void start(Stage primaryStage) throws Exception {
        Media media = new Media(new File("C:/Users/User/IdeaProjects/week7/srcSD.mp4").toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        MediaView mediaView = new MediaView(mediaPlayer);


        Label labelTime = new Label("00:00:00/00:00:00");
        mediaPlayer.setOnReady(() -> {
            Duration duration = mediaPlayer.getMedia().getDuration();
            int hours = (int) duration.toMillis() / 3600000;
            int minutes = (int) duration.toMillis() / 60000 % 60;
            int seconds = (int) duration.toMillis() / 1000 % 60;
            totalTime = String.format("%02d:%02d:%02d", hours, minutes, seconds);
            labelTime.setText("00:00:00/" + totalTime);
        });
        Slider slTime = new Slider();
        slTime.setPrefWidth(150);
        slTime.setMaxWidth(Region.USE_PREF_SIZE);
        slTime.setMinWidth(30);
        slTime.valueProperty().addListener((observable, oldValue, newValue) -> mediaPlayer.seek(Duration.millis((double) newValue / slTime.getMax() * mediaPlayer.getMedia().getDuration().toMillis())));

        KeyFrame keyFrame = new KeyFrame(Duration.millis(1000), event -> {
            Duration duration = mediaPlayer.getCurrentTime();
            int hours = (int) duration.toMillis() / 3600000;
            int minutes = (int) duration.toMillis() / 60000 % 60;
            int seconds = (int) duration.toMillis() / 1000 % 60;
            labelTime.setText(String.format("%02d:%02d:%02d/%s", hours, minutes, seconds, totalTime));
            slTime.setValue((mediaPlayer.getCurrentTime().toMillis() / mediaPlayer.getMedia().getDuration().toMillis() * slTime.getMax()));
        });
        Timeline timeline = new Timeline(keyFrame);
        timeline.setCycleCount(Timeline.INDEFINITE);

        Button playButton = new Button(">");
        playButton.setOnAction(e -> {
            if (playButton.getText().equals(">")) {
                mediaPlayer.play();
                timeline.play();
                playButton.setText("||");
            } else {
                mediaPlayer.pause();
                playButton.setText(">");
                timeline.pause();
            }
        });
        Slider slVolume = new Slider();
        slVolume.setPrefWidth(150);
        slVolume.setMaxWidth(Region.USE_PREF_SIZE);
        slVolume.setMinWidth(30);
        slVolume.setValue(50);
        mediaPlayer.volumeProperty().bind(slVolume.valueProperty().divide(100));
        HBox hBox = new HBox(10);
        hBox.setAlignment(Pos.CENTER);
        hBox.getChildren().addAll(playButton, new Label("Time"), slTime, labelTime, new Label("Volume"), slVolume);

        BorderPane pane = new BorderPane();
        pane.setCenter(mediaView);
        pane.setBottom(hBox);

        Scene scene = new Scene(pane, 400, 400);
        primaryStage.setTitle("16_24");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}