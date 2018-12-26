package game.entities;

import game.logic.Client;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.effect.Lighting;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
    private StackPane stack;
    private List<StackPane> stacks;
    private Client client;

    private Group group = new Group();

    public Player(int name, int x, int y) throws IOException {
        this.name = name;
        this.x = x;
        this.y = y;

        Random rand = new Random();
        this.radius = rand.nextInt(12) + 8;

        stacks = new ArrayList<>();
        stack = addCircle(name, x, y, radius);

        stack.setOnKeyPressed(value -> action(value.getCode()));
        client = new Client("10.17.34.122", 1, this);
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

    private StackPane addCircle(int name, int x, int y, int radius) {
        Circle circle = new Circle(radius,  Color.rgb(156,216,255));
        circle.setEffect(new Lighting());

        //create a text inside a circle
        final Text text = new Text (String.valueOf(radius));
        text.setStroke(Color.BLACK);

        final StackPane stack = new StackPane();
        stack.getChildren().addAll(circle, text);
        stack.setLayoutX(x);
        stack.setLayoutY(y);

//        circle.setLayoutX(x);
//        circle.setLayoutY(y);

        stack.boundsInParentProperty().addListener((a, b, d) -> {
            if (stack != this.stack && checkIntersect(stack, this.stack)) {
                handleKilling(stack, this.stack);
            }
        });
        stacks.add(stack);

        Platform.runLater(() -> {
                    group.getChildren().add(stack);
                }
        );
        return stack;



        //create a layout for circle with text inside

    }

    private void handleKilling(StackPane stack, StackPane curStack) {

        Circle stackCircle = (Circle) stack.getChildren().get(0);
        Circle curStackCircle = (Circle) curStack.getChildren().get(0);

        if (stackCircle.getRadius() != curStackCircle.getRadius()) {
            StackPane winner = stack;
            StackPane looser = curStack;
            Circle winnerCircle = (Circle) winner.getChildren().get(0);
            Circle looserCircle = (Circle) looser.getChildren().get(0);

            if (stackCircle.getRadius() < curStackCircle.getRadius()) {
                winner = curStack;
                looser = stack;
                winnerCircle = (Circle) winner.getChildren().get(0);
                looserCircle = (Circle) looser.getChildren().get(0);
            }

//        if (stackCircle.getRadius() != curStackCircle.getRadius()) {
            client.sendMessage(KILL + ":" + (int) looser.getLayoutX() + ":" + (int) looser.getLayoutY() + ":"
                    + (int) winner.getLayoutX() + ":" + (int) winner.getLayoutY() + ":" + (int) looserCircle.getRadius());

            killCircle((int) looser.getLayoutX(), (int) looser.getLayoutY(),
                    (int) winner.getLayoutX(), (int) winner.getLayoutY(), (int) looserCircle.getRadius());
//        }
        }

    }

//    Circle oneCircle = (Circle) oneOfPlayers.getChildren().get(0);
//    Circle secondCircle = (Circle) curCircle.getChildren().get(0);
    
    private boolean checkIntersect(StackPane oneOfPlayers, StackPane curCircle) {
        double allX = curCircle.getLayoutX();
        double curX = oneOfPlayers.getLayoutX();
        double squareOne = Math.pow(allX - curX, 2);
        double squareTwo = Math.pow(oneOfPlayers.getLayoutY() - curCircle.getLayoutY(), 2);
        double sqrt = Math.sqrt(squareOne + squareTwo);

        Circle circle = (Circle) oneOfPlayers.getChildren().get(0);
        Circle currentCircle = (Circle) curCircle.getChildren().get(0);

        double distance = circle.getRadius() + currentCircle.getRadius();
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
        StackPane killed = findCircle(killedX, killedY);
        StackPane winner = findCircle(winnerX, winnerY);

        if (killed != null) {
            killed.setLayoutX(-1999.0);
            killed.setLayoutY(-1999.0);
            stacks.remove(killed);
            Platform.runLater(() -> {
                        group.getChildren().remove(killed);
                    }
            );
        }

        assert winner != null;
        Circle winnerCircle = (Circle) winner.getChildren().get(0);
        Text winnerText  = (Text) winner.getChildren().get(1);
        winnerCircle.setRadius(winnerCircle.getRadius() + radius);
        winnerText.setText(String.valueOf(winnerCircle.getRadius()));
        setSpeed(getSpeed() - 2);
    }

    private void moveCircle(String where, Integer x, Integer y, Integer newValue) {
        StackPane circle = findCircle(x, y);

        if (circle != null) {
            if (where.equals("y")) {
                circle.setLayoutY(newValue);
            } else {
                circle.setLayoutX(newValue);
            }
        }
    }

    private StackPane findCircle(Integer x, Integer y) {
        for (StackPane stackPane : stacks) {
            if (stackPane.getLayoutY() == y && stackPane.getLayoutX() == x) {
                return stackPane;
            }
        }
        return null;
    }

    private void setY(int i) {
        y = i;
        stack.setLayoutY(y);
    }

    private void setX(int i) {
        x = i;
        stack.setLayoutX(x);
    }

    private int getX() {
        return (int) stack.getLayoutX();
    }

    private int getY() {
        return (int) stack.getLayoutY();
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