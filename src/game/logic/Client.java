package game.logic;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {

    private final BufferedReader readConsole = new BufferedReader(new InputStreamReader(System.in));
    private Socket socket;


    public Client(String host, int port) throws IOException {
        socket = new Socket(host, port);
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
                if (message != null)
                    System.out.println(message);
//                chatController.receiveMessage(message);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String newMessage) {
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
