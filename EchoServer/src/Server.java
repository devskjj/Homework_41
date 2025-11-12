import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Server {
    private final int port;

    private Server(int port) {
        this.port = port;
    }

    public static Server bindToServer(int port) {
        return new Server(port);
    }

    public void run() {
        try (ServerSocket server = new ServerSocket(port)) {
            try (Socket socket = server.accept()) {
                handle(socket);
            }
        } catch (IOException e) {
            System.out.println("Вероятнее всего порт " + port + " занят.");
            e.printStackTrace();
        }
    }

    private void handle(Socket socket) throws IOException {
        InputStream inputStream = socket.getInputStream();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

        try (Scanner scanner = new Scanner(inputStreamReader)) {
            while (true) {
                String input = scanner.nextLine().trim();
                System.out.printf("Got message: %s%n", input);

                if (input.equalsIgnoreCase("bye")) {
                    System.out.println("Конец работы!");
                    return;
                }
            }
        } catch (NoSuchElementException e) {
            System.out.println("Client is disconnected!");
        }
    }
}
