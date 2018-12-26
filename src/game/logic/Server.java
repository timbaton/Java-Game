package game.logic;

import game.entities.Player;
import game.entities.WrittenPlayer;
import javafx.scene.shape.Circle;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private final List<Connection> clients;
    private final List<WrittenPlayer> players;

    private ServerSocket serverSocket;
    public static Server server;

    public Server() throws IOException {
        this.serverSocket = new ServerSocket(1);
        int maxConnections = 20;
        clients = new ArrayList<>(maxConnections);
        players = new ArrayList<>(maxConnections);
    }

    public void start() {
        new Thread(this::getConnections).start();
    }

    private void getConnections() {
        while (true) {
            Connection connection = createConnection();
            if (connection != null) {
                clients.add(connection);
//                System.out.println("connection added");
                connection.start();
            }
        }
    }

    private Connection createConnection() {
        try {
            return new Connection(this, serverSocket.accept());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    void sendToAllPlayers(String string, Connection sender) throws IOException {
        String[] message = string.split(":");
        System.out.print("server got message: ");
        for (String text : message) {
            System.out.print(text + " ");
        }
        System.out.println();

        if (message[0].equals(Player.CREATE)) {

            for (Connection connection : clients) {
                if (connection != sender) {
                    try {
                        PrintWriter writer = new PrintWriter(connection.getSocket().getOutputStream(), true);
                        writer.println(string);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    for (WrittenPlayer player : players) {
                        new PrintWriter(sender.getSocket().getOutputStream(), true).println(message[0] + ":" + player);
                    }
                }
            }
//            System.out.println("added new player " + message[2]);
            players.add(new WrittenPlayer(message[1], Integer.valueOf(message[2]), Integer.valueOf(message[3]), Integer.valueOf(message[4])));
        } else if (message[0].equals(Player.MOVE)) {
            WrittenPlayer player = findCircle(Integer.valueOf(message[2]), Integer.valueOf(message[3]));
            assert player != null;
            Integer newValue = Integer.valueOf(message[4]);
            if (message[1].equals("x")) {
                player.setX(newValue);
            } else {
                player.setY(newValue);
            }
            sendMessage(string, sender);

        } else if (message[0].equals(Player.KILL)) {
            deleteCircle(Integer.valueOf(message[1]), Integer.valueOf(message[2]));
            growCircle(Integer.valueOf(message[3]), Integer.valueOf(message[4]), Integer.valueOf(message[5]));
            sendMessage(string, sender);
        }
    }

    private void growCircle(Integer x, Integer y, Integer looserRad) {
        WrittenPlayer player = findCircle(x, y);
        assert player != null;
        player.setRadius(player.getRadius() + looserRad);
    }

    private void sendMessage(String string, Connection sender) {
        System.out.println("Server sended message: " + string);
        for (Connection connection : clients) {
            if (connection != sender) {
                try {
                    PrintWriter writer = new PrintWriter(connection.getSocket().getOutputStream(), true);
                    writer.println(string);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private WrittenPlayer findCircle(Integer x, Integer y) {
        for (WrittenPlayer player : players) {
            if (player.getY() == y && player.getX() == x) {
                return player;
            }
        }
        return null;
    }

    private void deleteCircle(Integer x, Integer y) {
        for (int i = 0; i < players.size(); i ++ ) {
            if (players.get(i).getY() == y && players.get(i).getX() == x) {
                players.remove(i);
            }
        }
    }


    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.start();
    }
}
