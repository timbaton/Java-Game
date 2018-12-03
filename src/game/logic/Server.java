package game.logic;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private final List<Connection> clients;

    private ServerSocket serverSocket;
    public static Server server;

    public Server() throws IOException {
        this.serverSocket = new ServerSocket(1337);
        int maxConnections = 20;
        clients = new ArrayList<>(maxConnections);
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

    void sendToAllPlayers(String string, Connection sender) {
        for (Connection connection : clients) {
            if (connection != sender) {
                try {
                    PrintWriter writer = new PrintWriter(connection.getSocket().getOutputStream());
                    writer.println(string);
                    writer.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.start();
    }
}
