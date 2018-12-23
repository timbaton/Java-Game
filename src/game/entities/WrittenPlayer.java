package game.entities;

public class WrittenPlayer {
    private int x;
    private int y;
    private int rad;
    private String name;

    public int getRadius() {
        return rad;
    }

    public void setRadius(int radius) {
        this.rad = radius;
    }


    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public WrittenPlayer(String name, int x, int y, int rad) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.rad = rad;
    }

    @Override
    public String toString() {
        return this.name + ":" + this.x + ":" + this.y + ":" + this.rad;
    }
}
