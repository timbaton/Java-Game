package game.logic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

class Connection {
    private final Server server;
    private final Socket socket;

    Connection(Server server, Socket socket) {
        this.server = server;
        this.socket = socket;
    }

    void start() {
        new Thread(this::receiveOutputStream).start();
    }

    private void receiveOutputStream() {
        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));

            while (true) {
                String string = reader.readLine();
                notifyServer(string);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    Socket getSocket() {
        return socket;
    }

    private void notifyServer(String string) throws IOException {
        server.sendToAllPlayers(string, this);
    }
}
