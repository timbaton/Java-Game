package game.logic;

import java.io.IOException;

public class ServerSingleton {
    private static Server server;

    public static Server getServer() throws IOException {
        if (server == null) {
            server = new Server();
        }
        return server;
    }
}
