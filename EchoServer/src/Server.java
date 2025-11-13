import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;
import Enum.Commands;

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
        OutputStream outputStream = socket.getOutputStream();

        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        PrintWriter printWriter = new PrintWriter(outputStream, true);

        try (Scanner scanner = new Scanner(inputStreamReader)) {
            while (true) {
                String input = scanner.nextLine().trim();
//                String output = new StringBuilder(input).reverse().toString();
                String[] parts = input.split(" ", 2);
                try {
                String command = parts[0].toUpperCase();
                String message = parts[1];

                try {
                    Commands cmd = Commands.valueOf(command);
                    cmd.execute(message, printWriter);
                } catch (IllegalArgumentException e) {
                    printWriter.println(input);
                }
//                System.out.printf("Got message: %s%n", input);
//                printWriter.println(output);

//                if (input.equalsIgnoreCase("bye")) {
//                    System.out.println("Конец работы!");
//                    return;
//                }
            }
        } catch (NoSuchElementException e) {
            System.out.println("Client is disconnected!");
        }
    }
}
