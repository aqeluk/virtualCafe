import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import helpers.Cafe;
import helpers.ThreadHandler;

public class Barista {

    public static void main(String[] args) {
        int portNumber = 8000; // Choose any free port number
        
        try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
            System.out.println("Barista server started on port " + portNumber);

            Cafe cafe = new Cafe();
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket);

                // Handle the client connection in a separate thread
                ThreadHandler threadHandler = new ThreadHandler(clientSocket, cafe);
                threadHandler.start();
            }
        } catch (IOException e) {
            System.err.println("Error in Barista server: " + e.getMessage());
        }
    }
}

