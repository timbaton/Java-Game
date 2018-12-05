package game.logic;

import game.entities.Player;
import game.entities.WrittenPlayer;

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
        this.serverSocket = new ServerSocket(1337);
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
                System.out.println("connection added");
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
        if (message[0].equals(Player.CREATE)) {
            System.out.println("added new player " + message[2]);
            players.add(new WrittenPlayer(message[1], Integer.valueOf(message[2]), Integer.valueOf(message[3])));
        }
    }

    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.start();
    }
}
