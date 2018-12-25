package game.entities;

import game.logic.Client;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.input.KeyCode;
import javafx.scene.shape.Circle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Player {
    public final static String CREATE = "create";
    public final static String MOVE = "move";
    public final static String KILL = "kill";
    private static int speed = 12;
    public final static int RADIUS_INCOME_KILLING = 3;
    public final static int RADIUS_INCOME_EASY = 1;

    private int x;
    private int y;
    private int radius;
    private int name;
    private Circle circle;
    private List<Circle> circles;
    private Client client;

    private Group group = new Group();

    public Player(int name, int x, int y) throws IOException {
        this.name = name;
        this.x = x;
        this.y = y;
        this.radius = 50;
        circles = new ArrayList<>();
        circle = addCircle(name, x, y, radius);

        circle.setOnKeyPressed(value -> action(value.getCode()));
        client = new Client("localhost", 133, this);
        client.sendMessage(CREATE + ":" + this);
    }

    public void receiveMessage(String message) {
        String[] encode = message.split(":");
        switch (encode[0]) {
            case CREATE:
                addCircle(Integer.valueOf(encode[1]), Integer.valueOf(encode[2]), Integer.valueOf(encode[3]), Integer.valueOf(encode[4]));
                break;
            case MOVE:
                moveCircle(encode[1], Integer.valueOf(encode[2]), Integer.valueOf(encode[3]), Integer.valueOf(encode[4]));
                break;
            case KILL:
                killCircle(Integer.valueOf(encode[1]), Integer.valueOf(encode[2]), Integer.valueOf(encode[3]), Integer.valueOf(encode[4]), Integer.valueOf(encode[5]));
        }
    }

    private Circle addCircle(int name, int x, int y, int radius) {
        Circle circle = new Circle();
        circle.setRadius(radius);
        circle.setLayoutX(x);
        circle.setLayoutY(y);

        circle.boundsInParentProperty().addListener((a, b, d) -> {
            if (circle != this.circle && checkIntersect(circle, this.circle)) {
                handleKilling(circle, this.circle);
            }
        });
        circles.add(circle);

        Platform.runLater(() -> {
                    group.getChildren().add(circle);
                }
        );
        return circle;
    }

    private void handleKilling(Circle circle, Circle currentCircle) {
        Circle winner = currentCircle;
        Circle looser = circle;
        if (circle.getRadius() > currentCircle.getRadius()) {
            winner = circle;
            looser = currentCircle;
        }

//        if (circle.getRadius() != currentCircle.getRadius()) {
        client.sendMessage(KILL + ":" + (int) looser.getLayoutX() + ":" + (int) looser.getLayoutY() + ":"
                + (int) winner.getLayoutX() + ":" + (int) winner.getLayoutY() + ":" + (int) winner.getRadius());

        killCircle((int) looser.getLayoutX(), (int) looser.getLayoutY(),
                (int) winner.getLayoutX(), (int) winner.getLayoutY(), (int) looser.getRadius());
//        }
    }

    private boolean checkIntersect(Circle oneOfAllPlayers, Circle circle) {
        double allX = oneOfAllPlayers.getLayoutX();
        double curX = circle.getLayoutX();
        double squareOne = Math.pow(allX - curX, 2);
        double squareTwo = Math.pow(oneOfAllPlayers.getLayoutY() - circle.getLayoutY(), 2);
        double sqrt = Math.sqrt(squareOne + squareTwo);
        double distance = circle.getRadius() + oneOfAllPlayers.getRadius();
        return sqrt < distance;
    }

    public void action(KeyCode code) {
        if (code == KeyCode.DOWN) {
            client.sendMessage(MOVE + ":y:" + getX() + ":" + getY() + ":" + (getY() + speed));
            setY(getY() + speed);
        }

        if (code == KeyCode.UP) {
            client.sendMessage(MOVE + ":y:" + getX() + ":" + getY() + ":" + (getY() - speed));
            setY(getY() - speed);
        }

        if (code == KeyCode.RIGHT) {
            client.sendMessage(MOVE + ":x:" + getX() + ":" + getY() + ":" + (getX() + speed));
            setX(getX() + speed);
        }
        if (code == KeyCode.LEFT) {
            client.sendMessage(MOVE + ":x:" + getX() + ":" + getY() + ":" + (getX() - speed));
            setX(getX() - speed);
        }
    }

    private void killCircle(int killedX, int killedY, int winnerX, int winnerY, int radius) {
        Circle killed = findCircle(killedX, killedY);
        Circle winner = findCircle(winnerX, winnerY);
        if (killed != null) {
            killed.setLayoutX(-1999.0);
            killed.setLayoutY(-1999.0);
            circles.remove(killed);
            Platform.runLater(() -> {
                        group.getChildren().remove(killed);
                    }
            );
        }

        assert winner != null;
        winner.setRadius(winner.getRadius() + RADIUS_INCOME_KILLING);
        setSpeed(getSpeed() - 2);
    }

    private void moveCircle(String where, Integer x, Integer y, Integer newValue) {
        Circle circle = findCircle(x, y);

        if (circle != null) {
            if (where.equals("y")) {
                circle.setLayoutY(newValue);
            } else {
                circle.setLayoutX(newValue);
            }
        }
    }

    private Circle findCircle(Integer x, Integer y) {
        for (Circle circle : circles) {
            if (circle.getLayoutY() == y && circle.getLayoutX() == x) {
                return circle;
            }
        }
        return null;
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
        return (int) circle.getLayoutX();
    }

    private int getY() {
        return (int) circle.getLayoutY();
    }

    private int getSpeed() {
        return speed;
    }

    private void setSpeed(int speed) {
        if (speed < 2) {
            Player.speed = 2;
        } else {
            Player.speed = speed;
        }
    }

    public Group getGroup() {
        return group;
    }

    @Override
    public String toString() {
        return this.name + ":" + this.x + ":" + this.y + ":" + this.radius;
    }
}