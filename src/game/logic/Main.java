package game.logic;

import game.entities.Player;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    private static final int SIZE = 800;

    @Override
    public void start(Stage primaryStage) throws Exception {
        startGame(primaryStage);
    }

    private void startGame(Stage primaryStage) throws IOException {
        Player p = new Player("Tim", SIZE / 2, SIZE / 2);

        p.getGroup().setOnKeyPressed(value -> {
            p.action(value.getCode());
        });


        primaryStage.setScene(new Scene(p.getGroup(), SIZE, SIZE));
        primaryStage.setTitle("Hello World");
        primaryStage.show();
        primaryStage.getScene().getRoot().requestFocus();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
