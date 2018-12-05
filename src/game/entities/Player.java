package game.entities;

import game.logic.Client;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.shape.Circle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Player {
    public final static String CREATE = "create";
    public final static String MOVE = "move";
    public final static int STEP = 5;

    private int x;
    private int y;
    private int name;
    private Circle circle;
    private List<Circle> circles;
    private Client client;

    private Group group = new Group();

    public Player(int name, int x, int y) throws IOException {
        this.name = name;
        this.x = x;
        this.y = y;
        circles = new ArrayList<>();
        circle = addCircle(name, x, y);

        circle.setOnKeyPressed(value -> action(value.getCode()));
        client = new Client("localhost", 1339, this);
        client.sendMessage(CREATE + ":" + this);
    }

    private Circle addCircle(int name, int x, int y) {
        Circle circle = new Circle();
        int radius = 50;
        circle.setRadius(radius);
        circle.setLayoutX(x);
        circle.setLayoutY(y);

        circles.add(circle);

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
            client.sendMessage(MOVE + ":y:" + getX() + ":" + getY() + ":" + (getY() + STEP));
//            System.out.println(MOVE + ":y:" + getY() + ":" + (getY() + STEP));
            setY(getY() + STEP);
        }
        if (code == KeyCode.UP) {
            client.sendMessage(MOVE + ":y:" + getX() + ":" + getY() + ":" + (getY() - STEP));
            setY(getY() - STEP);
        }

        if (code == KeyCode.RIGHT) {
            client.sendMessage(MOVE + ":x:" + getX() + ":" + getY() + ":" + (getX() + STEP));
            setX(getX() + STEP);
        }
        if (code == KeyCode.LEFT) {
            client.sendMessage(MOVE + ":x:" + getX() + ":" + getY() + ":" + (getX() - STEP));
            setX(getX() - STEP);
        }
    }

    private void setY(int i) {
        y = i;
        circle.setLayoutY(y);
    }

    private void setX(int i) {
        x = i;
        circle.setLayoutX(x);
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
                addCircle(Integer.valueOf(encode[1]), Integer.valueOf(encode[2]), Integer.valueOf(encode[3]));
                break;
            case MOVE:
                moveCircle(encode[1], Integer.valueOf(encode[2]), Integer.valueOf(encode[3]), Integer.valueOf(encode[4]));
        }
    }

    private void moveCircle(String where, Integer x, Integer y, Integer newValue) {
        for (Circle circle : circles) {
            if (circle.getLayoutY() == y && circle.getLayoutX() == x) {
                if (where.equals("y")) {
                    circle.setLayoutY(newValue);
                } else {
                    circle.setLayoutX(newValue);
                }
            }
        }
    }

//    move:405:y:346:351

    @Override
    public String toString() {
        return this.name + ":" + this.x + ":" + this.y;
    }
}