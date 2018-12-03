package game.entities;

import game.logic.Client;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.shape.Circle;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Player {

    private int x;
    private int y;
    private Circle circle;
    private Group group;

    public Player(String name, int x, int y) throws IOException {
        this.x = x;
        this.y = y;

        Circle circle = new Circle();
        this.circle = circle;
        int radius = 50;
        circle.setRadius(radius);
        circle.setLayoutX(getX());
        circle.setLayoutY(getY());
        group = new Group(circle);

        Client client = new Client("localhost", 1337, this);
        client.sendMessage("client added");
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

    public void showMessage(String message) {
        System.out.println(message);
    }
}
