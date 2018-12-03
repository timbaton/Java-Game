package game.entities;

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

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    private int dir;
    private String name;
    private final int radius = 50;
    private int x;
    private int y;
    private Socket socket;
    private Circle circle;
    private List<Player> players;
    private Group group;

    public Player(Socket socket, String name, int x, int y) {
        this.x = x;
        this.y = y;
        players = new ArrayList<>();
        this.name = name;
        if (socket != null) {
            try {
                this.socket = socket;
                PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
                pw.println("new:" + name + ":" + x + ":" + y + ":" + dir);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Circle circle = new Circle();
        this.circle = circle;
        circle.setRadius(radius);
        circle.setLayoutX(getX());
        circle.setLayoutY(getY());
        group = new Group(circle);
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
            setY(getY() + 10);
        }
        if (code == KeyCode.LEFT) {
            setY(getY() - 10);
        }



    }

    private void setY(int i) {
        y = i;
        circle.setLayoutY(y);
    }
}
