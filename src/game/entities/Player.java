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
    private final String CREATE = "create";

    private int x;
    private int y;
    private Circle circle;

    private Random random = new Random();
    private Group group = new Group();

    public Player(String name, int x, int y) throws IOException {
        this.x = x;
        this.y = y;

        this.circle = addCircle();

        Client client = new Client("localhost", 1337, this);
        int a = random.nextInt();
        client.sendMessage(CREATE + ":" + a);
    }

    private Circle addCircle() {
        Circle circle = new Circle();
        int radius = 50;
        circle.setRadius(radius);
        circle.setLayoutX(getX());
        circle.setLayoutY(getY());

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
            setY(getY() + 10);
        }
        if (code == KeyCode.UP) {
            setY(getY() - 10);
        }

        if (code == KeyCode.RIGHT) {
            circle.setRadius(circle.getRadius() + 10);
        }
        if (code == KeyCode.LEFT) {
            circle.setRadius(circle.getRadius() - 10);

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
                addCircle();
                break;
        }
    }
}