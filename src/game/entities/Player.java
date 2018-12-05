package game.entities;

import game.logic.Client;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.shape.Circle;

import java.io.IOException;
import java.util.Random;

public class Player {
    public final static String CREATE = "create";
    public final static String MOVE = "move";
    public final static int STEP = 5;

    private int x;
    private int y;
    private String name;
    private Circle circle;
    private Client client;

    private Group group = new Group();

    public Player(String name, int x, int y) throws IOException {
        this.name = name;
        this.x = x;
        this.y = y;
        circle = addCircle(name, x, y);
        circle.setOnKeyPressed(value -> action(value.getCode()));
        client = new Client("localhost", 1339, this);
        client.sendMessage(CREATE + ":" + this);
    }

    private Circle addCircle(String name, int x, int y) {
        Circle circle = new Circle();
        int radius = 50;
        circle.setRadius(radius);
        circle.setLayoutX(x);
        circle.setLayoutY(y);

        Platform.runLater(() -> {
                    group.getChildren().add(circle);
                }
        );
        return circle;
    }

    public Group getGroup() {
        return group;
    }

    public void action(KeyCode code) {
        if (code == KeyCode.DOWN) {
            client.sendMessage(MOVE + ":y:" + getY());
            setY(getY() + STEP);
        }
        if (code == KeyCode.UP) {
            setY(getY() - STEP);
        }

        if (code == KeyCode.RIGHT) {
            circle.setRadius(circle.getRadius() + STEP);
        }
        if (code == KeyCode.LEFT) {
            circle.setRadius(circle.getRadius() - STEP);

        }
    }

    private void setY(int i) {
        y = i;
        circle.setLayoutY(y);
    }

    private int getX() {
        return x;
    }

    private int getY() {
        return y;
    }

    public void receiveMessage(String message) {
        System.out.println(message);
        String[] encode = message.split(":");
        switch (encode[0]) {
            case CREATE:
                addCircle(encode[1], Integer.valueOf(encode[2]), Integer.valueOf(encode[3]));
                break;
        }
    }

    @Override
    public String toString() {
        return this.name + ":" + this.x + ":" + this.y;
    }
}