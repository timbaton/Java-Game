package game.logic;


import game.entities.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {

    private final BufferedReader readConsole = new BufferedReader(new InputStreamReader(System.in));
    private Socket socket;
    private Player player;


    public Client(String host, int port, Player player) throws IOException {
        socket = new Socket(host, port);
        this.player = player;
        this.start();
    }

    private void start() {
        new Thread(this::receiveOutputStream).start();
    }

    private void receiveOutputStream() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while (true) {
                String message = reader.readLine();
                if (message != null) {
                    System.out.println("Player got message" + message);
                    player.receiveMessage(message);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String newMessage) {
        System.out.println("Player sended message" + newMessage);
        try {
            PrintWriter writer = new PrintWriter(socket.getOutputStream());
            writer.println(newMessage);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    public static void main(String[] args) throws IOException {
//        Client client = new Client("localhost", 1337);
//        client.start();
//    }

//    public void setController(ChatController chatController) {
//        this.chatController = chatController;
//    }
}
