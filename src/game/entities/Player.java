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
    private ImageView iv;
    private List<Player> players;
    private Group group;

    public Player(Socket socket, String name) {
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
//
        Circle circle = new Circle();
        circle.setRadius(radius);
        circle.setLayoutX(this.x);
        circle.setLayoutY(this.y);
        group = new Group(iv);
    }

    public List<Player> getPlayers() {
        return players;
    }

    public String getName() {
        return name;
    }

    public ImageView getIv() {
        return iv;
    }

    public Group getGroup() {
        return group;
    }

    public void move(KeyCode keyCode) {
        //System.out.println(value.getCode());
        if (keyCode == KeyCode.DOWN) {
            iv.setY(iv.getY() + 10);
        }
        if (keyCode == KeyCode.UP) {
            iv.setY(iv.getY() - 10);
        }
        if (keyCode == KeyCode.RIGHT) {
            iv.setX(iv.getX() + 10);
            if (dir == -1) {
                dir = 1;
                iv.setScaleX(1);
            }
            try {
                PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
                pw.println("x:" + name + ":" + (int) iv.getX() + ":" + dir);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (keyCode == KeyCode.LEFT) {
            iv.setX(iv.getX() - 10);
            if (dir == 1) {
                dir = -1;
                iv.setScaleX(-1);
            }
            try {
                PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
                pw.println("x:" + name + ":" + (int) iv.getX() + ":" + dir);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
