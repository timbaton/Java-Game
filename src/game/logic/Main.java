package game.logic;

import game.entities.Player;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Random;

public class Main extends Application {
    private static final int SIZE = 800;

    @Override
    public void start(Stage primaryStage) throws Exception {
        startGame(primaryStage);
    }

    private void startGame(Stage primaryStage) throws IOException {

        Random random = new Random();
        int x = random.nextInt(450 + 1 + 50) - 50;
        int y = random.nextInt(450 + 1 + 50) - 50;
        int name = random.nextInt(450 + 1 + 50) - 50;

        Player p = new Player(name, x, y);

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
