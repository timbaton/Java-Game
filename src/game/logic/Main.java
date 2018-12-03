package game.logic;

import game.entities.Player;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();

        startGame();
    }

    private void startGame() {
        Player p = new Player(null, "Tim");

//        p.getGroup().setOnKeyPressed(value -> {
//            p.move(value.getCode());
//
//
//            if (value.getCode() == KeyCode.SPACE) {
//                Circle c = new Circle(p.getIv().getX(),p.getIv().getY(),10);
//                p.getGroup().getChildren().add(c);
//                new AnimationTimer() {
//                    private long was = 0;
//                    @Override
//                    public void handle(long now) {
//                        //if (now - was > 10) {
//                        //    c.setCenterX(c.getCenterX() + 20 * d);
//                        c.setCenterX(c.getCenterX());
//                        //    was = now;
//                        //}
//                    }
//                }.start();
//            }
//
//        });

    }


    public static void main(String[] args) {
        launch(args);
    }
}
