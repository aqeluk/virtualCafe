import Helpers.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Barista {
    private final static int port = 8888;
    private static final Cafe cafe = new Cafe();

    public static void main(String[] args) {RunServer();}
    private static void RunServer(){
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Awaiting customers...");
            while(true){
                Socket socket = serverSocket.accept();
                new Thread(new Server(socket, cafe)).start();
            }
        } catch (IOException e) {e.printStackTrace();}
    }
}
